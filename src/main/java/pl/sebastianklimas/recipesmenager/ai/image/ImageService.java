package pl.sebastianklimas.recipesmenager.ai.image;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {
    private static final int MAX_WIDTH = 1980;
    private static final int MAX_HEIGHT = 1080;

    public InputStream simpleResizeImage(InputStream inputStream, String contentType) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);

        if (originalImage == null) {
            throw new IllegalArgumentException("Unable to load image from stream");
        }

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        double scale = Math.min(
                (double) MAX_WIDTH / width,
                (double) MAX_HEIGHT / height
        );

        if (scale >= 1.0) {
            return toInputStream(originalImage, contentType);
        }

        int targetWidth = (int) (width * scale);
        int targetHeight = (int) (height * scale);

        BufferedImage resized = Scalr.resize(originalImage, targetWidth, targetHeight);
        return toInputStream(resized, contentType);
    }

    private InputStream toInputStream(BufferedImage image, String contentType) throws IOException {
        String format = mapContentTypeToFormat(contentType);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return new ByteArrayInputStream(baos.toByteArray());
        }
    }

    private String mapContentTypeToFormat(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> throw new IllegalArgumentException("Unsupported image type: " + contentType);
        };
    }
}
