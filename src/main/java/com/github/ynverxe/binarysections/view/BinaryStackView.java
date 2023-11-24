package com.github.ynverxe.binarysections.view;

import com.github.ynverxe.binarysections.BinarySection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Stack;

public interface BinaryStackView {

  @Nullable BinarySection get(int index);

  default @Nullable BinarySection first() {
    int written = header().writtenSections();

    if (written == 0) return null;

    return get(0);
  }

  default @Nullable BinarySection last() {
    int written = header().writtenSections();

    if (written == 0) return null;

    return get(written - 1);
  }

  @NotNull BinaryStackHeader header();

  default @NotNull Stack<BinarySection> all() {
    Stack<BinarySection> sections = new Stack<>();

    int written = header().writtenSections();

    if (written != 0) {
      for (int i = 0; i < written; i++) {
        sections.add(get(i));
      }
    }

    return sections;
  }

  int metadataLength();

  @NotNull ByteBuffer metadata();
}