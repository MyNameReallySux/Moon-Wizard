package tests;

import game.Game;
import modules.GameModule;
import util.physics.Vector2f;

import java.awt.*;

/**
 * Created by Shorty on 8/25/2014.
 */
public class ScreenMappingTest extends GameModule {
    Vector2f[] tri, triWorld, rect, rectWorld;

    public ScreenMappingTest(Game game){
        super(game, "ScreenMappingTest");
    }

    @Override
    public boolean initialize() {
        tri = new Vector2f[]{
                new Vector2f( 0.0f, 1.0f),
                new Vector2f(-1.0f,-1.0f),
                new Vector2f( 1.0f,-1.0f)
        };
        triWorld = new Vector2f[tri.length];
        rect = new Vector2f[]{
                new Vector2f(-Game.screen().worldWidth / 2 , Game.screen().worldHeight / 2),
                new Vector2f(Game.screen().worldWidth / 2, Game.screen().worldHeight / 2),
                new Vector2f(Game.screen().worldWidth / 2, -Game.screen().worldHeight / 2),
                new Vector2f(-Game.screen().worldWidth / 2, -Game.screen().worldHeight / 2)
        };
        rectWorld = new Vector2f[rect.length];

        return true;
    }

    @Override
    public void input(double delta) {

    }

    @Override
    public void update(double delta) {
        for(int i = 0; i < tri.length; i++){
            triWorld[i] = tri[i].mul(Game.viewport());
        }

        for(int i = 0; i < rect.length; i++){
            rectWorld[i] = rect[i].mul(Game.viewport());
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        drawPolygon(g, triWorld);
        drawPolygon(g, rectWorld);
    }

    public void drawPolygon(Graphics g, Vector2f[] polygon){
        Vector2f P;
        Vector2f S = polygon[polygon.length - 1];
        for(int i = 0; i < polygon.length; i++){
            P = polygon[i];
            g.drawLine((int)S.x, (int)S.y, (int)P.x, (int)P.y);
            S = P;
        }
    }
}
