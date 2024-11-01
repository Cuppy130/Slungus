package engine;

public class Slungi {
    private float x, y, speed, direction;
    private int fireRate;
    private BattlingSlungus game;
    public boolean dead = false;
    public Slungi(float x, float y, float speed, int fireRate, BattlingSlungus game){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.fireRate = fireRate;
        this.game = game;
        setup();
    }

    public Slungi(float speed, int fireRate, BattlingSlungus game){
        this(game.getX(), game.getY(), speed, fireRate, game);
    }

    public void setup(){
        TimerUtils.invertval(()->{
            if(!dead){
                game.spawnProjectileEnemy(x, y);
            }
        }, fireRate);
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public void update(float delta) {
        direction = (float) Math.atan2(game.getPy() - y, game.getPx() - x);

        x += Math.cos(direction) * delta * speed;
        y += Math.sin(direction) * delta * speed;
    }

    public void kill() {
        dead = true;
    }
}
