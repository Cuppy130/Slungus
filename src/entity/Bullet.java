package entity;

public class Bullet extends Entity {
    private boolean enemyBullet;
    public Bullet(float x, float y, boolean shotByEnemy, int bulletDamage, float direction){
        super(x, y, 16);
        this.bulletDamage = bulletDamage;
        this.direction = direction;
        enemyBullet = shotByEnemy;
    }

    @Override
    public void update(float delta) {
        this.vx = (float) Math.cos(direction) * 4;
        this.vy = (float) Math.sin(direction) * 4;
        super.update(delta);
    }

    public boolean isEnemyBullet() {
        return enemyBullet;
    }

    public int getDamage(){
        return bulletDamage;
    }
}
