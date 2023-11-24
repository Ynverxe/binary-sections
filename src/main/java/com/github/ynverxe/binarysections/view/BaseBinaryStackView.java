package com.github.ynverxe.binarysections.view;

import com.github.ynverxe.binarysections.BinarySection;
import com.github.ynverxe.binarysections.format.BinaryStackFormat;
import com.github.ynverxe.binarysections.io.BinaryStackReader;
import com.github.ynverxe.binarysections.io.RandomInputStream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;

/**
 * Extendable class representing a binary stack view.
 */
public class BaseBinaryStackView implements BinaryStackView {

  protected final @NotNull BinaryStackReader reader;
  protected @NotNull BinaryStackHeader header;

  public BaseBinaryStackView(@NotNull RandomInputStream stream) {
    this.reader = new BinaryStackReader(stream);
    checkMagicNumber(false);
    this.header = new SimpleBinaryStackHeader(reader);
  }

  @ApiStatus.Experimental
  public BaseBinaryStackView(@NotNull RandomInputStream stream, @Nullable BinaryStackHeader header) {
    this.reader = new BinaryStackReader(stream);
    this.header = header;
  }

  @Override
  public @Nullable BinarySection get(int index) {
    int writtenSections = header.writtenSections();

    if (index >= writtenSections)
      throw new IndexOutOfBoundsException("Index is '" + index + "' but length is '" + writtenSections + "'");

    try {
      return reader.readSection(sectionStackStart(), header.sectionLength(), header.maxPayloadLength(), index);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public @NotNull BinaryStackHeader header() {
    return header;
  }

  protected boolean checkMagicNumber(boolean ignoreEOF) {
    try {
      long magic = reader.getMagicNumber();
      if (magic != 0 && magic != BinaryStackFormat.MAGIC_NUMBER)
        throw new IllegalStateException("Invalid input stream content (invalid magic number)");

      return magic != 0;
    } catch (IOException e) {
      if (e instanceof EOFException && ignoreEOF) {
        return false;
      }

      throw new RuntimeException(e);
    }
  }

  protected int sectionStackStart() {
    return BinaryStackFormat.payloadStartPosition(header.metadataLength());
  }
}