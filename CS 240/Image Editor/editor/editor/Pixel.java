public class Pixel {
    private int R;
    private int G;
    private int B;

    public  Pixel (int red, int green, int blue) {
        this.R = red;
        this.G = green;
        this.B = blue;
    }

    public void setR(int red) {
        R = red;
    }
    public void setG(int green) {
        G = green;
    }
    public void setB(int blue) {
        B = blue;
    }

    public int getB() {
        return B;
    }

    public int getG() {
        return G;
    }

    public int getR() {
        return R;
    }
}
