package com.github.ynverxe.binarysections.util.array;

import com.github.ynverxe.binarysections.util.stream.ByteArrayViewInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ByteArrayView extends Iterable<Byte> {

  byte get(int index);

  int length();

  byte @NotNull [] copy();

  @NotNull ByteArrayView subsequence(int start, int end);

  @NotNull ByteArrayView subsequence(int start);

  @NotNull ByteArrayView subsequenceByLength(int length);

  @NotNull ByteBuffer asViewBuffer();

  default @NotNull InputStream newStream() {
    return new ByteArrayViewInputStream(this);
  }

  default @NotNull DataInput newDataInput() {
    return new DataInputStream(newStream());
  }

  static @NotNull ByteArrayView subsequence(byte[] array, int start, int end) {
    return new SimpleByteArrayView(array, start, end);
  }

  static @NotNull ByteArrayView subsequence(byte[] array, int start) {
    if (start < 0)
      throw new IllegalArgumentException("start < 0");

    if (array.length == 0) {
      return new EmptyByteArrayView();
    }

    return new SimpleByteArrayView(array, start, array.length - 1);
  }

  static @NotNull ByteArrayView subsequenceByLength(byte[] array, int length) {
    if (length <= 0)
      throw new IndexOutOfBoundsException("length <= 0");

    if (length > array.length)
      throw new IllegalArgumentException("length >= array.length");

    return new SimpleByteArrayView(array, 0, length - 1);
  }

  static @NotNull ByteArrayView of(byte[] array) {
    return new SimpleByteArrayView(array, 0, array.length - 1);
  }
}