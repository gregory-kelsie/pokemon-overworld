package com.mygdx.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.TiledApp;
import com.mygdx.game.npcfactory.BerryFactory;
import com.mygdx.game.npcfactory.NpcFactory;
import com.mygdx.game.npcfactory.TextBoxFactory;
import com.mygdx.game.objects.BerryTree;
import com.mygdx.game.objects.Sign;
import com.mygdx.game.objects.WarpObject;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.sprites.NonPlayableCharacter;
import com.mygdx.game.sprites.components.PlayerSwitches;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gregorykelsie on 2018-07-27.
 */

public class B2WorldCreator {
    private World world;
    private TiledMap map;

    public static List<Body> initCollisionBoxes(World world, TiledMap map) {
        List<Body> bodies = new ArrayList<Body>();
        try {
            //Before creaitng a body we define what the body consists of - BodyDef
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            //Before creating a fixture, we define it as well
            FixtureDef fdef = new FixtureDef();
            Body body;
            for (MapObject object : map.getLayers().get("col").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / TiledApp.PPM,
                        (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / TiledApp.PPM, (rect.getHeight() / 2) / TiledApp.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = PlayScreen.WALL_BIT;
                body.createFixture(fdef);
                bodies.add(body);
            }
            return bodies;
        } catch (Exception e) {
            return new ArrayList<Body>();
        }
    }

    public static List<BerryTree> initBerryTrees(World world, TiledMap map, TextureAtlas atlas, PlayerSwitches switches) {
        List<BerryTree> berryTrees = new ArrayList<BerryTree>();
        BerryFactory bf = new BerryFactory(atlas, world);
        try {
            //Before creaitng a body we define what the body consists of - BodyDef
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            //Before creating a fixture, we define it as well
            FixtureDef fdef = new FixtureDef();
            Body body;
            for (MapObject object : map.getLayers().get("berry").getObjects().getByType(RectangleMapObject.class)) {
                int berryId = object.getProperties().get("id", Integer.class);
                int berryType = object.getProperties().get("type", Integer.class);
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                float x = (rect.getX() + rect.getWidth() / 2) / TiledApp.PPM;
                float y = (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM;
                Gdx.app.log("berryy", "" + y);
                berryTrees.add(bf.getBerryTree(berryId, berryType, x, y, switches.getBerryTimer(berryId)));
            }
            return berryTrees;
        } catch (Exception e) {
            return new ArrayList<BerryTree>();
        }
    }

    public static List<Sign> initSigns(World world, TiledMap map) {
        List<Sign> signs = new ArrayList<Sign>();
        TextBoxFactory tbf = new TextBoxFactory();
        try {
            //Before creaitng a body we define what the body consists of - BodyDef
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            //Before creating a fixture, we define it as well
            FixtureDef fdef = new FixtureDef();
            Body body;
            for (MapObject object : map.getLayers().get("sign").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                int signId = object.getProperties().get("id", Integer.class);
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / TiledApp.PPM,
                        (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / TiledApp.PPM, (rect.getHeight() / 2) / TiledApp.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = PlayScreen.SIGN_BIT;
                body.createFixture(fdef);
                signs.add(new Sign(body, tbf.getTextBox(signId)));

            }
            return signs;
        } catch (Exception e) {
            return new ArrayList<Sign>();
        }
    }

    public static List<Body> initDownLedges(World world, TiledMap map) {
        List<Body> bodies = new ArrayList<Body>();
        try {

            //Before creaitng a body we define what the body consists of - BodyDef
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            //Before creating a fixture, we define it as well
            FixtureDef fdef = new FixtureDef();
            Body body;
            Gdx.app.log("ledgeDown", "1");
            for (MapObject object : map.getLayers().get("ledgeDown").getObjects().getByType(RectangleMapObject.class)) {
                Gdx.app.log("ledgeDown", "2");
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / TiledApp.PPM,
                        (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / TiledApp.PPM, (rect.getHeight() / 2) / TiledApp.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = PlayScreen.DOWN_LEDGE_BIT;
                body.createFixture(fdef).setUserData("ledgeDown");
                bodies.add(body);
            }
            return bodies;
        } catch (Exception e) {
            Gdx.app.log("ledge", e.getMessage());
            return new ArrayList<Body>();
        }
    }

    public static List<Body> initRightLedges(World world, TiledMap map) {
        List<Body> bodies = new ArrayList<Body>();
        try {

            //Before creaitng a body we define what the body consists of - BodyDef
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            //Before creating a fixture, we define it as well
            FixtureDef fdef = new FixtureDef();
            Body body;
            for (MapObject object : map.getLayers().get("ledgeRight").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / TiledApp.PPM,
                        (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / TiledApp.PPM, (rect.getHeight() / 2) / TiledApp.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = PlayScreen.RIGHT_LEDGE_BIT;
                body.createFixture(fdef).setUserData("ledgeRight");
                bodies.add(body);
            }
            return bodies;
        } catch (Exception e) {
            Gdx.app.log("ledge", e.getMessage());
            return new ArrayList<Body>();
        }
    }

    public static List<Body> initLeftLedges(World world, TiledMap map) {
        List<Body> bodies = new ArrayList<Body>();
        try {
            //Before creaitng a body we define what the body consists of - BodyDef
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            //Before creating a fixture, we define it as well
            FixtureDef fdef = new FixtureDef();
            Body body;
            for (MapObject object : map.getLayers().get("ledgeLeft").getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / TiledApp.PPM,
                        (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / TiledApp.PPM, (rect.getHeight() / 2) / TiledApp.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = PlayScreen.LEFT_LEDGE_BIT;
                body.createFixture(fdef).setUserData("ledgeLeft");
                bodies.add(body);
            }
            return bodies;
        } catch (Exception e) {
            Gdx.app.log("ledge", e.getMessage());
            return new ArrayList<Body>();
        }
    }

    public static List<NonPlayableCharacter> initNpcs(World world, TiledMap map, TextureAtlas atlas) {
        List<NonPlayableCharacter> npcs = new ArrayList<NonPlayableCharacter>();
        NpcFactory npcFactory = new NpcFactory();
        try {
            for (MapObject object : map.getLayers().get("npc").getObjects().getByType(RectangleMapObject.class)) {
                //Initialize NPC Data
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                float x = (rect.getX() + rect.getWidth() / 2) / TiledApp.PPM;
                float y = (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM;
                String spriteName = (String)object.getProperties().get("sprite");
                int npcID = object.getProperties().get("id", Integer.class);

                //Create the warp object.
                npcs.add(npcFactory.createNPC(npcID, spriteName, x, y, atlas, world));
            }
            return npcs;
        } catch (Exception e) {
            Gdx.app.log("warperror", e.toString());
            return new ArrayList<NonPlayableCharacter>();
        }
    }

    public static List<Body> initWarpBoxes(World world, TiledMap map, MapLoader mapLoader) {
        List<Body> bodies = new ArrayList<Body>();
        try {
            BodyDef bdef = new BodyDef();
            PolygonShape shape = new PolygonShape();
            FixtureDef fdef = new FixtureDef();
            Body body;
            for (MapObject object : map.getLayers().get("warp").getObjects().getByType(RectangleMapObject.class)) {
                //Initialize Warp Data
                String warpLocation = (String)object.getProperties().get("warpLocation");
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);

                //Determine the direction of the player after going through the warp.
                int intDirection = object.getProperties().get("direction", Integer.class);
                Direction direction;
                if (intDirection == 0) {
                    direction = Direction.UP;
                } else if (intDirection == 2) {
                    direction = Direction.LEFT;
                } else if (intDirection == 3) {
                    direction = Direction.RIGHT;
                } else {
                    direction = Direction.DOWN;
                }

                //Create Box2D Body and Fixture
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / TiledApp.PPM,
                        (rect.getY() + rect.getHeight() / 2) / TiledApp.PPM);
                body = world.createBody(bdef);
                shape.setAsBox((rect.getWidth() / 2) / TiledApp.PPM, (rect.getHeight() / 2) / TiledApp.PPM);
                fdef.filter.categoryBits = PlayScreen.WARP_BIT;
                fdef.shape = shape;

                //Create the warp object.
                WarpObject warpObject = new WarpObject(warpLocation, x, y, direction, mapLoader);
                body.createFixture(fdef).setUserData(warpObject);

                //Add the warp object to the list of warp objects for the map.
                bodies.add(body);

            }
            return bodies;
        } catch (Exception e) {
            Gdx.app.log("warperror", e.toString());
            return new ArrayList<Body>();
        }
    }
}
