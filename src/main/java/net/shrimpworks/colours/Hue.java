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

	public static final Hue FINE_RED = new Hue(new int[][] { { 0, 15 }, { 345, 360 } }, Color.RED);
	public static final Hue FINE_ORANGE = new Hue(15, 45, Color.ORANGE);
	public static final Hue FINE_YELLOW = new Hue(45, 75, Color.YELLOW);
	public static final Hue FINE_LIGHT_GREEN = new Hue(75, 105, new Color(128, 255, 0));
	public static final Hue FINE_GREEN = new Hue(105, 135, Color.GREEN);
	public static final Hue FINE_SEA_GREEN = new Hue(135, 165, new Color(0, 255, 128));
	public static final Hue FINE_CYAN = new Hue(165, 195, Color.CYAN);
	public static final Hue FINE_LIGHT_BLUE = new Hue(195, 225, new Color(0, 128, 255));
	public static final Hue FINE_BLUE = new Hue(225, 255, Color.BLUE);
	public static final Hue FINE_PURPLE = new Hue(255, 285, new Color(128, 0, 255));
	public static final Hue FINE_MAGENTA = new Hue(285, 315, Color.MAGENTA);
	public static final Hue FINE_PINK = new Hue(315, 345, new Color(255, 0, 128));

	public static final Hue[] BASE = new Hue[] { Hue.RED, Hue.YELLOW, Hue.GREEN, Hue.CYAN, Hue.BLUE, Hue.MAGENTA };
	public static final Hue[] FINE = new Hue[] {
			Hue.FINE_RED, Hue.FINE_ORANGE, Hue.FINE_YELLOW, Hue.FINE_LIGHT_GREEN, Hue.FINE_GREEN, Hue.FINE_SEA_GREEN,
			Hue.FINE_CYAN, Hue.FINE_LIGHT_BLUE, Hue.FINE_BLUE, Hue.FINE_PURPLE, Hue.FINE_MAGENTA, Hue.FINE_PINK
	};

	private static final float DEGREES_SCALE = 360F;

	private final float[][] ranges;
	private final Color color;

	public Hue(float min, float max, Color color) {
		this(new float[][] { { min, max } }, color);
	}

	public Hue(int min, int max, Color color) {
		this(min / DEGREES_SCALE, max / DEGREES_SCALE, color);
	}

	public Hue(float[][] ranges, Color color) {
		this.ranges = ranges;
		this.color = color;
	}

	public Hue(int[][] ranges, Color color) {
		final float[][] floatRange = new float[ranges.length][];
		for (int i = 0; i < ranges.length; i++) {
			floatRange[i] = new float[] { ranges[i][0] / DEGREES_SCALE, ranges[i][0] / DEGREES_SCALE };
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
