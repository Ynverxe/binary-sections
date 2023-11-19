package com.github.ynverxe.binarysections.io;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface RandomInputStream extends Closeable {

  @NotNull ByteBuffer readAtIndex(int index, int length) throws IOException;

}