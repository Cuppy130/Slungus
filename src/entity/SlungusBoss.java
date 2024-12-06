package entity;

import java.util.concurrent.ScheduledFuture;

import engine.TimerUtils;
import game.Game;

public class SlungusBoss extends Entity {
    private int bulletDamage = 1;
    private int fireRate = 1;
    private Game game;
    private ScheduledFuture<?> timer;
    @Override
    public void update(float delta) {
        direction = (float) Math.atan2(game.getPlayer().getY() - y, game.getPlayer().getX() - x);
        vx = (float) Math.cos(direction) * speed;
        vy = (float) Math.sin(direction) * speed;
        super.update(delta);
    }

    public SlungusBoss(Game game){
        super(640/2, -256, 256);
        this.game = game;
        speed = 0.1f;
        bulletDamage = 7;
        hp = 1000;
        maxHp = 1000;
        timer = TimerUtils.interval(()->{
            for (int i = 0; i < 8; i++) {
                TimerUtils.timeout(()->{
                    game.spawnEnemyBullet(x, y, bulletDamage);
                }, i*25);
            }
        }, 1000/fireRate);
    }

    public void stop(){
        TimerUtils.stopInterval(timer);
    }

    @Override
    public void takeDamage(int hp) {
        super.takeDamage(hp);
    }
}
