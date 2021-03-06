package net.shrimpworks.colours;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

	public static BufferedImage solidImage(int w, int h, Color color) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(color);
		graphics.fillRect(0, 0, w, h);
		return image;
	}

	public static BufferedImage halfHalfImage(int w, int h, Color color1, Color color2) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		graphics.setColor(color1);
		graphics.fillRect(0, 0, w / 2, h);

		graphics.setColor(color2);
		graphics.fillRect(w / 2, 0, w, h);

		return image;
	}

	public static BufferedImage quartersImage(int w, int h, Color color1, Color color2, Color color3, Color color4) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();

		graphics.setColor(color1);
		graphics.fillRect(0, 0, w / 2, h / 2);

		graphics.setColor(color2);
		graphics.fillRect(w / 2, 0, w, h / 2);

		graphics.setColor(color3);
		graphics.fillRect(0, h / 2, w / 2, h);

		graphics.setColor(color4);
		graphics.fillRect(w / 2, h / 2, w, h);

		return image;
	}
}
