package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColourReader {

	private static final Hue[] HUES = new Hue[] { Hue.RED, Hue.YELLOW, Hue.GREEN, Hue.CYAN, Hue.BLUE, Hue.MAGENTA };

	private final List<Hue> hues;
	private final float resolution;
	private final float blackThreshold;
	private final float whiteThreshold;

	public ColourReader() {
		this(Arrays.asList(HUES), 0.8f, 0.0f, 0.0f);
	}

	private ColourReader(List<Hue> hues, float resolution, float blackThreshold, float whiteThreshold) {
		if (hues.isEmpty()) throw new IllegalArgumentException("Empty hues collection not allowed");
		if (resolution < 0.0) throw new IllegalArgumentException("Resolution value may not be lower than 0.0");
		if (resolution > 1.0) throw new IllegalArgumentException("Resolution value may not exceed 1.0");

		this.hues = hues;
		this.resolution = resolution;
		this.blackThreshold = blackThreshold;
		this.whiteThreshold = whiteThreshold;
	}

	public ColourReader withHues(List<Hue> hues) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	public ColourReader withResolution(float resolution) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	public ColourReader withBlackThreshold(float blackThreshold) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	public ColourReader withWhiteThreshold(float whiteThreshold) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	public HSBColour averageColour(BufferedImage image) {
		final List<Integer> samples = readImage(image, resolution);

		final int[] totalRGB = new int[] { 0, 0, 0 };

		samples.stream().forEach(rgb -> {
			totalRGB[0] += (rgb >> 16) & 0xFF;
			totalRGB[1] += (rgb >> 8) & 0xFF;
			totalRGB[2] += (rgb) & 0xFF;
		});

		return new HSBColour(Color.RGBtoHSB(totalRGB[0] / samples.size(),
											totalRGB[1] / samples.size(),
											totalRGB[2] / samples.size(), null));
	}

	public List<ColourVolume> colourVolumes(BufferedImage image) {
		final List<Integer> samples = readImage(image, resolution);

		final Map<Color, List<HSBColour>> colorList = new HashMap<>();

		samples.stream().forEach(rgb -> {
			HSBColour hsb = new HSBColour(Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF, null));
			Color color = null;

			// handle black/white/grey separately
			if (hsb.saturation() <= whiteThreshold && hsb.brightness() >= (1f - whiteThreshold)) {
				color = Color.WHITE;
			} else if (hsb.hue() <= blackThreshold && hsb.saturation() <= whiteThreshold
					   && hsb.brightness() < (1f - whiteThreshold) && hsb.brightness() > blackThreshold) {
				color = Color.GRAY;
			} else if (hsb.brightness() <= blackThreshold) {
				color = Color.BLACK;
			} else {
				for (Hue hue : hues) if (hue.matches(hsb)) color = hue.color();
			}

			// null check, since it's possible a hue set was provided which does not cover something
			if (color != null) {
				if (!colorList.containsKey(color)) colorList.put(color, new ArrayList<>());
				colorList.get(color).add(hsb);
			}
		});

		List<ColourVolume> colours = new ArrayList<>();

		for (Map.Entry<Color, List<HSBColour>> e : colorList.entrySet()) {
			float[] hsb = new float[3];
			for (HSBColour c : e.getValue()) {
				hsb[0] += c.hue();
				hsb[1] += c.saturation();
				hsb[2] += c.brightness();
			}
			colours.add(new ColourVolume(new HSBColour(hsb[0] / e.getValue().size(),
													   hsb[1] / e.getValue().size(),
													   hsb[2] / e.getValue().size()),
										 ((float)e.getValue().size() / (float)samples.size())));
		}

		Collections.sort(colours);

		return Collections.unmodifiableList(colours);
	}

	private static List<Integer> readImage(BufferedImage image, float resolution) {
		final int xStep = image.getWidth() / (int)(image.getWidth() * resolution);
		final int yStep = image.getHeight() / (int)(image.getHeight() * resolution);

		final List<Integer> result = new ArrayList<>();
		for (int x = 0; x < image.getWidth(); x += xStep) {
			for (int y = 0; y < image.getHeight(); y += yStep) {
				result.add(image.getRGB(x, y));
			}
		}

		return result;
	}
}
