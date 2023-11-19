package com.github.ynverxe.binarysections.util.array;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Iterator;

public class EmptyByteArrayView implements ByteArrayView {

  @Override
  public byte get(int index) {
    throw new IndexOutOfBoundsException("Index '0' but length '0'");
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public byte @NotNull [] copy() {
    return new byte[0];
  }

  @Override
  public @NotNull ByteArrayView subsequence(int start, int end) {
    throw new UnsupportedOperationException("Empty View");
  }

  @Override
  public @NotNull ByteArrayView subsequence(int start) {
    throw new UnsupportedOperationException("Empty View");
  }

  @Override
  public @NotNull ByteArrayView subsequenceByLength(int length) {
    throw new UnsupportedOperationException("Empty View");
  }

  @Override
  public @NotNull ByteBuffer asViewBuffer() {
    return ByteBuffer.allocate(0);
  }

  @NotNull
  @Override
  public Iterator<Byte> iterator() {
    return new Iterator<Byte>() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public Byte next() {
        throw new UnsupportedOperationException("Empty View");
      }
    };
  }
}