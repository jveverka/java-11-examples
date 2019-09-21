package itx.images;

public final class ParsingUtils {

    public static final String IMAGE_HEIGHT = "Image Height";
    public static final String IMAGE_WIDTH = "Image Width";

    private ParsingUtils() {
    }

    public static int getIntegerFromData(String data) {
        String[] words = data.split(" ");
        return Integer.parseInt(words[0]);
    }

}
