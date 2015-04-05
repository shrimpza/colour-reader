package net.shrimpworks.colours;

public class Hue {

	public static final Hue RED = new Hue(new int[][] { { 0, 30 }, { 330, 360 } });
	public static final Hue YELLOW = new Hue(30, 90);
	public static final Hue GREEN = new Hue(90, 150);
	public static final Hue CYAN = new Hue(150, 210);
	public static final Hue BLUE = new Hue(210, 270);
	public static final Hue MAGENTA = new Hue(270, 330);

	private final float ranges[][];

	public Hue(float min, float max) {
		this(new float[][] { { min, max } });
	}

	public Hue(int min, int max) {
		this(min / 360f, max / 360f);
	}

	public Hue(float[][] ranges) {
		this.ranges = ranges;
	}

	public Hue(int[][] ranges) {
		final float[][] floatRange = new float[ranges.length][];
		for (int i = 0; i < ranges.length; i++) {
			floatRange[i] = new float[] { ranges[i][0] / 360, ranges[i][0] / 360 };
		}

		this.ranges = floatRange;
	}

	public boolean matches(HSBColour colour) {
		for (float[] r : ranges) {
			if (colour.hue() >= r[0] && colour.hue() <= colour.hue()) return true;
		}
		return false;
	}
}
