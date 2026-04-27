import java.awt.*;
import java.awt.image.BufferedImage;

class ImageOperations {

    /**
     * Removes the red channel from the image, keeping only green and blue.
     * @param img the source image.
     * @return a new image with the red channel set to zero.
     */
    static BufferedImage zeroRed(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int g = ColorOperations.getGreen(rgb);
                int b = ColorOperations.getBlue(rgb);
                newImg.setRGB(x, y, new Color(0, g, b).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Converts the image to grayscale using the luminance formula.
     * @param img the source image.
     * @return a new grayscale image.
     */
    static BufferedImage grayscale(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int r = ColorOperations.getRed(rgb);
                int g = ColorOperations.getGreen(rgb);
                int b = ColorOperations.getBlue(rgb);
                int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                newImg.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Inverts all color channels of the image.
     * @param img the source image.
     * @return a new image with inverted colors.
     */
    static BufferedImage invert(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int r = 255 - ColorOperations.getRed(rgb);
                int g = 255 - ColorOperations.getGreen(rgb);
                int b = 255 - ColorOperations.getBlue(rgb);
                newImg.setRGB(x, y, new Color(r, g, b).getRGB());
            }
        }
        return newImg;
    }

    /**
     * Mirrors the image in the specified direction.
     * @param img the source image.
     * @param dir the direction to mirror, either horizontal or vertical.
     * @return a new mirrored image.
     */
    static BufferedImage mirror(BufferedImage img, MirrorMenuItem.MirrorDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        if (dir == MirrorMenuItem.MirrorDirection.VERTICAL) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    newImg.setRGB(x, height - 1 - y, img.getRGB(x, y));
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    newImg.setRGB(width - 1 - x, y, img.getRGB(x, y));
                }
            }
        }
        return newImg;
    }

    /**
     * Repeats the image a specified number of times in the given direction.
     * @param img the source image.
     * @param n the number of times to repeat the image.
     * @param dir the direction to repeat, either horizontal or vertical.
     * @return a new image with the source tiled n times.
     */
    static BufferedImage repeat(BufferedImage img, int n, RepeatMenuItem.RepeatDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = null;
        if (dir == RepeatMenuItem.RepeatDirection.HORIZONTAL) {
            newImg = new BufferedImage(width * n, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < n; i++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        newImg.setRGB(x + i * width, y, img.getRGB(x, y));
                    }
                }
            }
        } else {
            newImg = new BufferedImage(width, height * n, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < n; i++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        newImg.setRGB(x, y + i * height, img.getRGB(x, y));
                    }
                }
            }
        }
        return newImg;
    }

    /**
     * Rotates the image 90 degrees in the specified direction.
     * @param img the source image.
     * @param dir the direction to rotate, either clockwise or counter-clockwise.
     * @return a new rotated image.
     */
    static BufferedImage rotate(BufferedImage img, RotateMenuItem.RotateDirection dir) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImg = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        if (dir == RotateMenuItem.RotateDirection.CLOCKWISE) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    newImg.setRGB(height - 1 - y, x, img.getRGB(x, y));
                }
            }
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    newImg.setRGB(y, width - 1 - x, img.getRGB(x, y));
                }
            }
        }
        return newImg;
    }

    /**
     * Zooms in on the image. The zoom factor increases in multiplicatives of 10% and
     * decreases in multiplicatives of 10%.
     * @param img the original image to zoom in on.
     * The image cannot be already zoomed in or out because then the image will be distorted.
     * @param zoomFactor The factor to zoom in by.
     * @return the zoomed in image.
     */
    static BufferedImage zoom(BufferedImage img, double zoomFactor) {
        int newImageWidth = (int) (img.getWidth() * zoomFactor);
        int newImageHeight = (int) (img.getHeight() * zoomFactor);
        BufferedImage newImg = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(img, 0, 0, newImageWidth, newImageHeight, null);
        g2d.dispose();
        return newImg;
    }
}
