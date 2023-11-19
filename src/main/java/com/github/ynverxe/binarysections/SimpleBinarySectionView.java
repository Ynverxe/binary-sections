package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.util.ByteArrayView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReaderBinarySectionView implements BinarySectionStackView {

  private final ByteArrayView byteArrayView;

  @Override
  public @Nullable BinarySection get(int index) {
    return null;
  }

  @Override
  public @NotNull Header header() {
    return null;
  }
}