package com.github.ynverxe.binarysections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;

public interface BinarySectionStackView {

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

  @NotNull Header header();

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

  interface Header {
    int sectionLength();

    int writtenSections();
  }
}