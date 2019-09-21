package itx.images.tests;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
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
        InputStream imageStream = this.getClass().getResourceAsStream("/IMG_20180827_190350.jpg");
        ImageInfo imageInfo = ImageDataReader.getImageInfo(imageStream);
        Assert.assertNotNull(imageInfo);
        Assert.assertTrue(imageInfo.getPixelsX() == 2448);
        Assert.assertTrue(imageInfo.getPixelsY() == 3264);
    }

}
