package com.github.ynverxe.binarysections.io;

import com.github.ynverxe.binarysections.util.ByteArrayView;
import com.github.ynverxe.binarysections.util.SimpleByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface ByteSource extends RandomInputStream {

  int writeAtIndex(int index, @NotNull ByteArrayView bytes);

  default int writeAtIndex(int index, @NotNull ByteBuffer byteBuffer) {
    return writeAtIndex(index, ByteArrayView.of(byteBuffer.array()));
  }

  default int writeAtIndex(int index, byte @NotNull [] bytes) {
    return writeAtIndex(index, ByteArrayView.of(bytes));
  }

  default void discardLastBytes(int byteCount) {}

  static @NotNull ByteArrayByteSource ofBytes(int length) {
    return ofBytes(new byte[length]);
  }

  static @NotNull ByteArrayByteSource ofBytes(byte[] bytes) {
    return new ByteArrayByteSource(bytes);
  }
}