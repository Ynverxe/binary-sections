package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.exception.ByteArrayOutOfLimitException;
import com.github.ynverxe.binarysections.io.BinarySectionStackReader;
import com.github.ynverxe.binarysections.io.source.ByteSource;
import com.github.ynverxe.binarysections.io.BinarySectionStackWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;

import static com.github.ynverxe.binarysections.format.BinarySectionStackConstants.*;

public final class SimpleBinarySectionStack extends AbstractBinarySectionStackView implements BinarySectionStack {

  private final @NotNull ByteSource source;
  private final @NotNull BinarySectionStackWriter writer;
  private final @NotNull HeaderImpl header;

  private SimpleBinarySectionStack(@NotNull ByteSource source, @NotNull HeaderImpl header) {
    super(source);

    this.source = source;
    this.writer = new BinarySectionStackWriter(source);
    this.header = header;
  }

  @Override
  public void set(int index, byte[] bytes) throws IndexOutOfBoundsException, ByteArrayOutOfLimitException {
    checkIndex(index);

    checkLength(bytes.length);

    try {
      writer.doSectionWrite(HEADER_BYTES, header.payloadLength, index, bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setAll(int start, @NotNull Collection<byte[]> byteArrayArrayCollection) throws ByteArrayOutOfLimitException {
    checkIndex(start);
    checkIndex(start + byteArrayArrayCollection.size());

    for (byte[] bytes : byteArrayArrayCollection) {
      checkLength(bytes.length);
    }

    try {
      writer.writeFixedSections(byteArrayArrayCollection, header.payloadLength);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void add(byte[] bytes) throws ByteArrayOutOfLimitException {
    checkLength(bytes.length);

    int index = header.sectionCount++;

    try {
      writer.writeHeader(header.payloadLength, header.sectionCount);
      writer.doSectionWrite(HEADER_BYTES, header.payloadLength, index, bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addAll(Collection<byte[]> byteArrayCollection) throws ByteArrayOutOfLimitException {
    for (byte[] bytes : byteArrayCollection) {
      checkLength(bytes.length);
    }

    try {
      writer.writeHeader(header.payloadLength, header.sectionCount += byteArrayCollection.size());
      writer.writeFixedSections(byteArrayCollection, header.payloadLength);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(int index) throws IndexOutOfBoundsException {
    checkIndex(index);

    try {
      removeAndPull(index);
      writer.writeHeader(header.payloadLength(), --header.sectionCount);
      writer.truncate(header.sectionLength());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() {
    if (header.sectionCount == 0) return;

    try {
      writer.truncate(header.writtenSections() * header.sectionLength());
      writer.writeHeader(header.payloadLength, header.sectionCount = 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @NotNull BinarySectionStackView asViewable() {
    return new SimpleBinarySectionView(source);
  }

  @Override
  public @NotNull BinarySectionStackView.Header header() {
    return header;
  }

  private void removeAndPull(int index) throws IOException {
    ByteBuffer replace;

    boolean adjustNext;
    if (adjustNext = header.sectionCount > index + 1) {
      replace = ByteBuffer.wrap(get(index + 1)
        .payloadAsArray());
    } else {
      replace = ByteBuffer.allocate(header.payloadLength);
    }

    writer.doSectionWrite(HEADER_BYTES, header.payloadLength, index, replace.array());

    if (adjustNext) {
      removeAndPull(index + 1);
    }
  }

  private void checkIndex(int index) {
    if (index >= header.sectionCount) {
      throw new IndexOutOfBoundsException("Index '" + index + "' section count '" + header.sectionCount + "'");
    }
  }

  private void checkLength(int length) {
    if (length > header.payloadLength) {
      throw new ByteArrayOutOfLimitException("byte count is out of limit (" + header.payloadLength() + ")");
    }
  }

  static class HeaderImpl implements Header {
    private final int payloadLength;
    private int sectionCount;

    HeaderImpl(int payloadLength, int sectionCount) {
      this.payloadLength = payloadLength;
      this.sectionCount = sectionCount;
    }

    @Override
    public int payloadLength() {
      return payloadLength;
    }

    @Override
    public int writtenSections() {
      return sectionCount;
    }
  }

  static SimpleBinarySectionStack from(ByteSource source) throws IOException {
    BinarySectionStackReader reader = new BinarySectionStackReader(source);
    return new SimpleBinarySectionStack(source, new HeaderImpl(reader.readPayloadLength(), reader.readWrittenSections()));
  }

  static SimpleBinarySectionStack create(ByteSource source, int payloadLength) {
    if (payloadLength <= 0)
      throw new IllegalArgumentException("Section Length <= 0");

    SimpleBinarySectionStack sectionStack = new SimpleBinarySectionStack(source, new HeaderImpl(payloadLength, 0));
    try {
      sectionStack.writer.writeHeader(payloadLength, 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return sectionStack;
  }
}