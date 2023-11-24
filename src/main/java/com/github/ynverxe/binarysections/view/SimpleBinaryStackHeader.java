package com.github.ynverxe.binarysections.view;

import com.github.ynverxe.binarysections.io.BinaryStackReader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SimpleBinaryStackHeader implements BinaryStackHeader {

  private final int payloadLength;
  private final BinaryStackReader reader;

  public SimpleBinaryStackHeader(@NotNull BinaryStackReader reader) {
    this.reader = reader;
    try {
      this.payloadLength = reader.readPayloadLength();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int maxPayloadLength() {
    return payloadLength;
  }

  @Override
  public int writtenSections() {
    try {
      return reader.readWrittenSections();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}