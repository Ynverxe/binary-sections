package com.github.ynverxe.binarysections.view;

import com.github.ynverxe.binarysections.format.BinaryStackFormat;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface BinaryStackHeader {

  /**
   * The length of every payload section.
   */
  int maxPayloadLength();

  /**
   * The number of sections written in the stack data.
   */
  int writtenSections();

  /**
   * The length of a complete section, this length is four bytes for an integer number
   * denoting the used payload count followed by the entire payload length.
   * This value is not present into the stack data, it's just a utility method.
   */
  default int sectionLength() {
    return BinaryStackFormat.MAX_PAYLOAD_SIZE_LENGTH + maxPayloadLength();
  }

  int metadataLength();

  @NotNull ByteBuffer metadata();
}