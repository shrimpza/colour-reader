package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColourReaderTest {

	private BufferedImage solidImage(int w, int h, Color color) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(0, 0, w, h);
		return image;
	}

	@Test
	public void testBadInput() {
		BufferedImage black = solidImage(20, 20, Color.BLACK);

		try {
			ColourReader.averageColour(black, 0.4f);
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
		BufferedImage black = solidImage(20, 20, Color.BLACK);
		HSBColour hsb = ColourReader.averageColour(black, 0.5f);
		assertNotNull(hsb);
		assertEquals(0, hsb.brightness(), 0.0);

		BufferedImage green = solidImage(20, 20, Color.GREEN);
		hsb = ColourReader.averageColour(green, 0.75f);
		assertNotNull(hsb);
		assertEquals(120, hsb.hue() * 360, 0.0);

		BufferedImage white = solidImage(50, 50, Color.WHITE);
		hsb = ColourReader.averageColour(white, 0.1f);
		assertNotNull(hsb);
		assertEquals(1, hsb.brightness(), 0.0);
		assertEquals(0, hsb.saturation(), 0.0);

		BufferedImage blue = solidImage(20, 20, Color.BLUE);
		hsb = ColourReader.averageColour(blue, 0.75f);
		assertNotNull(hsb);
		assertEquals(240, hsb.hue() * 360, 0.0);
		assertEquals(1, hsb.brightness(), 0.0);
		assertEquals(1, hsb.saturation(), 0.0);
	}
}
