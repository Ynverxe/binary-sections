package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.io.RandomInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SimpleBinarySectionView extends AbstractBinarySectionStackView {

  private final Header header;

  public SimpleBinarySectionView(@NotNull RandomInputStream stream) {
    super(stream);

    this.header = new HeaderImpl();
  }

  @Override
  public @NotNull Header header() {
    return header;
  }

  class HeaderImpl implements Header {

    @Override
    public int payloadLength() {
      try {
        return reader.readPayloadLength();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
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
}