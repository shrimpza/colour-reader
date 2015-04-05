package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ColourReaderTest {

	private BufferedImage solidImage(int w, int h, Color color) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setBackground(color);
		return image;
	}

	@Test
	public void averageTest() {
		BufferedImage black = solidImage(20, 20, Color.BLACK);

		HSBColour hsb = ColourReader.averageColour(black, 0.5f);
		assertNotNull(hsb);
		assertEquals(0, hsb.brightness(), 0.0);
	}
}
