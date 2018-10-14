
public class Image {
    private int w;
    private int h;

    public Image (int height, int width) {
        this.w = width;
        this.h = height;
        this.aPixel = new Pixel[h][w];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                aPixel[i][j] = new Pixel(0,0,0);
            }
        }
    }
    public Pixel[][] aPixel;
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }
}
