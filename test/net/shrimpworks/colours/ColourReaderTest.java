package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColourReaderTest {

	@Test
	public void testBadInput() {
		BufferedImage black = ImageUtils.solidImage(20, 20, Color.BLACK);

		try {
			ColourReader.averageColour(black, -0.1f);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			// expected
		}

		try {
			ColourReader.averageColour(black, 1.5f);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			// expected
		}
	}

	@Test
	public void averageTest() {
		BufferedImage black = ImageUtils.solidImage(20, 20, Color.BLACK);
		HSBColour hsb = ColourReader.averageColour(black, 0.5f);
		assertNotNull(hsb);
		assertEquals(0, hsb.brightness(), 0.0);

		BufferedImage green = ImageUtils.solidImage(20, 20, Color.GREEN);
		hsb = ColourReader.averageColour(green, 0.75f);
		assertNotNull(hsb);
		assertEquals(120, hsb.hue() * 360, 0.0);

		BufferedImage white = ImageUtils.solidImage(50, 50, Color.WHITE);
		hsb = ColourReader.averageColour(white, 0.1f);
		assertNotNull(hsb);
		assertEquals(1, hsb.brightness(), 0.0);
		assertEquals(0, hsb.saturation(), 0.0);

		BufferedImage blue = ImageUtils.solidImage(20, 20, Color.BLUE);
		hsb = ColourReader.averageColour(blue, 0.75f);
		assertNotNull(hsb);
		assertEquals(240, hsb.hue() * 360, 0.0);
		assertEquals(1, hsb.brightness(), 0.0);
		assertEquals(1, hsb.saturation(), 0.0);
	}

	@Test
	public void moreAverageTest() {
		BufferedImage grey = ImageUtils.halfHalfImage(20, 20, Color.BLACK, Color.WHITE);
		HSBColour hsb = ColourReader.averageColour(grey, 1f);
		assertNotNull(hsb);
		assertEquals(0.5, hsb.brightness(), 0.1);

		BufferedImage yellow = ImageUtils.halfHalfImage(20, 20, Color.RED, Color.GREEN);
		hsb = ColourReader.averageColour(yellow, 0.5f);
		assertNotNull(hsb);
		assertEquals(60, hsb.hue() * 360, 10);

		BufferedImage pink = ImageUtils.halfHalfImage(20, 20, Color.RED, Color.BLUE);
		hsb = ColourReader.averageColour(pink, 0.5f);
		assertNotNull(hsb);
		assertEquals(300, hsb.hue() * 360, 10);
	}
}
