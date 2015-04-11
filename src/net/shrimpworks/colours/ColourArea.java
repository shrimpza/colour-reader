package net.shrimpworks.colours;

import java.util.Objects;

/**
 * A simple object used to hold an {@link HSBColour} and the percentage
 * area it occupies within an image.
 */
public class ColourArea implements Comparable<ColourArea> {

	private static final float EQUALS_FLOAT_VARIANCE = 0.00001f;

	private final HSBColour colour;
	private final float area;

	public ColourArea(HSBColour colour, float area) {
		this.colour = colour;
		this.area = area;
	}

	public HSBColour colour() {
		return colour;
	}

	public float volume() {
		return area;
	}

	@Override
	public int compareTo(ColourArea colourArea) {
		// simple sorting by area in reverse order (largest to smallest)
		return colourArea == null ? -1 : Float.compare(area, colourArea.area) * -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ColourArea that = (ColourArea)o;
		return Math.abs(area - that.area) < EQUALS_FLOAT_VARIANCE
			   && Objects.equals(colour, that.colour);
	}

	@Override
	public int hashCode() {
		return Objects.hash(colour, area);
	}

	@Override
	public String toString() {
		return String.format("ColourArea [colour=%s, area=%.4f]", colour, area);
	}
}
