package google.hash.code.model;

public class GPosition {

    public final float x;
    public final float y;

    public GPosition(Position position, float widthUnit, float heightUnit) {
        this.x = (float) position.x * widthUnit;
        this.y = (float) position.y * heightUnit;
    }

    public GPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
