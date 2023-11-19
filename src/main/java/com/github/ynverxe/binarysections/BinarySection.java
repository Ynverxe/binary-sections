package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class BinarySection {

  private final int index;
  private final int payloadLength;
  private final @NotNull ByteBuffer entireSection;

  public BinarySection(int index, int payloadLength, @NotNull ByteBuffer entireSection) {
    this.index = index;
    this.payloadLength = payloadLength;
    this.entireSection = entireSection;
  }

  public int index() {
    return index;
  }

  public @NotNull ByteBuffer entireSection() {
    return entireSection;
  }

  public int payloadLength() {
    return payloadLength;
  }

  public @NotNull ByteArrayView payload() {
    return ByteArrayView.subsequenceByLength(entireSection.array(), payloadLength);
  }

  public byte @NotNull [] payloadAsArray() {
    return payload().copy();
  }
}