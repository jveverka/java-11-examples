package itx.images.tests;

import com.drew.imaging.ImageProcessingException;
import itx.images.ImageDataReader;
import itx.images.dto.ImageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class ImageDataReaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(ImageDataReaderTest.class);

    @Test
    public void testExifDataRead() throws ImageProcessingException, IOException {
        final String imagePath = "/IMG_20180827_190350.jpg";
        LOG.info("reading image {}", imagePath);
        InputStream imageStream = this.getClass().getResourceAsStream(imagePath);
        ImageInfo imageInfo = ImageDataReader.getImageInfo(imageStream);
        Assert.assertNotNull(imageInfo);
        Assert.assertTrue(imageInfo.getPixelsX() == 2448);
        Assert.assertTrue(imageInfo.getPixelsY() == 3264);
    }

}
