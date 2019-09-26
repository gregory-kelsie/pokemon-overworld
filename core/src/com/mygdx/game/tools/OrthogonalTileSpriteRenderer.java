package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.mygdx.game.objects.BerryTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gregorykelsie on 2018-11-11.
 */

public class OrthogonalTileSpriteRenderer extends OrthogonalTiledMapRenderer {
    private List<Sprite> sprites;
    private Sprite playerSprite;
    private List<BerryTree> berries;
    public OrthogonalTileSpriteRenderer(TiledMap map, float unitScale) {
        super(map, unitScale);
        sprites = new ArrayList<Sprite>();
        berries = new ArrayList<BerryTree>();

    }

    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    public void addBerries(List<BerryTree> s) { berries = s; }

    public void removeSprites() {
        sprites.clear();
        berries = new ArrayList<BerryTree>();
    }

    public void setPlayerSprite(Sprite s) { this.playerSprite = s; }

    @Override
    public void render() {
        beginRender();
        int currentLayer = 0;
        int totalLayers = map.getLayers().size();
        for (MapLayer layer: map.getLayers()) {
            if (currentLayer == totalLayers - 1) {
                //Render the sprite first
                for (Sprite s: sprites) {
                    if (s.getY() > playerSprite.getY()) {
                        s.draw(this.getBatch());
                    }
                }
                for (BerryTree s: berries) {
                    if (s.isSpawned() && s.getSprite().getY() > playerSprite.getY()) {
                        s.getSprite().draw(this.getBatch());
                    }
                }
                playerSprite.draw(this.getBatch());
                for (Sprite s: sprites) {
                    if (s.getY() <= playerSprite.getY()) {
                        s.draw(this.getBatch());
                    }
                }
                for (BerryTree s: berries) {
                    if (s.isSpawned() && s.getSprite().getY() <= playerSprite.getY()) {
                        s.getSprite().draw(this.getBatch());
                    }
                }
            }
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                }
            } else {
                Gdx.app.log("invisible", "");
            }
            currentLayer++;
        }
        endRender();
    }
}
