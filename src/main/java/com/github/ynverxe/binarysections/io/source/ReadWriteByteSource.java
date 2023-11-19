package com.github.ynverxe.binarysections.io.source;

import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteByteSource implements ThreadSafeByteSource {

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private final Lock readLock = readWriteLock.readLock();
  private final Lock writeLock = readWriteLock.writeLock();

  private final @NotNull ByteSource backing;

  public ReadWriteByteSource(@NotNull ByteSource backing) {
    this.backing = backing;
  }

  @Override
  public @NotNull ByteSource original() {
    return backing;
  }

  @Override
  public @NotNull ThreadSafeByteSource threadSafeSource() {
    return this;
  }

  @Override
  public @NotNull ByteBuffer readAtIndex(int index, int length) throws IOException {
    try {
      readLock.lock();
      return backing.readAtIndex(index, length);
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public int writeAtIndex(int index, @NotNull ByteArrayView bytes) throws IOException {
    try {
      writeLock.lock();
      return backing.writeAtIndex(index, bytes);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void discardLastBytes(int byteCount) throws IOException {
    try {
      writeLock.lock();
      backing.discardLastBytes(byteCount);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void close() throws IOException {
    synchronized (backing) {
      backing.close();
    }
  }
}