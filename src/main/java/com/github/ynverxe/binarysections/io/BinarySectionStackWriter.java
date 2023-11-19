package com.github.ynverxe.binarysections.io;

import com.github.ynverxe.binarysections.format.BinarySectionStackConstants;
import com.github.ynverxe.binarysections.io.source.ByteSource;
import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;

@ApiStatus.Internal
public final class BinarySectionStackWriter implements Closeable {

  private final @NotNull ByteSource source;

  public BinarySectionStackWriter(@NotNull ByteSource source) {
    this.source = source;
  }

  public void writeFixedSections(Collection<byte[]> sections, int payloadLength) throws IOException {
    doSectionsWrite(payloadLength, sections);
  }

  private void doSectionsWrite(int payloadLength, Collection<byte[]> sections) throws IOException {
    int headerBytes = BinarySectionStackConstants.HEADER_BYTES;
    writeHeader(payloadLength, sections.size());

    int index = 0;
    for (byte[] section : sections) {
      doSectionWrite(headerBytes, payloadLength, index++, section);
    }
  }

  public void doSectionWrite(int skip, int payloadLength, int index, byte @NotNull [] payload) throws IOException {
    int bytesPerSection = BinarySectionStackConstants.PAYLOAD_LENGTH + payloadLength;
    int writeStart = skip + (index * bytesPerSection);

    ByteBuffer sectionBuffer = ByteBuffer.allocate(bytesPerSection)
      .putInt(payload.length)
      .put(payload);

    source.writeAtIndex(writeStart, sectionBuffer);
  }

  public void writeHeader(int payloadLength, int writtenSections) throws IOException {
    int headerBytes = BinarySectionStackConstants.HEADER_BYTES;
    ByteBuffer buffer = ByteBuffer.allocate(headerBytes);

    buffer.putInt(payloadLength);
    buffer.putInt(writtenSections);

    source.writeAtIndex(0, ByteArrayView.of(buffer.array()));
  }

  public void truncate(int bytes) throws IOException {
    source.discardLastBytes(bytes);
  }

  @Override
  public void close() throws IOException {
    source.close();
  }
}