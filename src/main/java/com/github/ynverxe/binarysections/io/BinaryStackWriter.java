package com.github.ynverxe.binarysections.io;

import com.github.ynverxe.binarysections.io.source.ByteSource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Collection;

import static com.github.ynverxe.binarysections.format.BinaryStackFormat.*;
import static com.github.ynverxe.binarysections.util.write.BinaryDataRelocationUtil.relocateSection;

@ApiStatus.Internal
public final class BinaryStackWriter implements Closeable {

  private final @NotNull ByteSource source;

  public BinaryStackWriter(@NotNull ByteSource source) {
    this.source = source;
  }

  public void writeSections(int skip, Collection<byte[]> sections, int sectionLength) throws IOException {
    int index = 0;
    for (byte[] section : sections) {
      doSectionWrite(skip, sectionLength, index++, section);
    }

    writeWrittenSections(sections.size());
  }

  public void doSectionWrite(int skip, int sectionLength, int index, byte @NotNull [] payload) throws IOException {
    int writeStart = skip + (index * sectionLength);

    ByteBuffer sectionBuffer = ByteBuffer.allocate(sectionLength)
      .putInt(payload.length)
      .put(payload);

    source.writeAtIndex(writeStart, sectionBuffer);
  }

  public void pullSection(int skip, int index, int sectionLength, int amount) throws IOException {
    relocateSection(source, skip, index, sectionLength, -amount);
  }

  public void pushSection(int skip, int index, int sectionLength, int amount) throws IOException {
    relocateSection(source, skip, index, sectionLength, amount);
  }

  public void writeMagicNumber() throws IOException {
    source.writeAtIndex(0, ByteBuffer.allocate(MAGIC_NUMBER_LENGTH).putLong(MAGIC_NUMBER));
  }

  public void writeHeader(int payloadLength, int writtenSections) throws IOException {
    source.writeAtIndex(payloadLengthPosition(), ByteBuffer.allocate(MAX_PAYLOAD_SIZE_LENGTH).putInt(payloadLength));
    source.writeAtIndex(sectionCountPosition(), ByteBuffer.allocate(WRITTEN_SECTIONS_LENGTH).putInt(writtenSections));
  }

  public void writeWrittenSections(int writtenSections) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(WRITTEN_SECTIONS_LENGTH);
    buffer.putInt(writtenSections);
    source.writeAtIndex(sectionCountPosition(), buffer);
  }

  public void writeMetadata(byte[] bytes) throws IOException {
    source.writeAtIndex(customMetadataLengthPosition(), ByteBuffer.allocate(METADATA_SIZE_LENGTH).putInt(bytes.length));
    source.writeAtIndex(customMetadataPosition(), ByteBuffer.wrap(bytes));
  }

  public void truncate(int bytes) throws IOException {
    source.discardLastBytes(bytes);
  }

  @Override
  public void close() throws IOException {
    source.close();
  }
}