package com.github.ynverxe.binarysections;

import com.github.ynverxe.binarysections.io.source.ByteArrayByteSource;
import com.github.ynverxe.binarysections.io.source.ByteSource;
import org.junit.jupiter.api.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BinarySectionStackTest {

  private static final int INITIAL_BUFFER_SIZE = 1000;
  private static final int PAYLOAD_LENGTH = 100;

  private final ByteArrayByteSource source;
  private final BinarySectionStack sectionStack;

  public BinarySectionStackTest() {
    this.source = ByteSource.ofBytes(INITIAL_BUFFER_SIZE);
    this.sectionStack = BinarySectionStack.create(source, PAYLOAD_LENGTH);
  }

  @Test
  @Order(1)
  public void testNoDataLoss() {
    Charset charset = StandardCharsets.UTF_8;

    sectionStack.add("Hello".getBytes(charset));
    sectionStack.add("World".getBytes(charset));

    assertEquals("Hello", new String(sectionStack.get(0).payloadAsArray(), charset));
    assertEquals("World", new String(sectionStack.get(1).payloadAsArray(), charset));

    sectionStack.set(0, "Byeee".getBytes(charset));
    assertEquals("Byeee", new String(sectionStack.get(0).payloadAsArray(), charset));

    BinarySectionStackView.Header header = sectionStack.header();

    assertEquals(header.payloadLength(), PAYLOAD_LENGTH);
    assertEquals(header.writtenSections(), 2);
  }

  @Test
  @Order(2)
  public void testSectionRemoval() {
    sectionStack.remove(0);

    assertEquals("World", new String(sectionStack.get(0).payloadAsArray(), StandardCharsets.UTF_8));
    assertEquals(INITIAL_BUFFER_SIZE - sectionStack.header().sectionLength(), source.array().length);
  }

  @Test
  @Order(3)
  public void testClear() {
    int bufferSize = source.array().length;
    int discardedBytes = sectionStack.header().writtenSections() * sectionStack.header().sectionLength();

    sectionStack.clear();

    assertEquals(0, sectionStack.header().writtenSections());
    assertEquals(bufferSize - discardedBytes, source.array().length);
  }

  @Test
  @Order(4)
  public void testMultipleSectionAdd() {
    List<byte[]> encoded = Arrays.asList(
      encode("This"),
      encode("is"),
      encode("a list"),
      encode("of sections")
    );

    sectionStack.addAll(encoded);

    int i = 0;
    for (BinarySection section : sectionStack.all()) {
      assertArrayEquals(encoded.get(i++), section.payloadAsArray());
    }
  }

  private static byte[] encode(String txt) {
    return txt.getBytes(StandardCharsets.UTF_8);
  }
}