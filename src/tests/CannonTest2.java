package tests;

import drawing.DynamicPoint;
import drawing.Polygon;
import game.Game;
import modules.GameModule;
import util.physics.Matrix3x3f;
import util.physics.Vector2f;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Game Framework
 * Created by MyNameReallySux on 9/3/2014.
 * Copyright 2014©
 */

public class CannonTest2 extends GameModule {
    private final String LOG = "CannonTest2";
    private Vector2f bulletVelocity;
    private Polygon cannon;
    private DynamicPoint bullet;


    public CannonTest2(Game game) {
        super(game, "CannonTest");
    }

    @Override
    public boolean initialize() {
        cannon = new Polygon(new Vector2f[]{
                new Vector2f(0f, 0.05f),
                new Vector2f(0.75f, 0.05f),
                new Vector2f(0.75f, -0.05f),
                new Vector2f(0f, -0.05f)
        }, new Vector2f(), 45f, 30f);

        bulletVelocity = new Vector2f();
        Matrix3x3f scale = Matrix3x3f.scale(0.5f, 0.5f);
        for(int i = 0; i < cannon.base.length; i++){
            cannon.base[i] = cannon.base[i].mul(scale);
        }

        return true;
    }

    @Override
    public void input(double delta) {
        if(Game.keyboard().keyDown(KeyEvent.VK_LEFT)){
            cannon.rotation += cannon.rotationDelta * delta;
        }
        if(Game.keyboard().keyDown(KeyEvent.VK_RIGHT)){
            cannon.rotation -= cannon.rotationDelta * delta;
        }

        if(Game.keyboard().keyDownOnce(KeyEvent.VK_SPACE)) {
            bullet = new DynamicPoint(new Vector2f());
            Matrix3x3f scale = Matrix3x3f.scale(0.5f, 0.5f);
            for(int i = 0; i < bullet.base.length; i++){
                bullet.base[i] = bullet.base[i].mul(scale);
            }

            Matrix3x3f matrix = Matrix3x3f.translate(5f, 0f);
            matrix = matrix.mul(Matrix3x3f.rotate(cannon.rotation));

            bullet.velocity = new Vector2f().mul(matrix);

            matrix = Matrix3x3f.translate(0.375f, 0f);
            matrix = matrix.mul(Matrix3x3f.rotate(cannon.rotation));
            matrix = matrix.mul(Matrix3x3f.translate(-1.0f, -1.0f));
            bullet.position = matrix.toVector(false);
        }
    }

    @Override
    public void update(double delta) {
        Matrix3x3f matrix = Matrix3x3f.identity();
        matrix = matrix.mul(Matrix3x3f.rotate(cannon.rotation));
        matrix = matrix.mul(Matrix3x3f.translate(-1.0f, -1.0f));

        for(int i = 0; i < cannon.base.length; i++){
            cannon.world[i] = cannon.base[i].mul(matrix);
        }

        if(bullet != null){
            matrix = Matrix3x3f.identity();
            matrix = matrix.mul(Matrix3x3f.rotate(bullet.rotation));
            matrix = matrix.mul(Matrix3x3f.translate(bullet.position.x, bullet.position.y));

            for(int i = 0; i < bullet.base.length; i++){
                bullet.world[i] = bullet.base[i].mul(matrix);
            }

            bullet.update(delta);
            bulletVelocity = bullet.velocity;
            if(bullet.position.y < -Game.screen().worldHeight * 1.1){
                bullet = null;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawString("<- to Raise, -> to Lower", 20, 50);
        g.drawString("Press Space to Fire Cannon", 20, 65);
        String vel = String.format("Velocity (%.2f, %.2f)", bulletVelocity.x, bulletVelocity.y);
        g.drawString(vel, 20, 80);

        for(int i = 0; i < cannon.base.length; i++){
            cannon.world[i] = cannon.world[i].mul(Game.viewport());
        }

        g.setColor(Color.WHITE);
        cannon.drawPolygon(g);

        if(bullet != null){
            for(int i = 0; i < bullet.world.length; i++){
                bullet.world[i] = bullet.world[i].mul(Game.viewport());
            }
            bullet.drawCircle(g, 5);
        }
    }
}
