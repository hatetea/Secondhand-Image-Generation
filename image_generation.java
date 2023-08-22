import javax.media.jai.*; // надо установить Java Advanced Imaging
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImageGenerator {
    public static void generateImage(String imagePath, String query, String title, String subtitle) throws IOException {
        RenderedImage image = JAI.create("fileload", imagePath);

        int width = image.getWidth();
        int height = image.getHeight();

        double[][] pixelColors = new double[width][height];
        int numBands = image.getSampleModel().getNumBands();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double[] pixel = new double[numBands];
                image.getData().getPixel(x, y, pixel);
                pixelColors[x][y] = (pixel.length == 1) ? pixel[0] : (pixel[0] + pixel[1] + pixel[2]) / 3.0;
            }
        }

        double rSum = 0, gSum = 0, bSum = 0;
        int count = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double pixelValue = pixelColors[x][y];
                rSum += pixelValue;
                gSum += pixelValue;
                bSum += pixelValue;
                count++;
            }
        }

        int averageR = (int) (rSum / count);
        int averageG = (int) (gSum / count);
        int averageB = (int) (bSum / count);

        BufferedImage background = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = background.createGraphics();
        g2d.setColor(new Color(averageR, averageG, averageB, 255));
        g2d.fillRect(0, 0, 1000, 1000);

        BufferedImage circleImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
        Graphics2D circleG2d = circleImage.createGraphics();
        circleG2d.setColor(new Color(255, 255, 255, 0));
        circleG2d.fillOval(0, 0, 511, 511);
        circleG2d.setColor(new Color(255, 255, 255, 255));
        circleG2d.setStroke(new BasicStroke(10));
        circleG2d.drawOval(0, 0, 511, 511);

        double maxSize = Math.max(width, height);
        double resizeFactor = 512.0 / maxSize;
        int resizedWidth = (int) (width * resizeFactor);
        int resizedHeight = (int) (height * resizeFactor);

        BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D resizedG2d = resizedImage.createGraphics();
        resizedG2d.drawImage(image, 0, 0, resizedWidth, resizedHeight, null);

        int offsetX = (1000 - resizedWidth) / 2;
        int offsetY = (1000 - resizedHeight) / 2;

        background.createGraphics().drawImage(circleImage, offsetX, offsetY, null);
        background.createGraphics().drawImage(resizedImage, offsetX, offsetY, null);

        Font bigFont = new Font("Arial", Font.BOLD, 80);
        Font smallFont = new Font("Arial", Font.PLAIN, 45);

        g2d.setFont(bigFont);
        g2d.setColor(Color.WHITE);
        FontMetrics bigFontMetrics = g2d.getFontMetrics();
        int text1Width = bigFontMetrics.stringWidth(title);
        g2d.drawString(title, (1000 - text1Width) / 2, offsetY + resizedHeight + 50);

        g2d.setFont(smallFont);
        g2d.setColor(Color.WHITE);
        FontMetrics smallFontMetrics = g2d.getFontMetrics();
        int text2Width = smallFontMetrics.stringWidth(subtitle);
        g2d.drawString(subtitle, (1000 - text2Width) / 2, offsetY + resizedHeight + bigFontMetrics.getHeight() + 60);

        File output = new File("assets/second/" + query + ".png");
        try {
            ImageIO.write(background, "PNG", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        generateImage("image.png", "query", "Title", "Subtitle");
    }
}
