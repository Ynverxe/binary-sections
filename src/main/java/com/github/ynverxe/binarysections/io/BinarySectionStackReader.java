package com.github.ynverxe.binarysections.format;

import com.github.ynverxe.binarysections.io.RandomInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;

public final class BinarySectionStackHelper {

  private BinarySectionStackHelper() {}

  public static int readpayloadLength(@NotNull RandomInputStream stream) throws IOException {
    ByteBuffer buffer = stream.readAtIndex(0, 4);

    int read = buffer.getInt();
    if (read <= 0)
      throw new IllegalStateException("Missing Section Length");

    return read;
  }

  public static int readWrittenSections(@NotNull RandomInputStream stream) throws IOException {
    ByteBuffer buffer = stream.readAtIndex(3, 4);

    int read = buffer.getInt();
    if (read <= 0)
      throw new IllegalStateException("Missing Section Length");

    return read;
  }
}