package net.shrimpworks.colours;

public class HSBColour {

	private final float hue;
	private final float saturation;
	private final float brightness;

	public HSBColour(float hue, float saturation, float brightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

	public HSBColour(float[] hsb) {
		this(hsb[0], hsb[1], hsb[2]);
	}

	public float hue() {
		return hue;
	}

	public float saturation() {
		return saturation;
	}

	public float brightness() {
		return brightness;
	}

	public float[] hsb() {
		return new float[] { hue, saturation, brightness };
	}

	@Override
	public String toString() {
		return String.format("HSBColour [hue=%f, saturation=%f, brightness=%f]",
							 hue, saturation, brightness);
	}
}
