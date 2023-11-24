package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.exception.ByteArrayOutOfLimitException;
import com.github.ynverxe.binarysections.io.source.ByteSource;
import com.github.ynverxe.binarysections.io.BinaryStackWriter;
import com.github.ynverxe.binarysections.view.BaseBinaryStackView;
import com.github.ynverxe.binarysections.view.BinaryStackView;
import com.github.ynverxe.binarysections.view.SimpleBinaryStackHeader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;

/**
 * Extendable class representing a writable binary stack.
 */
public class BaseBinaryStack extends BaseBinaryStackView implements BinaryStack {

  private final @NotNull ByteSource source;
  protected final @NotNull BinaryStackWriter writer;

  /**
   * A constructor to load an existent ByteStack data
   */
  public BaseBinaryStack(@NotNull ByteSource source) {
    super(source);

    this.source = source;
    this.writer = new BinaryStackWriter(source);
  }

  /**
   * A constructor to create a ByteStack on from an empty or non-empty ByteSource
   */
  public BaseBinaryStack(@NotNull ByteSource source, int payloadLength) {
    super(source, null);
    this.source = source;
    this.writer = new BinaryStackWriter(source);

    try {
      if (checkMagicNumber(true)) { // non-empty byte source
        try {
          int foundPayloadLength = reader.readPayloadLength();
          if (foundPayloadLength != payloadLength)
            throw new IllegalArgumentException("byte source has a different payloadLength");
        } catch (IllegalStateException ignore) {}
      } else {
        writer.writeMagicNumber();
        writer.writeHeader(payloadLength, 0);
      }

      this.header = new SimpleBinaryStackHeader(reader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void set(int index, byte[] bytes) throws IndexOutOfBoundsException, ByteArrayOutOfLimitException {
    checkIndex(index);

    checkLength(bytes.length);

    try {
      writer.doSectionWrite(sectionStackStart(), header.sectionLength(), index, bytes);
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
      writer.writeSections(sectionStackStart(), byteArrayArrayCollection, header.sectionLength());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void add(byte[] bytes) throws ByteArrayOutOfLimitException {
    checkLength(bytes.length);

    int writtenSections = header.writtenSections();

    try {
      writer.doSectionWrite(sectionStackStart(), header.sectionLength(), writtenSections, bytes);
      writer.writeWrittenSections(writtenSections + 1);
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
      writer.writeWrittenSections(header.writtenSections() + byteArrayCollection.size());
      writer.writeSections(sectionStackStart(), byteArrayCollection, header.sectionLength());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void remove(int index) throws IndexOutOfBoundsException {
    checkIndex(index);

    try {
      removeAndPull(index);

      writer.writeWrittenSections(header.writtenSections() - 1);
      writer.truncate(header.sectionLength());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() {
    int writtenSections = header.writtenSections();
    if (writtenSections == 0) return;

    try {
      writer.truncate(writtenSections * header.sectionLength());
      writer.writeWrittenSections(0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeMetadataSection(byte @NotNull [] bytes, boolean expandIfNeeded, boolean contractIfPossible) throws ByteArrayOutOfLimitException {
    int length = header.metadataLength();
    int skip = sectionStackStart();
    int sectionLength = header.sectionLength();

    try {
      if (bytes.length > length) {
        if (!expandIfNeeded) throw new ByteArrayOutOfLimitException("Header length is " + length);

        int neededSpace = bytes.length - length;
        for (int writtenSections = header.writtenSections() - 1; writtenSections >= 0; writtenSections--) {
          writer.pushSection(skip, writtenSections, sectionLength, neededSpace);
        }
      }

      if (bytes.length < length && contractIfPossible) {
        int dif = length - bytes.length;
        for (int i = 0; i < header.writtenSections(); i++) {
          writer.pullSection(skip, i, sectionLength, dif);
        }
      }

      writer.writeMetadata(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @NotNull BinaryStackView asViewable() {
    return new BaseBinaryStackView(source);
  }

  private void removeAndPull(int index) throws IOException {
    int sectionLength = header.sectionLength();
    int writtenSections = header.writtenSections();
    int skip = sectionStackStart();

    if (index + 1 < writtenSections) {
      for (int i = index + 1; i < header.writtenSections(); i++) {
        writer.pullSection(skip, i, sectionLength, sectionLength);
      }
    } else {
      writer.doSectionWrite(skip, sectionLength, index, new byte[index]);
    }
  }

  private void checkIndex(int index) {
    if (index >= header.writtenSections()) {
      throw new IndexOutOfBoundsException("Index '" + index + "' section count '" + header.writtenSections() + "'");
    }
  }

  private void checkLength(int length) {
    if (length > header.maxPayloadLength()) {
      throw new ByteArrayOutOfLimitException("byte count is out of limit (" + header.maxPayloadLength() + ")");
    }
  }
}