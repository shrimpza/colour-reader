package net.shrimpworks.colours;

import java.awt.*;

public class Hue {

	public static final Hue RED = new Hue(new int[][] { { 0, 30 }, { 330, 360 } }, new Color[] { Color.RED, Color.RED });
	public static final Hue YELLOW = new Hue(30, 90, Color.YELLOW);
	public static final Hue GREEN = new Hue(90, 150, Color.GREEN);
	public static final Hue CYAN = new Hue(150, 210, Color.CYAN);
	public static final Hue BLUE = new Hue(210, 270, Color.BLUE);
	public static final Hue MAGENTA = new Hue(270, 330, Color.MAGENTA);

	private final float[][] ranges;
	private final Color[] colors;

	public Hue(float min, float max, Color color) {
		this(new float[][] { { min, max } }, new Color[] { color });
	}

	public Hue(int min, int max, Color color) {
		this(min / 360f, max / 360f, color);
	}

	public Hue(float[][] ranges, Color[] colors) {
		this.ranges = ranges;
		this.colors = colors;
	}

	public Hue(int[][] ranges, Color[] colors) {
		final float[][] floatRange = new float[ranges.length][];
		for (int i = 0; i < ranges.length; i++) {
			floatRange[i] = new float[] { ranges[i][0] / 360, ranges[i][0] / 360 };
		}

		this.ranges = floatRange;
		this.colors = colors;
	}

	public float[][] ranges() {
		return ranges;
	}

	public Color[] colors() {
		return colors;
	}

	public boolean matches(HSBColour colour) {
		for (float[] r : ranges) {
			if (colour.hue() >= r[0] && colour.hue() <= colour.hue()) return true;
		}
		return false;
	}
}
