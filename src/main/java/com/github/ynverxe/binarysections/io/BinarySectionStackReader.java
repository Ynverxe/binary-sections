package com.github.ynverxe.binarysections.io;

import com.github.ynverxe.binarysections.BinarySection;
import com.github.ynverxe.binarysections.BinarySectionStackView;
import com.github.ynverxe.binarysections.format.BinarySectionStackConstants;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

@ApiStatus.Internal
public final class BinarySectionStackReader implements Closeable {

  private final RandomInputStream stream;

  public BinarySectionStackReader(RandomInputStream stream) {
    this.stream = stream;
  }

  public int readPayloadLength() throws IOException {
    ByteBuffer buffer = stream.readAtIndex(0, 4);

    int read = buffer.getInt();
    if (read <= 0)
      throw new IllegalStateException("Missing Section Length");

    return read;
  }

  public int readWrittenSections() throws IOException {
    ByteBuffer buffer = stream.readAtIndex(3, 4);

    int read = buffer.getInt();
    if (read <= 0)
      throw new IllegalStateException("Missing Section Length");

    return read;
  }

  public @NotNull BinarySection readSection(@NotNull BinarySectionStackView.Header header, int index) throws IOException {
    int sectionLength = header.sectionLength();
    int toSkip = index * sectionLength;

    int start = BinarySectionStackConstants.HEADER_BYTES + toSkip;

    int payloadLength = stream.readAtIndex(start, 4).getInt();
    ByteBuffer bytes = stream.readAtIndex(start + 4, header.payloadLength());

    return new BinarySection(index, payloadLength, bytes);
  }

  @Override
  public void close() throws IOException {
    stream.close();
  }
}