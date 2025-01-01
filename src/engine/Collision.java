package engine;

public class Collision {
    public static boolean AABB(float x1, float y1, float s1, float x2, float y2, float s2){
        return x1 - s1 / 2 < x2 + s2 / 2 && x1 + s1 / 2 > x2 - s2 / 2 && y1 - s1 / 2 < y2 + s2 / 2 && y1 + s1 / 2 > y2 - s2 / 2;
    }
}
