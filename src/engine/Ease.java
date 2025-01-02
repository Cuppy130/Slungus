package engine;

public class Ease {
    public static float linear(float t){
        return t;
    }
    public static float inQuad(float t){
        return t * t;
    }
    public static float outQuad(float t){
        return t * (2 - t);
    }
    public static float inOutQuad(float t){
        return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }
    public static float inCubic(float t){
        return t * t * t;
    }
    public static float outCubic(float t){
        return (--t) * t * t + 1;
    }
    public static float inOutCubic(float t){
        return t < 0.5 ? 4 * t * t * t : (t - 1) * (2 * t - 2) * (2 * t - 2) + 1;
    }
    public static float tri(float t){
        return Math.abs(1 - t % 2) * 2 - 1;
    }
}
