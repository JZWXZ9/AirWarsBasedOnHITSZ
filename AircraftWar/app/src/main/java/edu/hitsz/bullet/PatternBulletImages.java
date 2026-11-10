package edu.hitsz.bullet;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.EnumMap;
import java.util.Map;

/** Small code-drawn icons keep pattern bullets distinct without external assets. */
final class PatternBulletImages {
    private static final Map<PatternBulletStyle, Image> IMAGES = createImages();

    private PatternBulletImages() {
    }

    static Image get(PatternBulletStyle style) {
        return IMAGES.get(style);
    }

    private static Map<PatternBulletStyle, Image> createImages() {
        Map<PatternBulletStyle, Image> images = new EnumMap<>(PatternBulletStyle.class);
        for (PatternBulletStyle style : PatternBulletStyle.values()) {
            images.put(style, createImage(style));
        }
        return images;
    }

    private static Image createImage(PatternBulletStyle style) {
        int size = 13;
        WritableImage image = new WritableImage(size, size);
        PixelWriter pixels = image.getPixelWriter();
        Color edge = edgeColor(style);
        Color core = coreColor(style);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (contains(style, x, y)) {
                    boolean border = !contains(style, x - 1, y) || !contains(style, x + 1, y)
                            || !contains(style, x, y - 1) || !contains(style, x, y + 1);
                    pixels.setColor(x, y, border ? edge : core);
                } else {
                    pixels.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }
        return image;
    }

    private static boolean contains(PatternBulletStyle style, int x, int y) {
        int dx = Math.abs(x - 6);
        int dy = Math.abs(y - 6);
        return switch (style) {
            case DIAMOND -> dx + dy <= 5;
            case SPADE -> (y <= 7 && dx + Math.max(0, y - 3) <= 5) || (y >= 7 && y <= 11 && dx <= 1);
            case ORB -> dx * dx + dy * dy <= 25;
            case CAROUSEL -> (dx <= 2 && dy <= 5) || (dy <= 2 && dx <= 5);
            case SCYTHE -> (dx <= 1 && y >= 2 && y <= 11) || (y <= 4 && dx <= 5 - y);
        };
    }

    private static Color edgeColor(PatternBulletStyle style) {
        return switch (style) {
            case DIAMOND -> Color.rgb(73, 210, 255);
            case SPADE -> Color.rgb(95, 51, 158);
            case ORB -> Color.rgb(255, 225, 112);
            case CAROUSEL -> Color.rgb(203, 126, 39);
            case SCYTHE -> Color.rgb(190, 48, 63);
        };
    }

    private static Color coreColor(PatternBulletStyle style) {
        return switch (style) {
            case DIAMOND -> Color.rgb(200, 250, 255);
            case SPADE -> Color.rgb(190, 151, 255);
            case ORB -> Color.rgb(255, 248, 206);
            case CAROUSEL -> Color.rgb(255, 216, 132);
            case SCYTHE -> Color.rgb(255, 145, 154);
        };
    }
}
