package net.shrimpworks.colours;

import java.awt.*;

/**
 * A simple colour-matching class, which holds ranges of hues for the purpose
 * of being able to determine simple colour ranges.
 * <p>
 * A reference Colour instance is also associated with the hue.
 */
public class Hue {

	public static final Hue RED = new Hue(new int[][] { { 0, 30 }, { 330, 360 } }, Color.RED);
	public static final Hue YELLOW = new Hue(30, 90, Color.YELLOW);
	public static final Hue GREEN = new Hue(90, 150, Color.GREEN);
	public static final Hue CYAN = new Hue(150, 210, Color.CYAN);
	public static final Hue BLUE = new Hue(210, 270, Color.BLUE);
	public static final Hue MAGENTA = new Hue(270, 330, Color.MAGENTA);

	private final float[][] ranges;
	private final Color color;

	public Hue(float min, float max, Color color) {
		this(new float[][] { { min, max } }, color);
	}

	public Hue(int min, int max, Color color) {
		this(min / 360f, max / 360f, color);
	}

	public Hue(float[][] ranges, Color color) {
		this.ranges = ranges;
		this.color = color;
	}

	public Hue(int[][] ranges, Color color) {
		final float[][] floatRange = new float[ranges.length][];
		for (int i = 0; i < ranges.length; i++) {
			floatRange[i] = new float[] { ranges[i][0] / 360, ranges[i][0] / 360 };
		}

		this.ranges = floatRange;
		this.color = color;
	}

	public float[][] ranges() {
		return ranges;
	}

	public Color color() {
		return color;
	}

	/**
	 * Determine whether the provided {@link HSBColour} instance's hue falls
	 * within this Hue instance's range.
	 *
	 * @param colour colour to check for match
	 * @return true if the colour matches
	 */
	public boolean matches(HSBColour colour) {
		for (float[] r : ranges) {
			if (colour.hue() >= r[0] && colour.hue() <= colour.hue()) return true;
		}
		return false;
	}
}
