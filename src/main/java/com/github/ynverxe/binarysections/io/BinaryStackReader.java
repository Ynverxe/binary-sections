package com.github.ynverxe.binarysections.io;

import com.github.ynverxe.binarysections.BinarySection;
import com.github.ynverxe.binarysections.format.BinaryStackFormat;
import com.github.ynverxe.binarysections.view.BinaryStackHeader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.github.ynverxe.binarysections.format.BinaryStackFormat.*;

@ApiStatus.Internal
public final class BinaryStackReader implements Closeable {

  private final RandomInputStream stream;

  public BinaryStackReader(RandomInputStream stream) {
    this.stream = stream;
  }
  
  public long getMagicNumber() throws IOException {
    return stream.readAtIndex(0, MAGIC_NUMBER_LENGTH).getLong();
  }

  public int readPayloadLength() throws IOException {
    ByteBuffer buffer = stream.readAtIndex(payloadLengthPosition(), MAX_PAYLOAD_SIZE_LENGTH);

    int read = buffer.getInt();
    if (read <= 0)
      throw new IllegalStateException("Missing Payload Length");

    return read;
  }

  public int readWrittenSections() throws IOException {
    ByteBuffer buffer = stream.readAtIndex(sectionCountPosition(), WRITTEN_SECTIONS_LENGTH);

    return buffer.getInt();
  }

  public int readCustomMetadataLength() throws IOException {
    return stream.readAtIndex(customMetadataLengthPosition(), METADATA_SIZE_LENGTH).getInt();
  }

  public byte[] readCustomMetadata() throws IOException {
    int length = readCustomMetadataLength();

    if (length == 0) return new byte[0];

    return stream.readAtIndex(customMetadataPosition(), length).array();
  }

  public @NotNull BinarySection readSection(int skip, int sectionLength, int maxPayloadLength, int index) throws IOException {
    int start = skip + index * sectionLength;

    int payloadLength = stream.readAtIndex(start, USED_PAYLOAD_COUNT_LENGTH).getInt();
    ByteBuffer bytes = stream.readAtIndex(start + USED_PAYLOAD_COUNT_LENGTH, maxPayloadLength);

    return new BinarySection(index, payloadLength, bytes);
  }

  @Override
  public void close() throws IOException {
    stream.close();
  }
}