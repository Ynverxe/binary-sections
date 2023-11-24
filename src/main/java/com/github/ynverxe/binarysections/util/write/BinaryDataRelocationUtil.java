package com.github.ynverxe.binarysections.util.write;

import com.github.ynverxe.binarysections.io.source.ByteSource;

import java.io.IOException;
import java.nio.ByteBuffer;

public final class BinaryDataRelocationUtil {

  public static void relocateSection(ByteSource source, int skip, int index, int sectionLength, int amount) throws IOException {
    int start = skip + (index * sectionLength);
    int dest = start + amount;

    ByteBuffer read = source.readAtIndex(start, sectionLength);
    source.writeAtIndex(dest, read);
  }
}