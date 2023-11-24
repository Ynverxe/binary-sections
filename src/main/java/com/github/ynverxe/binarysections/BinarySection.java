package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.format.BinaryStackFormat;
import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class BinarySection {

  private final int index;
  private final int usedPayloadCount;
  private final @NotNull ByteBuffer entireSection;

  public BinarySection(int index, int usedPayloadCount, @NotNull ByteBuffer entireSection) {
    this.index = index;
    this.usedPayloadCount = usedPayloadCount;
    this.entireSection = entireSection;
  }

  public int index() {
    return index;
  }

  public int payloadLength() {
    return entireSection.capacity();
  }

  public int internalSectionLength() {
    return entireSection.capacity() + BinaryStackFormat.USED_PAYLOAD_COUNT_LENGTH;
  }

  public @NotNull ByteBuffer entireSection() {
    return entireSection;
  }

  public int usedPayloadLength() {
    return usedPayloadCount;
  }

  public @NotNull ByteArrayView writtenBytes() {
    return ByteArrayView.subsequenceByLength(entireSection.array(), usedPayloadCount);
  }

  public byte @NotNull [] writtenBytesAsArray() {
    return writtenBytes().copy();
  }
}