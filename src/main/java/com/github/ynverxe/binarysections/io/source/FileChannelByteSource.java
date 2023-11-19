package com.github.ynverxe.binarysections.io.source;

import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelByteSource implements ThreadSafeByteSource {

  private final @NotNull FileChannel fileChannel;

  public FileChannelByteSource(@NotNull FileChannel fileChannel) {
    this.fileChannel = fileChannel;
  }

  @Override
  public @NotNull ByteSource original() {
    return this;
  }

  @Override
  public @NotNull ByteBuffer readAtIndex(int index, int length) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(length);
    fileChannel.read(buffer, index);
    return buffer;
  }

  @Override
  public int writeAtIndex(int index, @NotNull ByteArrayView bytes) throws IOException {
    return fileChannel.write(bytes.asViewBuffer(), index);
  }

  @Override
  public void close() throws IOException {
    fileChannel.close();
  }

  public @NotNull FileChannel fileChannel() {
    return fileChannel;
  }
}