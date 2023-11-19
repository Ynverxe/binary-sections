package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.io.BinarySectionStackReader;
import com.github.ynverxe.binarysections.io.RandomInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public abstract class AbstractBinarySectionStackView implements BinarySectionStackView {

  protected final @NotNull BinarySectionStackReader reader;

  public AbstractBinarySectionStackView(@NotNull RandomInputStream stream) {
    this.reader = new BinarySectionStackReader(stream);
  }

  @Override
  public @Nullable BinarySection get(int index) {
    try {
      return reader.readSection(header(), index);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}