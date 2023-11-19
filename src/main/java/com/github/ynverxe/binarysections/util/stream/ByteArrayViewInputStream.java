package com.github.ynverxe.binarysections.util.stream;

import com.github.ynverxe.binarysections.util.array.ByteArrayView;

import java.io.IOException;
import java.io.InputStream;

public class ByteArrayViewInputStream extends InputStream {

  private int position;
  private ByteArrayView view;

  public ByteArrayViewInputStream(ByteArrayView view) {
    this.view = view;
  }

  @Override
  public int read() throws IOException {
    return view.get(position++) & 0xFF;
  }

  @Override
  public void close() throws IOException {
    view = null;
  }

  @Override
  public int available() throws IOException {
    return view.length() - position;
  }

  @Override
  public synchronized void reset() throws IOException {
    position = 0;
  }
}