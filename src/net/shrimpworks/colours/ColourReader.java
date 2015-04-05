package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColourReader {

	public static HSBColour averageColour(BufferedImage image, float resolution) {

		if (resolution < 0.5) throw new IllegalArgumentException("Resolution value may not be lower than 0.5");
		if (resolution > 1.0) throw new IllegalArgumentException("Resolution value may not exceed 1.0");

		// TODO:
		// given scan resolution (0.0f to 1.0f), get rgb for each pixel
		// accumulate rgb values, divide by number of readings (average colour)
		// return HSBColour instance of averaged values

		int xStep = (int)(image.getWidth() * resolution);
		int yStep = (int)(image.getHeight() * resolution);

		int samples = 0;
		int[] totalRGB = new int[] { 0, 0, 0 };
		for (int x = 0; x < image.getWidth(); x += xStep) {
			for (int y = 0; y < image.getHeight(); y += yStep) {
				int rgb = image.getRGB(x, y);
				totalRGB[0] += (rgb >> 16) & 0xFF;
				totalRGB[1] += (rgb >> 8) & 0xFF;
				totalRGB[2] += (rgb) & 0xFF;
				samples++;
			}
		}

		return new HSBColour(Color.RGBtoHSB(totalRGB[0] / samples, totalRGB[1] / samples, totalRGB[2] / samples, null));
	}
}
