package pl.sebastianklimas.recipesmenager.ai.image;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ImageServiceTest {

    private final ImageService imageService = new ImageService();

    @Test
    void shouldThrowExceptionWhenImageCannotBeLoaded() {
        InputStream invalidStream = new ByteArrayInputStream(new byte[0]);

        assertThatThrownBy(() -> imageService.simpleResizeImage(invalidStream, "image/png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unable to load image from stream");
    }

    @Test
    void shouldResizeImageWhenLargerThanMax() throws IOException {
        BufferedImage largeImage = new BufferedImage(4000, 3000, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(largeImage, "png", baos);

        InputStream input = new ByteArrayInputStream(baos.toByteArray());

        InputStream result = imageService.simpleResizeImage(input, "image/png");

        BufferedImage resized = ImageIO.read(result);
        assertThat(resized.getWidth()).isLessThanOrEqualTo(1980);
        assertThat(resized.getHeight()).isLessThanOrEqualTo(1080);
    }

    @Test
    void shouldReturnSameImageIfWithinMaxSize() throws IOException {
        BufferedImage smallImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(smallImage, "png", baos);

        InputStream input = new ByteArrayInputStream(baos.toByteArray());
        InputStream result = imageService.simpleResizeImage(input, "image/png");

        BufferedImage sameImage = ImageIO.read(result);
        assertThat(sameImage.getWidth()).isEqualTo(800);
        assertThat(sameImage.getHeight()).isEqualTo(600);
    }
}
