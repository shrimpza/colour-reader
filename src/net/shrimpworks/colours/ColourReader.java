package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Colour Reader provides image colour composition analysis functionality.
 * <p>
 * Build a Colour Reader with the required parameters, then execute one of the
 * analysis methods, passing it the BufferedImage instance to analyse.
 */
public class ColourReader {


	private final Collection<Hue> hues;
	private final float resolution;
	private final float blackThreshold;
	private final float whiteThreshold;

	/**
	 * Create a new Colour Reader with default parameters.
	 */
	public ColourReader() {
		this(Arrays.asList(Hue.BASE), 0.8f, 0.0f, 0.0f);
	}

	private ColourReader(Collection<Hue> hues, float resolution, float blackThreshold, float whiteThreshold) {
		if (hues.isEmpty()) throw new IllegalArgumentException("Empty hues collection not allowed");
		if (resolution < 0.0) throw new IllegalArgumentException("Resolution value may not be lower than 0.0");
		if (resolution > 1.0) throw new IllegalArgumentException("Resolution value may not exceed 1.0");

		this.hues = hues;
		this.resolution = resolution;
		this.blackThreshold = blackThreshold;
		this.whiteThreshold = whiteThreshold;
	}

	/**
	 * Return a new Colour Reader with properties based on this instance
	 * with a new collection of Hues used for colour volume determination.
	 *
	 * @param hues custom hues collection
	 * @return new Colour Reader instance
	 */
	public ColourReader withHues(Collection<Hue> hues) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	/**
	 * Return a new Colour Reader with properties based on this instance
	 * with a custom-defined scan resolution.
	 * <p>
	 * Scan resolution determines the number of pixels from the source image
	 * which will be sampled. The value is represented as a percentage on a
	 * scale of 0 to 1, so a value of 1 would mean 100% of the source image
	 * pixels will be sampled, and a value of 0.25 would mean 25% of the
	 * source image pixels will be sampled.
	 * <p>
	 * Decreasing the resolution may improve performance when analysing larger
	 * images at the expense of accuracy.
	 *
	 * @param resolution custom scan resolution in range 0.0 to 1.0
	 * @return new Colour Reader instance
	 */
	public ColourReader withResolution(float resolution) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	/**
	 * Return a new Colour Reader with properties based on this instance
	 * with a custom-defined black threshold.
	 * <p>
	 * Black threshold provides a brightness cut-off, below which any colour
	 * will be considered solid black. The value is represented on a scale
	 * of 0 to 1, where 0 would mean only completely black pixels are
	 * considered black, and 1 would consider everything black.
	 * <p>
	 * For example, a value of 0.2 would cause many darker shades, regardless
	 * of colour, to be considered black.
	 *
	 * @param blackThreshold custom blackness threshold in range 0.0 to 1.0
	 * @return new Colour Reader instance
	 */
	public ColourReader withBlackThreshold(float blackThreshold) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	/**
	 * Return a new Colour Reader with properties based on this instance
	 * with a custom-defined white threshold.
	 * <p>
	 * White threshold specifies the lower brightness bound upper saturation
	 * bounds, beyond which any colour is considered solid white. The value
	 * is represented on a scale of 0 to 1, where 0 would mean only
	 * completely white pixels are considered white, and 1 would consider
	 * everything white.
	 * <p>
	 * For example, a value of 0.2 would cause many bright shades, regardless
	 * of colour, to be considered white.
	 *
	 * @param whiteThreshold custom whitness threshold in range 0.0 to 1.0
	 * @return new Colour Reader instance
	 */
	public ColourReader withWhiteThreshold(float whiteThreshold) {
		return new ColourReader(hues, resolution, blackThreshold, whiteThreshold);
	}

	/**
	 * Determine a single average colour of an image, across the entire image.
	 *
	 * @param image image to analyse
	 * @return average colour of the image
	 */
	public HSBColour averageColour(BufferedImage image) {
		final List<Integer> samples = readImage(image, resolution);

		/*
		   loop through each sample, building up cumulative r/g/b totals then
		   divide the cumulative totals by number of samples to get an average
		   colour.
		*/

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

	/**
	 * Determine the colour composition of an image.
	 * <p>
	 * A sorted collection of Colour Volumes are returned, each containing
	 * an averaged colour and the percentage (on scale of 0 to 1) of the
	 * image that colour occupies.
	 * <p>
	 * White and black thresholds are used to "trim" certain tones, and
	 * a collection of hues (see {@link #withHues(Collection)}
	 * are used to determine colour groupings.
	 *
	 * @param image image to analyse
	 * @return list of colours in image, ordered by their usage volume
	 */
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
