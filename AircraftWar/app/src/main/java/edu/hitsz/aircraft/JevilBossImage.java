package edu.hitsz.aircraft;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/** A small, original pixel-art jester sprite drawn at runtime. */
public final class JevilBossImage {
    private static final int WIDTH = 52;
    private static final int HEIGHT = 60;
    private static final Color OUTLINE = Color.rgb(27, 18, 42);
    private static final Color PURPLE = Color.rgb(112, 58, 166);
    private static final Color BLUE = Color.rgb(45, 156, 196);
    private static final Color CLOTH = Color.rgb(238, 232, 247);
    private static final Color FACE = Color.rgb(236, 217, 231);
    private static final Color EYE = Color.rgb(247, 211, 89);

    private JevilBossImage() {
    }

    public static WritableImage createImage() {
        WritableImage image = new WritableImage(WIDTH, HEIGHT);
        PixelWriter pixels = image.getPixelWriter();

        // Paired jester bells and a pointed cap.
        fillRect(pixels, 7, 2, 14, 12, PURPLE);
        fillRect(pixels, 31, 2, 14, 12, BLUE);
        fillRect(pixels, 4, 0, 7, 6, EYE);
        fillRect(pixels, 41, 0, 7, 6, EYE);
        fillRect(pixels, 13, 10, 26, 8, OUTLINE);

        // Head and face.
        fillRect(pixels, 10, 15, 32, 23, OUTLINE);
        fillRect(pixels, 13, 17, 26, 19, FACE);
        fillRect(pixels, 17, 21, 5, 7, OUTLINE);
        fillRect(pixels, 30, 21, 5, 7, OUTLINE);
        fillRect(pixels, 18, 22, 2, 3, EYE);
        fillRect(pixels, 31, 22, 2, 3, EYE);
        fillRect(pixels, 23, 30, 7, 2, PURPLE);

        // Collar, diamond buttons, and a split purple-blue coat.
        fillRect(pixels, 8, 37, 36, 5, CLOTH);
        fillRect(pixels, 13, 41, 26, 16, OUTLINE);
        fillRect(pixels, 16, 43, 9, 12, PURPLE);
        fillRect(pixels, 27, 43, 9, 12, BLUE);
        fillRect(pixels, 23, 43, 6, 6, EYE);
        fillRect(pixels, 23, 50, 6, 6, CLOTH);
        fillRect(pixels, 3, 41, 10, 5, OUTLINE);
        fillRect(pixels, 39, 41, 10, 5, OUTLINE);

        return image;
    }

    private static void fillRect(PixelWriter pixels, int x, int y, int width, int height, Color color) {
        for (int row = y; row < y + height; row++) {
            for (int column = x; column < x + width; column++) {
                pixels.setColor(column, row, color);
            }
        }
    }
}
