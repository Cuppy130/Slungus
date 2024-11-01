package engine;

public class Projectile {
    private float x, y;
    private float vx, vy;
    private final int width = 16;
    private final int height = 16;
    private boolean enemyProjectile;

    public Projectile(float startX, float startY, float xVelocity, float yVelocity, boolean enemyProjectile) {
        this.x = startX;
        this.y = startY;
        this.enemyProjectile = enemyProjectile;
        
        this.vx = xVelocity;
        this.vy = yVelocity;
    }

    public void update() {
        x += vx;
        y += vy;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean AABB(float x2, float y2, float width2, float height2){
        return x < x2 + width2 && x + width > x2 && y < y2 + height2 && y + height > y2;
    }

    public void render(Runnable runme) {
        runme.run();
    }

    public boolean isEnemy() {
        return enemyProjectile;
    }

    public boolean shouldBeRemoved() {
        if(!AABB(0, 0, 640, 480)){
            return true;
        }
        return false;
    }
}
