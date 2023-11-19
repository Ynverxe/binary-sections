package com.github.ynverxe.binarysections.util.array;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;

public final class SimpleByteArrayView implements ByteArrayView {

  private final int start, end;
  private final int length;
  private final byte[] bytes;
  private final ByteBuffer buffer;

  SimpleByteArrayView(byte[] bytes, int start, int end) {
    this.bytes = bytes;
    this.buffer = ByteBuffer.wrap(bytes);
    this.start = start;
    this.end = end;
    this.length = (end - start) + 1;
  }

  @Override
  public byte get(int index) {
    if (index >= length)
      throw new IndexOutOfBoundsException("Index '" + index + "' but length '" + length + "'");

    return bytes[start + index];
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public byte @NotNull [] copy() {
    return Arrays.copyOfRange(bytes, start, end + 1);
  }

  @Override
  public @NotNull Iterator<Byte> iterator() {
    return new ViewableIterator();
  }

  @Override
  public @NotNull ByteArrayView subsequence(int start) {
    return ByteArrayView.subsequence(bytes, start);
  }

  @Override
  public @NotNull ByteArrayView subsequence(int start, int end) {
    return ByteArrayView.subsequence(bytes, start, end);
  }

  @Override
  public @NotNull ByteArrayView subsequenceByLength(int length) {
    return ByteArrayView.subsequenceByLength(bytes, length);
  }

  @Override
  public @NotNull ByteBuffer asViewBuffer() {
    return buffer.asReadOnlyBuffer();
  }

  class ViewableIterator implements Iterator<Byte> {

    private int index = -1;

    @Override
    public boolean hasNext() {
      return length > index + 1;
    }

    @Override
    public Byte next() {
      return get(++index);
    }
  }
}