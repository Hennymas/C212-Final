import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ImageEditorPpmTest {

    /**
     * Creates a small PPM file for testing.
     *
     * @param file   the file to write to.
     * @param width  the image width.
     * @param height the image height.
     * @throws IOException if the file cannot be written.
     */
    private void createTestPpm(File file, int width, int height) throws IOException {
        PrintWriter writer = new PrintWriter(file);
        writer.println("P3");
        writer.println(width + " " + height);
        writer.println("255");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = (x * 60) % 256;
                int g = (y * 70) % 256;
                int b = ((x + y) * 40) % 256;
                writer.println(r + " " + g + " " + b);
            }
        }
        writer.close();
    }

    @Test
    void testReadAndWritePpm(@TempDir File tempDir) throws IOException {
        File inputFile = new File(tempDir, "test_input.ppm");
        createTestPpm(inputFile, 5, 4);

        ImageEditor editor = new ImageEditor();
        editor.readPpmImage(inputFile.getAbsolutePath());
        BufferedImage img = editor.getImage();
        assertNotNull(img);
        assertEquals(5, img.getWidth());
        assertEquals(4, img.getHeight());

        File outputFile = new File(tempDir, "test_output.ppm");
        editor.writePpmImage(outputFile.getAbsolutePath());

        Scanner scanner = new Scanner(outputFile);
        assertEquals("P3", scanner.next());
        assertEquals(5, scanner.nextInt());
        assertEquals(4, scanner.nextInt());
        assertEquals(255, scanner.nextInt());

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 5; x++) {
                int r = scanner.nextInt();
                int g = scanner.nextInt();
                int b = scanner.nextInt();
                Color expected = ColorOperations.getColorAtPos(img, x, y);
                assertEquals(expected.getRed(), r);
                assertEquals(expected.getGreen(), g);
                assertEquals(expected.getBlue(), b);
            }
        }
        scanner.close();
    }

    @Test
    void testReadExistingPpm() {
        File ppmFile = new File("pm.ppm");
        if (!ppmFile.exists()) {
            return;
        }
        ImageEditor editor = new ImageEditor();
        editor.readPpmImage(ppmFile.getAbsolutePath());
        BufferedImage img = editor.getImage();
        assertNotNull(img);
        assertEquals(500, img.getWidth());
        assertEquals(500, img.getHeight());
    }
}
