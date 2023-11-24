package com.github.ynverxe.binarysections.view;

import com.github.ynverxe.binarysections.format.BinaryStackFormat;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface BinaryStackHeader {

  int maxPayloadLength();

  int writtenSections();

  default int sectionLength() {
    return BinaryStackFormat.MAX_PAYLOAD_SIZE_LENGTH + maxPayloadLength();
  }

  int metadataLength();

  @NotNull ByteBuffer metadata();
}