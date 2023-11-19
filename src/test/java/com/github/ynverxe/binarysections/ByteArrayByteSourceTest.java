package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.io.source.ByteArrayByteSource;
import com.github.ynverxe.binarysections.io.source.ByteSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

public class ByteArrayByteSourceTest {
  @Test
  public void testGrown() throws IOException {
    ByteArrayByteSource source = ByteSource.ofBytes(10);
    source.writeAtIndex(0, ByteBuffer.allocate(92));

    assertEquals(92, source.array().length);
  }
}