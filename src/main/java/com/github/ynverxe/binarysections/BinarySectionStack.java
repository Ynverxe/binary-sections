package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.exception.ByteArrayOutOfLimitException;
import com.github.ynverxe.binarysections.io.source.ByteSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

public interface BinarySectionStack extends BinarySectionStackView {

  void set(int index, byte[] bytes) throws IndexOutOfBoundsException, ByteArrayOutOfLimitException;

  void setAll(int start, @NotNull Collection<byte[]> bytes) throws ByteArrayOutOfLimitException;

  default void setAll(int start, byte[]... bytes) throws ByteArrayOutOfLimitException {
    setAll(start, Arrays.asList(bytes));
  }

  void add(byte[] byteArrayArray) throws ByteArrayOutOfLimitException;

  void addAll(Collection<byte[]> byteArrayCollection) throws ByteArrayOutOfLimitException;

  default void addAll(byte[]... byteArrayArray) throws ByteArrayOutOfLimitException {
    addAll(Arrays.asList(byteArrayArray));
  }

  void remove(int index) throws IndexOutOfBoundsException;

  void clear();

  default void set(int index, @NotNull ByteBuffer buffer) throws IndexOutOfBoundsException, ByteArrayOutOfLimitException {
    set(index, buffer.array());
  }

  default void erase(int index) throws IndexOutOfBoundsException {
    set(index, ByteBuffer.allocate(header().payloadLength()));
  }

  @NotNull BinarySectionStackView asViewable();

  static @NotNull BinarySectionStack from(@NotNull ByteSource source) throws IOException {
    return SimpleBinarySectionStack.from(source);
  }

  static @NotNull BinarySectionStack create(@NotNull ByteSource source, int payloadLength) throws IllegalArgumentException {
    return SimpleBinarySectionStack.create(source, payloadLength);
  }
}