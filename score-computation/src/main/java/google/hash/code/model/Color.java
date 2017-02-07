package google.hash.code.model;

public class Color {
    public final float red;
    public final float green;
    public final float blue;

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public String toString() {
        return String.format("#%02x%02x%02x", (int) (red * 255), (int) (green * 255), (int) (blue * 255));
    }
}
