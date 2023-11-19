package com.github.ynverxe.binarysections.io.source;

import com.github.ynverxe.binarysections.io.RandomInputStream;
import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public interface ByteSource extends RandomInputStream {

  int writeAtIndex(int index, @NotNull ByteArrayView bytes) throws IOException;

  default int writeAtIndex(int index, @NotNull ByteBuffer byteBuffer) throws IOException {
    return writeAtIndex(index, ByteArrayView.of(byteBuffer.array()));
  }

  default int writeAtIndex(int index, byte @NotNull [] bytes) throws IOException {
    return writeAtIndex(index, ByteArrayView.of(bytes));
  }

  default void discardLastBytes(int byteCount) throws IOException {}

  default @NotNull ThreadSafeByteSource threadSafeSource() {
    return this instanceof ThreadSafeByteSource ? (ThreadSafeByteSource) this : new ReadWriteByteSource(this);
  }

  static @NotNull ByteArrayByteSource ofBytes(int length) {
    return ofBytes(new byte[length]);
  }

  static @NotNull ByteArrayByteSource ofBytes(byte[] bytes) {
    return new ByteArrayByteSource(bytes);
  }

  static @NotNull FileChannelByteSource byFileChannel(@NotNull FileChannel fileChannel) {
    if (!fileChannel.isOpen())
      throw new IllegalArgumentException("FileChannel is not open");

    return new FileChannelByteSource(fileChannel);
  }

  static @NotNull FileChannelByteSource byRandomAccessFile(@NotNull RandomAccessFile randomAccessFile) {
    return byFileChannel(randomAccessFile.getChannel());
  }
}