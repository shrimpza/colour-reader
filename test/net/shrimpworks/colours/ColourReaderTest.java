package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColourReaderTest {

	@Test
	public void testBadInput() {
		BufferedImage black = ImageUtils.solidImage(20, 20, Color.BLACK);

		// verify that invalid resolutions are handled as expected

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
		// verify a solid black image has an average colour of black
		BufferedImage black = ImageUtils.solidImage(20, 20, Color.BLACK);
		HSBColour hsb = ColourReader.averageColour(black, 0.5f);
		assertNotNull(hsb);
		assertEquals(0, hsb.brightness(), 0.0);

		// verify a solid green image has an average colour of green
		BufferedImage green = ImageUtils.solidImage(20, 20, Color.GREEN);
		hsb = ColourReader.averageColour(green, 0.75f);
		assertNotNull(hsb);
		assertEquals(120, hsb.hue() * 360, 0.0);

		// verify a solid white image has an average colour of white
		BufferedImage white = ImageUtils.solidImage(50, 50, Color.WHITE);
		hsb = ColourReader.averageColour(white, 0.1f);
		assertNotNull(hsb);
		assertEquals(1, hsb.brightness(), 0.0);
		assertEquals(0, hsb.saturation(), 0.0);

		// verify a solid blue image has an average colour of blue
		BufferedImage blue = ImageUtils.solidImage(20, 20, Color.BLUE);
		hsb = ColourReader.averageColour(blue, 0.75f);
		assertNotNull(hsb);
		assertEquals(240, hsb.hue() * 360, 0.0);
		assertEquals(1, hsb.brightness(), 0.0);
		assertEquals(1, hsb.saturation(), 0.0);
	}

	@Test
	public void moreAverageTest() {
		// ger average colour of a 50/50 black/white image, grey result expected
		BufferedImage grey = ImageUtils.halfHalfImage(20, 20, Color.BLACK, Color.WHITE);
		HSBColour hsb = ColourReader.averageColour(grey, 1f);
		assertNotNull(hsb);
		assertEquals(0.5, hsb.brightness(), 0.1);

		// ger average colour of a 50/50 red/green image, yellow result expected
		BufferedImage yellow = ImageUtils.halfHalfImage(20, 20, Color.RED, Color.GREEN);
		hsb = ColourReader.averageColour(yellow, 0.5f);
		assertNotNull(hsb);
		assertEquals(60, hsb.hue() * 360, 10);

		// ger average colour of a 50/50 red/blue image, magenta result expected
		BufferedImage pink = ImageUtils.halfHalfImage(20, 20, Color.RED, Color.BLUE);
		hsb = ColourReader.averageColour(pink, 0.5f);
		assertNotNull(hsb);
		assertEquals(300, hsb.hue() * 360, 10);
	}

	@Test
	public void colourVolumeTest() {
		List<ColourVolume> colours;

		// verify getting colours from a solid grey image - 100% grey expected
		BufferedImage grey = ImageUtils.solidImage(20, 20, Color.GRAY);
		colours = ColourReader.colourVolumes(grey, 1f);
		assertNotNull(colours);
		assertEquals(1, colours.size());
		assertArrayEquals(getHSB(Color.GRAY), colours.stream().findFirst().get().colour().hsb(), 0.001f);

		// verify getting colour volumes from a solid red image - 100% full red expected
		BufferedImage red = ImageUtils.solidImage(20, 20, Color.RED);
		colours = ColourReader.colourVolumes(red, 1f);
		assertNotNull(colours);
		assertEquals(1, colours.size());
		assertArrayEquals(getHSB(Color.RED), colours.stream().findFirst().get().colour().hsb(), 0.001f);

		// get colour volumes from a black and white image - 50/50 split expected
		List<Color> expected = Arrays.asList(Color.BLACK, Color.WHITE);
		BufferedImage blackAndWhite = ImageUtils.halfHalfImage(20, 20, Color.BLACK, Color.WHITE);
		colours = ColourReader.colourVolumes(blackAndWhite, 1f);
		assertNotNull(colours);
		assertEquals(2, colours.size());
		assertTrue(expected.containsAll(colours.stream().map(ColourVolume::colour).collect(Collectors.toList())));
		assertEquals(2, colours.stream().filter(c -> c.volume() == 0.5f).count());

		// get colour volumes from a 4-solid-colours image, 4-way 25% split of solid colours expected
		BufferedImage fourCols = ImageUtils.quartersImage(20, 20, Color.BLACK, Color.WHITE, Color.RED, Color.GREEN);
		colours = ColourReader.colourVolumes(fourCols, 1f);
		assertNotNull(colours);
		assertEquals(4, colours.size());
		expected = Arrays.asList(Color.BLACK, Color.WHITE, Color.RED, Color.GREEN);
		assertTrue(expected.containsAll(colours.stream().map(ColourVolume::colour).collect(Collectors.toList())));
		assertEquals(4, colours.stream().filter(c -> c.volume() == 0.25f).count());

		// get colour volumes from a 3-colour image where one colour is more prevalent, 50/25/25 split of solid colours expected
		BufferedImage threeCols = ImageUtils.quartersImage(20, 20, Color.BLUE, Color.BLUE, Color.RED, Color.GREEN);
		colours = ColourReader.colourVolumes(threeCols, 1f);
		assertNotNull(colours);
		assertEquals(3, colours.size());
		expected = Arrays.asList(Color.BLUE, Color.RED, Color.GREEN);
		assertTrue(expected.containsAll(colours.stream().map(ColourVolume::colour).collect(Collectors.toList())));
		assertEquals(2, colours.stream().filter(c -> c.volume() == 0.25f).count());
		assertEquals(1, colours.stream().filter(c -> c.volume() == 0.5f).count());
	}

	/**
	 * Helper method to get HSB values from a Color instance
	 *
	 * @param color colour to get HSB values from
	 * @return array of HSB values
	 */
	public float[] getHSB(Color color) {
		int rgb = color.getRGB();
		return Color.RGBtoHSB((rgb >> 16) & 0xFF,
							  (rgb >> 8) & 0xFF,
							  (rgb) & 0xFF, null);
	}
}
