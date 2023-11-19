package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.util.array.ByteArrayView;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ByteArrayViewTest {

  private static final byte[] ENCODED_TEXT = "Hello World".getBytes(StandardCharsets.UTF_8);

  @Test
  public void testSubsequence() {
    ByteArrayView subsequence = ByteArrayView.subsequenceByLength(ENCODED_TEXT, 5);

    int i = 0;
    for (char c : "Hello".toCharArray()) {
      char decoded = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(new byte[] {subsequence.get(i++)}))
        .get();

      assertEquals(c, decoded);
    }
  }
}