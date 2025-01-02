package misc;

public class Color {
    private int value = 0;

    public static final Color WHITE = new Color(255, 255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0, 255);
    public static final Color RED = new Color(255, 0, 0, 255);
    public static final Color GREEN = new Color(0, 255, 0, 255);
    public static final Color BLUE = new Color(0, 0, 255, 255);
    public static final Color YELLOW = new Color(255, 255, 0, 255);
    public static final Color CYAN = new Color(0, 255, 255, 255);
    public static final Color MAGENTA = new Color(255, 0, 255, 255);

    public Color(int r, int g, int b, int a) {
        value = (a << 24) | (r << 16) | (g << 8) | b;
    }

    public Color(int value){
        this.value = value;
    }

    public Color(float f, float g, float h) {
        this((int)(f * 255), (int)(g * 255), (int)(h * 255), 255);
    }

    public int getR() {
        return (value >> 16) & 0xFF;
    }

    public int getG() {
        return (value >> 8) & 0xFF;
    }

    public int getB() {
        return value & 0xFF;
    }

    public int getA() {
        return (value >> 24) & 0xFF;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setR(int r) {
        value = (value & 0x00FFFFFF) | (r << 16);
    }

    public void setG(int g) {
        value = (value & 0xFF00FFFF) | (g << 8);
    }

    public void setB(int b) {
        value = (value & 0xFFFF00FF) | b;
    }

    public void setA(int a) {
        value = (value & 0x00FFFFFF) | (a << 24);
    }

    public void set(int r, int g, int b, int a) {
        value = (a << 24) | (r << 16) | (g << 8) | b;
    }

    public void set(int value) {
        this.value = value;
    }

    public static Color fromHex(String hex) {
        return new Color(
            Integer.valueOf(hex.substring(1, 3), 16),
            Integer.valueOf(hex.substring(3, 5), 16),
            Integer.valueOf(hex.substring(5, 7), 16),
            Integer.valueOf(hex.substring(7, 9), 16)
        );
    }

    public static Color fromRGB(int r, int g, int b) {
        return new Color(r, g, b, 255);
    }

    public static Color fromRGBA(int r, int g, int b, int a) {
        return new Color(r, g, b, a);
    }

    @Override
    public String toString() {
        return "Color{" +
            "r =" + getR() + ", g =" + getG() + ", b =" + getB() + ", a =" + getA() +
            '}';
    }
}
