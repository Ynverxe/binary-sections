package com.github.ynverxe.binarysections.io.source;

import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteArrayByteSource implements ByteSource {

  private byte[] array;

  public ByteArrayByteSource(byte[] array) {
    this.array = array;
  }

  @Override
  public @NotNull ByteBuffer readAtIndex(int index, int length) throws IOException, EOFException {
    if (index >= array.length || index + length - 1 >= array.length)
      throw new EOFException();

    ByteBuffer buffer = ByteBuffer.allocate(length);

    return (ByteBuffer) buffer.put(array, index, length)
      .position(0);
  }

  @Override
  public int writeAtIndex(int index, @NotNull ByteArrayView bytes) {
    int availableBytes = array.length - index;

    if (bytes.length() > availableBytes) {
      this.array = Arrays.copyOf(array, array.length + bytes.length() - availableBytes);
    }

    for (Byte aByte : bytes) {
      array[index] = aByte;
      index++;
    }

    return bytes.length();
  }

  @Override
  public void discardLastBytes(int byteCount) {
    this.array = Arrays.copyOfRange(array, 0, array.length - byteCount);
  }

  @Override
  public void close() throws IOException {
    array = null;
  }

  public byte @NotNull [] array() {
    return array;
  }
}