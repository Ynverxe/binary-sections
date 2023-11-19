package com.github.ynverxe.binarysections.io.source;

import org.jetbrains.annotations.NotNull;

public interface ThreadSafeByteSource extends ByteSource {
  @NotNull ByteSource original();
}