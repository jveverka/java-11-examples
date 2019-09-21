package itx.images.dto;

public class ImageInfo {

    private final int pixelsX;
    private final int pixelsY;

    public ImageInfo(int pixelsX, int pixelsY) {
        this.pixelsX = pixelsX;
        this.pixelsY = pixelsY;
    }

    public int getPixelsX() {
        return pixelsX;
    }

    public int getPixelsY() {
        return pixelsY;
    }

    public static class Builder {

        private int pixelsX;
        private int pixelsY;

        public Builder setPixelsX(int pixelsX) {
            this.pixelsX = pixelsX;
            return this;
        }

        public Builder setPixelsY(int pixelsY) {
            this.pixelsY = pixelsY;
            return this;
        }

        public ImageInfo build() {
            return new ImageInfo(this.pixelsX, this.pixelsY);
        }

    }

}
