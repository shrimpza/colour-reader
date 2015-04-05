package net.shrimpworks.colours;

import java.awt.*;

public class ColourVolume implements Comparable<ColourVolume> {

	private final Color colour;
	private final float volume;

	public ColourVolume(Color colour, float volume) {
		this.colour = colour;
		this.volume = volume;
	}

	public Color colour() {
		return colour;
	}

	public float volume() {
		return volume;
	}

	@Override
	public int compareTo(ColourVolume colourVolume) {
		return colourVolume == null ? -1 : Float.compare(volume, colourVolume.volume) * -1;
	}
}
