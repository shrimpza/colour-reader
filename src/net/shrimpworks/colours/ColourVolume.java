package net.shrimpworks.colours;

import java.util.Objects;

/**
 * A simple object used to hold an {@link HSBColour} and the percentage
 * volume it occupies within an image.
 */
public class ColourVolume implements Comparable<ColourVolume> {

	private final HSBColour colour;
	private final float volume;

	public ColourVolume(HSBColour colour, float volume) {
		this.colour = colour;
		this.volume = volume;
	}

	public HSBColour colour() {
		return colour;
	}

	public float volume() {
		return volume;
	}

	@Override
	public int compareTo(ColourVolume colourVolume) {
		// simple sorting by volume in reverse order (largest to smallest)
		return colourVolume == null ? -1 : Float.compare(volume, colourVolume.volume) * -1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ColourVolume that = (ColourVolume)o;
		return Math.abs(volume - that.volume) < 0.00001 &&
			   Objects.equals(colour, that.colour);
	}

	@Override
	public int hashCode() {
		return Objects.hash(colour, volume);
	}

	@Override
	public String toString() {
		return String.format("ColourVolume [colour=%s, volume=%.4f]", colour, volume);
	}
}
