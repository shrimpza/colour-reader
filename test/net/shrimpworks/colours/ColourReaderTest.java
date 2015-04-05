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

	private BufferedImage halfHalfImage(int w, int h, Color color1, Color color2) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		graphics.setColor(color1);
		graphics.fillRect(0, 0, w / 2, h);

		graphics.setColor(color2);
		graphics.fillRect(w / 2, 0, w, h);

		return image;
	}

	@Test
	public void testBadInput() {
		BufferedImage black = solidImage(20, 20, Color.BLACK);

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

	@Test
	public void moreAverageTest() {
		BufferedImage grey = halfHalfImage(20, 20, Color.BLACK, Color.WHITE);
		HSBColour hsb = ColourReader.averageColour(grey, 1f);
		assertNotNull(hsb);
		assertEquals(0.5, hsb.brightness(), 0.1);

		BufferedImage yellow = halfHalfImage(20, 20, Color.RED, Color.GREEN);
		hsb = ColourReader.averageColour(yellow, 0.5f);
		assertNotNull(hsb);
		assertEquals(60, hsb.hue() * 360, 10);

		BufferedImage pink = halfHalfImage(20, 20, Color.RED, Color.BLUE);
		hsb = ColourReader.averageColour(pink, 0.5f);
		assertNotNull(hsb);
		assertEquals(300, hsb.hue() * 360, 10);
	}
}
