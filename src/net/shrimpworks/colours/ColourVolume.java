package net.shrimpworks.colours;

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
		return colourVolume == null ? -1 : Float.compare(volume, colourVolume.volume) * -1;
	}

	@Override
	public String toString() {
		return String.format("ColourVolume [colour=%s, volume=%.4f]", colour, volume);
	}
}
