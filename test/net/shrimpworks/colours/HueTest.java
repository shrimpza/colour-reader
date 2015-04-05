package net.shrimpworks.colours;

import java.awt.*;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HueTest {

	@Test
	public void matchTest() {
		assertTrue(Hue.BLUE.matches(new HSBColour(0.65f, 0f, 0f)));
		assertTrue(new Hue(0.4f, 0.5f).matches(new HSBColour(Color.RGBtoHSB(0, 200, 200, null))));
	}
}
