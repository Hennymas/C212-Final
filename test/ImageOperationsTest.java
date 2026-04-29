import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class ImageOperationsTest {

    /**
     * Creates a small test image with known pixel colors.
     * @param width  the width of the test image.
     * @param height the height of the test image.
     * @return a BufferedImage with predictable pixel values.
     */
    private BufferedImage createTestImage(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (x * 50) % 256;
                int g = (y * 80) % 256;
                int b = ((x + y) * 30) % 256;
                img.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        return img;
    }

    @Test
    void testZeroRed() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.zeroRed(img);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int rgb = result.getRGB(x, y);
                assertEquals(0, ColorOperations.getRed(rgb));
                assertEquals(ColorOperations.getGreen(img.getRGB(x, y)), ColorOperations.getGreen(rgb));
                assertEquals(ColorOperations.getBlue(img.getRGB(x, y)), ColorOperations.getBlue(rgb));
            }
        }
    }

    @Test
    void testGrayscale() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.grayscale(img);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int origRgb = img.getRGB(x, y);
                int r = ColorOperations.getRed(origRgb);
                int g = ColorOperations.getGreen(origRgb);
                int b = ColorOperations.getBlue(origRgb);
                int expectedGray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                int resultRgb = result.getRGB(x, y);
                assertEquals(expectedGray, ColorOperations.getRed(resultRgb));
                assertEquals(expectedGray, ColorOperations.getGreen(resultRgb));
                assertEquals(expectedGray, ColorOperations.getBlue(resultRgb));
            }
        }
    }

    @Test
    void testInvert() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.invert(img);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int origRgb = img.getRGB(x, y);
                int resultRgb = result.getRGB(x, y);
                assertEquals(255 - ColorOperations.getRed(origRgb), ColorOperations.getRed(resultRgb));
                assertEquals(255 - ColorOperations.getGreen(origRgb), ColorOperations.getGreen(resultRgb));
                assertEquals(255 - ColorOperations.getBlue(origRgb), ColorOperations.getBlue(resultRgb));
            }
        }
    }

    @Test
    void testDoubleInvertRestoresOriginal() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.invert(ImageOperations.invert(img));
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                assertEquals(img.getRGB(x, y), result.getRGB(x, y));
            }
        }
    }

    @Test
    void testMirrorHorizontal() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.mirror(img, MirrorMenuItem.MirrorDirection.HORIZONTAL);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                assertEquals(img.getRGB(x, y), result.getRGB(img.getWidth() - 1 - x, y));
            }
        }
    }

    @Test
    void testMirrorVertical() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.mirror(img, MirrorMenuItem.MirrorDirection.VERTICAL);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                assertEquals(img.getRGB(x, y), result.getRGB(x, img.getHeight() - 1 - y));
            }
        }
    }

    @Test
    void testRotateClockwise() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.rotate(img, RotateMenuItem.RotateDirection.CLOCKWISE);
        assertEquals(img.getHeight(), result.getWidth());
        assertEquals(img.getWidth(), result.getHeight());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                assertEquals(img.getRGB(x, y), result.getRGB(img.getHeight() - 1 - y, x));
            }
        }
    }

    @Test
    void testRotateCounterClockwise() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = ImageOperations.rotate(img, RotateMenuItem.RotateDirection.COUNTER_CLOCKWISE);
        assertEquals(img.getHeight(), result.getWidth());
        assertEquals(img.getWidth(), result.getHeight());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                assertEquals(img.getRGB(x, y), result.getRGB(y, img.getWidth() - 1 - x));
            }
        }
    }

    @Test
    void testFourClockwiseRotationsRestoreOriginal() {
        BufferedImage img = createTestImage(4, 3);
        BufferedImage result = img;
        for (int i = 0; i < 4; i++) {
            result = ImageOperations.rotate(result, RotateMenuItem.RotateDirection.CLOCKWISE);
        }
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                assertEquals(img.getRGB(x, y), result.getRGB(x, y));
            }
        }
    }

    @Test
    void testRepeatHorizontal() {
        BufferedImage img = createTestImage(4, 3);
        int n = 3;
        BufferedImage result = ImageOperations.repeat(img, n, RepeatMenuItem.RepeatDirection.HORIZONTAL);
        assertEquals(img.getWidth() * n, result.getWidth());
        assertEquals(img.getHeight(), result.getHeight());
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    assertEquals(img.getRGB(x, y), result.getRGB(x + i * img.getWidth(), y));
                }
            }
        }
    }

    @Test
    void testRepeatVertical() {
        BufferedImage img = createTestImage(4, 3);
        int n = 2;
        BufferedImage result = ImageOperations.repeat(img, n, RepeatMenuItem.RepeatDirection.VERTICAL);
        assertEquals(img.getWidth(), result.getWidth());
        assertEquals(img.getHeight() * n, result.getHeight());
        for (int i = 0; i < n; i++) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    assertEquals(img.getRGB(x, y), result.getRGB(x, y + i * img.getHeight()));
                }
            }
        }
    }
}
