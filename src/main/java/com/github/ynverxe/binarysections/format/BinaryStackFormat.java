package com.github.ynverxe.binarysections.format;

public final class BinaryStackFormat {

  public static final long MAGIC_NUMBER = 0x42537461636L; // BStack
  public static final byte MAGIC_NUMBER_LENGTH = 8;
  public static final byte MAX_PAYLOAD_SIZE_LENGTH = 4;
  public static final byte WRITTEN_SECTIONS_LENGTH = 4;
  public static final byte USED_PAYLOAD_COUNT_LENGTH = 4;
  public static final byte METADATA_SIZE_LENGTH = 4;

  private BinaryStackFormat() {}

  public static int payloadLengthPosition() {
    return MAGIC_NUMBER_LENGTH;
  }

  public static int sectionCountPosition() {
    return MAGIC_NUMBER_LENGTH + MAX_PAYLOAD_SIZE_LENGTH;
  }

  public static int customMetadataLengthPosition() {
    return sectionCountPosition() + WRITTEN_SECTIONS_LENGTH;
  }

  public static int customMetadataPosition() {
    return customMetadataLengthPosition() + METADATA_SIZE_LENGTH;
  }

  public static int payloadStartPosition(int metadataLength) {
    return customMetadataPosition() + metadataLength;
  }
}