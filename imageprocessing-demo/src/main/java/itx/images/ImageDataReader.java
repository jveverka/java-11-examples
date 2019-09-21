package itx.images;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import itx.images.dto.ImageInfo;

import java.io.IOException;
import java.io.InputStream;

public class ImageDataReader {

    public static ImageInfo getImageInfo(InputStream imageStream) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(imageStream);
        ImageInfo.Builder builder = new ImageInfo.Builder();
        for (Directory directory: metadata.getDirectories()) {
            for (Tag tag: directory.getTags()) {
                if (ParsingUtils.IMAGE_HEIGHT.equals(tag.getTagName())) {
                    builder.setPixelsY(ParsingUtils.getIntegerFromData(tag.getDescription()));
                } else if (ParsingUtils.IMAGE_WIDTH.equals(tag.getTagName())) {
                    builder.setPixelsX(ParsingUtils.getIntegerFromData(tag.getDescription()));
                }
            }
        }
        return builder.build();
    }

}
