package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.TiledApp;
import com.mygdx.game.objects.BerryTree;
import com.mygdx.game.objects.Sign;
import com.mygdx.game.objects.WarpObject;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.scenes.OverworldMenu;
import com.mygdx.game.sprites.Direction;
import com.mygdx.game.sprites.Mario;
import com.mygdx.game.sprites.NonPlayableCharacter;
import com.mygdx.game.sprites.State;
import com.mygdx.game.sprites.TalkingNPC;
import com.mygdx.game.sprites.components.PlayerSwitches;
import com.mygdx.game.sprites.components.TextBox;
import com.mygdx.game.tools.B2WorldCreator;
import com.mygdx.game.tools.MapLoader;
import com.mygdx.game.tools.OrthogonalTileSpriteRenderer;
import com.mygdx.game.tools.TextFormater;
import com.mygdx.game.tools.WorldContactListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gregorykelsie on 2018-07-25.
 */

public class PlayScreen implements Screen, MapLoader, NPCInteraction {

    public static final short NOTHING_BIT = 0;
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short WARP_BIT = 4;
    public static final short NPC_BIT = 8;
    public static final short WALL_BIT = 16;
    public static final short DOWN_LEDGE_BIT = 32;
    public static final short RIGHT_LEDGE_BIT = 64;
    public static final short LEFT_LEDGE_BIT = 128;
    public static final short SIGN_BIT = 256;
    public static final short BERRY_BIT = 512;

    private TiledApp app;
    private Texture texture;
    private Texture panelTexture;
    private Texture textureA;
    private Texture textureB;
    private Texture startButton;
    private Texture selectButton;
    private Texture textBox;
    private Texture textBoxTriangle;
    private Texture mapNameBoxTexture;

    private Hud hud;
    private OrthographicCamera gameCam;
    private OrthographicCamera c2;
    private Viewport gamePort;

    private TmxMapLoader loader;
    private TiledMap map;
    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;
    private OrthogonalTileSpriteRenderer renderer2;

    //Box2D Variables
    private World world;
    private List<Body> bodies;
    private List<Body> warpObjects;
    private List<Body> downLedges;
    private List<Body> rightLedges;
    private List<Body> leftLedges;
    private List<NonPlayableCharacter> npcs;
    private List<Sign> signs;
    private List<BerryTree> berryTrees;

    private float grassCount;

    private Box2DDebugRenderer b2dr;

    private Mario mario;
    private TalkingNPC npc;

    private TextureAtlas atlas;
    private TextureAtlas berryAtlas;

    private WarpObject w;

    private Music bgm;
    private String bgmName;

    private boolean displayTextBox;
    private BitmapFont regularFont;
    private TextBox tb;

    //Map Header Variables
    private boolean displayMapName;
    private int mapNameState; // Whether or not the map name is flying in or out or paused.
    private float mapNameElapsedTime;
    private int mapNameX;
    private String mapHeaderName;

    //Menu Variables
    private OverworldMenu overworldMenu;

    //Click Variables
    private boolean prevClick;
    private boolean curClick;

    //Player Variables
    PlayerSwitches switches;

    public PlayScreen(TiledApp app) {
        w = null;
        Gdx.app.log("test1", "" + 25.25f * 16 / TiledApp.PPM);
        this.app = app;
        init();
        prevClick = false;
        curClick = false;
        Date d = new Date();
        Gdx.app.log("DATATE", "Hr: " + d.getTime());
    }

    private void init() {
        grassCount = 0;
        displayTextBox = false;
        displayMapName = false;
        mapNameX = 1080;
        mapNameElapsedTime = 0;
        mapNameState = 0;
        overworldMenu = new OverworldMenu();

        switches = new PlayerSwitches();
        initTextures();
        initCameras();
        initMap();
        initBox2DWorld();
    }
    private void initTextures() {
        texture = new Texture("dpad.png");
        panelTexture = new Texture("panel.png");
        textureA = new Texture("Switch_A.png");
        textureB = new Texture("Switch_B.png");
        startButton = new Texture("start.png");
        selectButton = new Texture("select.png");
        textBox = new Texture("hud/textborder.png");
        mapNameBoxTexture = new Texture("hud/maptitleborder.png");
        textBoxTriangle = new Texture("hud/triangle.png");
        atlas = new TextureAtlas("sprites/PlayableCharacters.atlas");
        berryAtlas = new TextureAtlas("sprites/berry/berries.atlas");
        regularFont = new BitmapFont(Gdx.files.internal("hud/regularFont.fnt"));
        regularFont.setColor(Color.BLACK);
    }

    private void initCameras() {
        //Tiled Cam
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TiledApp.V_WIDTH / TiledApp.PPM, TiledApp.V_HEIGHT / TiledApp.PPM, gameCam);
        gamePort.apply();
        hud = new Hud(app.batch);
        gameCam.position.set((TiledApp.V_WIDTH / 2) / TiledApp.PPM, (TiledApp.V_HEIGHT / 2) /
        TiledApp.PPM, 0);
        //HUD Cam (Buttons)
        c2 = new OrthographicCamera();
        c2.setToOrtho(false, 1080, 1920);
    }

    public void setW(String mapName, float x, float y, Direction direction) {
        w = new WarpObject(mapName, x, y, direction, this);
    }

    public void loadMap(String mapName, float x, float y, Direction direction) {
        resetMapBox();
        bgm.stop();
        TiledApp.ASSET_MANAGER.unload(bgmName);
        map.dispose();
        for (Body b: bodies) {
            world.destroyBody(b);
        }
        for (Body warpObject: warpObjects) {
            world.destroyBody(warpObject);
        }
        for (Body b: downLedges) {
            world.destroyBody(b);
        }
        for (Body b: rightLedges) {
            world.destroyBody(b);
        }
        for (Body b: leftLedges) {
            world.destroyBody(b);
        }
        for (NonPlayableCharacter n: npcs) {
            world.destroyBody(n.getB2body());
        }
        for (Sign s: signs) {
            world.destroyBody(s.getB2body());
        }
        for (BerryTree t: berryTrees) {
            world.destroyBody(t.getB2body());
        }
        renderer2.removeSprites();
        map = loader.load("map/" + mapName + ".tmx");
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        tileWidth = map.getProperties().get("tilewidth", Integer.class);
        tileHeight = map.getProperties().get("tileheight", Integer.class);
        mapHeaderName = (String)map.getProperties().get("mapName");
        if (mapHeaderName == null) {
            mapHeaderName = "";
        }

        String newBgm = map.getProperties().get("bgm", String.class);
        mario.setDirection(direction);

        renderer2.setMap(map);
        mario.getB2body().setTransform(x, y, 0);
        adjustOffscreenCamera();
        bodies = B2WorldCreator.initCollisionBoxes(world, map);
        Gdx.app.log("initDownLeges", "BEGIN");
        downLedges = B2WorldCreator.initDownLedges(world, map);
        rightLedges = B2WorldCreator.initRightLedges(world, map);
        leftLedges = B2WorldCreator.initLeftLedges(world, map);
        warpObjects = B2WorldCreator.initWarpBoxes(world, map, this);
        npcs = B2WorldCreator.initNpcs(world, map, atlas);
        signs = B2WorldCreator.initSigns(world, map);
        berryTrees = B2WorldCreator.initBerryTrees(world, map, berryAtlas, switches);
        for (Sprite s: npcs) {
            renderer2.addSprite(s);
        }
        renderer2.addBerries(berryTrees);

        bgmName = "bgm/" + newBgm + ".mp3";
        TiledApp.ASSET_MANAGER.load(bgmName, Music.class);
        TiledApp.ASSET_MANAGER.finishLoading();
        bgm = TiledApp.ASSET_MANAGER.get(bgmName, Music.class);
        bgm.play();
        bgm.setLooping(true);
        if (!mapHeaderName.equals("")) {
            displayMapName = true;
        }
    }

    private void initMap() {
        loader = new TmxMapLoader();
        map = loader.load("map/3.0.tmx");
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        tileWidth = map.getProperties().get("tilewidth", Integer.class);
        tileHeight = map.getProperties().get("tileheight", Integer.class);
        renderer2 = new OrthogonalTileSpriteRenderer(map, 1 / TiledApp.PPM);
        TiledApp.ASSET_MANAGER.load("bgm/route1.mp3", Music.class);
        TiledApp.ASSET_MANAGER.finishLoading();

        bgm = TiledApp.ASSET_MANAGER.get("bgm/route1.mp3", Music.class);
        bgmName = "bgm/route1.mp3";
        bgm.play();
        bgm.setLooping(true);
        mapHeaderName = "Route 1";
        displayMapName = true;

    }

    private void initBox2DWorld() {
        //World gravity = 0 and bodies outside of simulation are resting/sleeping
        world = new World(new Vector2(0, 0), true);

        //DEBUGGER
        //b2dr = new Box2DDebugRenderer();

        //Add objects to the world
        bodies = B2WorldCreator.initCollisionBoxes(world, map);
        warpObjects = B2WorldCreator.initWarpBoxes(world, map, this);
        downLedges = B2WorldCreator.initDownLedges(world, map);
        leftLedges = B2WorldCreator.initLeftLedges(world, map);
        rightLedges = B2WorldCreator.initRightLedges(world, map);
        npcs = B2WorldCreator.initNpcs(world, map, atlas);
        signs = B2WorldCreator.initSigns(world, map);
        berryTrees = B2WorldCreator.initBerryTrees(world, map, berryAtlas, switches);

        //Add the player to the world
        mario = new Mario(world, this);
        renderer2.setPlayerSprite(mario);
        adjustOffscreenCamera();
        world.setContactListener(new WorldContactListener());

        for (Sprite s: npcs) {
            renderer2.addSprite(s);
        }
        renderer2.addBerries(berryTrees);
    }

    /**
     * Ensure that the camera is locked to only showing the map.
     * When the player is close to an edge, the camera should freeze.
     */
    private void adjustOffscreenCamera() {
        if (mario.getB2body().getPosition().x < TiledApp.V_WIDTH / 2 / TiledApp.PPM) {
            gameCam.position.x = TiledApp.V_WIDTH / 2 / TiledApp.PPM;
        } else if (mario.getB2body().getPosition().x > (mapWidth * tileWidth) / TiledApp.PPM - TiledApp.V_WIDTH / 2 / TiledApp.PPM) {
            gameCam.position.x = (mapWidth * tileWidth) / TiledApp.PPM - TiledApp.V_WIDTH / 2 / TiledApp.PPM;
        }

        if (mario.getB2body().getPosition().y < TiledApp.V_HEIGHT / 2 / TiledApp.PPM) {
            gameCam.position.y = TiledApp.V_HEIGHT / 2 / TiledApp.PPM;
        } else if (mario.getB2body().getPosition().y > (mapHeight * tileHeight) / TiledApp.PPM - ((TiledApp.V_HEIGHT / 2) / TiledApp.PPM)) {
            gameCam.position.y = (mapHeight * tileHeight) / TiledApp.PPM - ((TiledApp.V_HEIGHT / 2) / TiledApp.PPM);
        }
    }

    @Override
    public void openTextBox(TextBox t) {
        displayTextBox = true;
        tb = t;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    private void updateMapName(float dt) {
        mapNameElapsedTime += dt;
        if (mapNameState == 0) {
            if (mapNameElapsedTime >= 0.016) {
                mapNameX -= 16;
                if (mapNameX <= 600) {
                    mapNameState = 1;
                }
                mapNameElapsedTime = 0;
            }
        } else if (mapNameState == 2){
            if (mapNameElapsedTime >= 0.016) {
                mapNameX += 20;
                if (mapNameX >= 1080) {
                    resetMapBox();
                }
            }
        } else if (mapNameState == 1) {
            if (mapNameElapsedTime >= 1.5) {
                mapNameElapsedTime = 0;
                mapNameState = 2;
            }
        }
    }

    private void resetMapBox() {
        displayMapName = false;
        mapNameX = 1080;
        mapNameState = 0;
    }

    public void update(float dt) {
        handleInput(dt);
        world.step(1/60f, 6, 2);
        mario.update(dt);
        for (NonPlayableCharacter n: npcs) {
            n.update(dt);
        }
        for (BerryTree t: berryTrees) {
            t.update(dt);
        }
        //npc.update(dt);
        if (displayTextBox) {
            tb.update(dt);
        }
        if (displayMapName) {
            updateMapName(dt);
        }
        overworldMenu.update(dt);
        if (mario.getB2body().getPosition().x >= TiledApp.V_WIDTH / 2 / TiledApp.PPM &&
                mario.getB2body().getPosition().x <= (mapWidth * tileWidth) / TiledApp.PPM - TiledApp.V_WIDTH / 2 / TiledApp.PPM) {
            gameCam.position.x = mario.getB2body().getPosition().x;
        }
        if (mario.getB2body().getPosition().y >= TiledApp.V_HEIGHT / 2  / TiledApp.PPM &&
                mario.getB2body().getPosition().y <= ((mapHeight * tileHeight) / TiledApp.PPM) - TiledApp.V_HEIGHT / 2 / TiledApp.PPM) {
            gameCam.position.y = mario.getB2body().getPosition().y;
        }
        gameCam.update();
        renderer2.setView(gameCam);
        if (w != null) {
            loadMap(w.getWarpLocation(), w.getX() * 16 / TiledApp.PPM, w.getY() * 16 / TiledApp.PPM, w.getDirection());
            w = null;
        }
    }

    private void checkGrass(double dt) {
        TiledMapTileLayer ttl = (TiledMapTileLayer) map.getLayers().get("Grass");
        try {
            if (ttl.getCell(mario.getTileX(), mario.getTileY()) != null) {
                Gdx.app.log("GrassCount", grassCount + "");
                grassCount += dt;
                if (grassCount >= 1) {
                    grassCount = 0;
                    //Probability Check
                    if (Math.random() <= 0.22) {
                        Gdx.app.log("HasGrass", "ENCOUNTER!!");
                    }
                }

            }
        } catch (Exception e) {
            //Grass Not Found
        }
    }

    private boolean clicked() {
        if (curClick && !prevClick) {
            return true;
        }
        return false;
    }

    private boolean withinATile(float playerX, float objectX) {
        if (Math.abs(playerX - objectX) <= 6 / TiledApp.PPM) {
            return true;
        }
        return false;
    }

    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            prevClick = curClick;
            curClick = true;
            Gdx.app.log("touchcoords", "x: " + x + ", y: " + y);
            if (displayTextBox) {
                if (clicked()) {
                    if (tb.isFinished()) {
                        displayTextBox = false;
                        tb.refreshTextBox();
                    } else if (tb.isFinishedRendering()){
                        tb = tb.getNextTextBox();
                    } else {
                        tb.quickUpdate();
                    }
                }
            }
            else if (overworldMenu.isOpen()) {
                if (x >= 128 && x <= 276 && y >= 1220 && y <= 1344 && clicked()) {
                    overworldMenu.clickUp();
                } else if (x >= 128 && x <= 276 && y >= 1482 && y <= 1622 && clicked()) {
                    overworldMenu.clickDown();
                } else if (x >= 594 && x <= 776 && y >= 1421 && y <= 1549 && clicked()) {
                    overworldMenu.close();
                } else if (x >= 802 && x <= 1012 && y >= 1303 && y <= 1442 && clicked()) {
                    if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.POKEDEX) {

                    } else if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.POKEMON) {

                    } else if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.BAG) {

                    } else if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.PROFILE) {

                    } else if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.SAVE) {

                    } else if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.OPTIONS) {

                    } else if (overworldMenu.getPosition() == OverworldMenu.MenuPosition.EXIT) {
                        overworldMenu.close();
                    }
                } else if (x >= 162 && x <= 441 && y >= 1750 && y <= 1920 && clicked()) {
                    //Start Button
                    overworldMenu.close();
                }
            }
            else if (!mario.isJumping()) {
                if (x >= 292 && x <= 435 && y >= 1378 && y <= 1521 && mario.getB2body().getLinearVelocity().x <= 2) {
                    //Move right
                    //Apply force of 4f from the center and wake it up
                    mario.setDirection(Direction.RIGHT);
                    mario.getB2body().setLinearVelocity(new Vector2(0, 0));
                    mario.getB2body().applyLinearImpulse(new Vector2(2f, 0), mario.getB2body().getWorldCenter(), true);
                    checkGrass(dt);
                } else if (x >= 0 && x <= 143 && y >= 1378 && y <= 1521 && mario.getB2body().getLinearVelocity().x >= -2) {
                    mario.setDirection(Direction.LEFT);
                    mario.getB2body().setLinearVelocity(new Vector2(0, 0));
                    mario.getB2body().applyLinearImpulse(new Vector2(-2f, 0), mario.getB2body().getWorldCenter(), true);
                    checkGrass(dt);
                } else if (x >= 128 && x <= 276 && y >= 1220 && y <= 1344 && mario.getB2body().getLinearVelocity().y <= 2) {
                    mario.setDirection(Direction.UP);
                    mario.getB2body().setLinearVelocity(new Vector2(0, 0));
                    mario.getB2body().applyLinearImpulse(new Vector2(0, 2f), mario.getB2body().getWorldCenter(), true);
                    checkGrass(dt);
                } else if (x >= 128 && x <= 276 && y >= 1482 && y <= 1622 && mario.getB2body().getLinearVelocity().y >= -2) {
                    mario.setDirection(Direction.DOWN);
                    mario.getB2body().setLinearVelocity(new Vector2(0, 0));
                    mario.getB2body().applyLinearImpulse(new Vector2(0, -2f), mario.getB2body().getWorldCenter(), true);
                    checkGrass(dt);
                } else if (x >= 594 && x <= 776 && y >= 1421 && y <= 1549) {
                    //B Button
                    Gdx.app.log("Button", "B");
                } else if (x >= 162 && x <= 441 && y >= 1750 && y <= 1920) {
                    //Start Button
                    overworldMenu.open();
                } else if (x >= 802 && x <= 1012 && y >= 1303 && y <= 1442 && clicked()) {
                    //A Button
                    //Check if any menu is open, dialogue boxes etc
                    //If nothing is open check if the player interacted with any npcs.
                    Gdx.app.log("Button", "A");
                    Gdx.app.log("Player Position:", "x: " +
                            mario.getB2body().getPosition().x + ", y: " + mario.getB2body().getPosition().y);
                    boolean interacted = false;
                    for (NonPlayableCharacter n : npcs) {
                        Gdx.app.log("NPC Position:", "x: " +
                                n.getB2body().getPosition().x + ", y: " + n.getB2body().getPosition().y);
                        float xRatio = mario.getB2body().getPosition().x / n.getB2body().getPosition().x;
                        float yRatio = mario.getB2body().getPosition().y / n.getB2body().getPosition().y;
                        Gdx.app.log("Ratios:", "x: " +
                                xRatio + ", y: " + yRatio);
                        Gdx.app.log("Tile Difference:", "playerx: " +
                                mario.getTileXX() + ", npcx: " + n.getTileXX() + ", playery: " + mario.getTileYY() +
                        ", npcy: " + n.getTileYY());

                        if (mario.getDirection() == Direction.RIGHT) {
                            if ((mario.getTileXX() + 1 == n.getTileXLeftSide() || mario.getTileXX() == n.getTileXLeftSide()) &&
                                    (withinATile(mario.getB2body().getPosition().y, n.getB2body().getPosition().y))) {
                                n.interact(Direction.RIGHT, this);
                                interacted = true;
                                break;
                            }
                        } else if (mario.getDirection() == Direction.LEFT) {
                            if ((mario.getTileXX() - 1 == n.getTileXRightSide() || mario.getTileXX() == n.getTileXRightSide()) &&
                                    withinATile(mario.getB2body().getPosition().y, n.getB2body().getPosition().y)) {
                                n.interact(Direction.LEFT, this);
                                interacted = true;
                                break;
                            }
                        } else if (mario.getDirection() == Direction.DOWN) {
                            if (withinATile(mario.getB2body().getPosition().x, n.getB2body().getPosition().x) &&
                                    ((mario.getTileYY() - 1 == n.getTileYTopSide()) || mario.getTileYY() == n.getTileYTopSide())) {
                                n.interact(Direction.DOWN, this);
                                interacted = true;
                                break;
                            }
                        } else if (mario.getDirection() == Direction.UP) {
                            if (withinATile(mario.getB2body().getPosition().x, n.getB2body().getPosition().x) &&
                                    ((mario.getTileYY() + 1 == n.getTileYBotSide()) || mario.getTileYY() == n.getTileYBotSide())) {
                                n.interact(Direction.UP, this);
                                interacted = true;
                                break;
                            }
                        }
                    }
                    if (!interacted) {
                        for (Sign s : signs) {
                            if (mario.getDirection() == Direction.UP) {
                                if (withinATile(mario.getB2body().getPosition().x, s.getB2body().getPosition().x) &&
                                        ((mario.getTileYY() + 1 == s.getTileYBotSide()) || mario.getTileYY() == s.getTileYBotSide())) {
                                    openTextBox(s.getTextBox());
                                    interacted = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!interacted) {
                        for (BerryTree t : berryTrees) {
                            if (mario.getDirection() == Direction.UP) {
                                if (withinATile(mario.getB2body().getPosition().x, t.getB2body().getPosition().x) &&
                                        ((mario.getTileYY() + 1 == t.getTileYBotSide()) || mario.getTileYY() == t.getTileYBotSide())) {
                                    t.interact(switches, this);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            prevClick = false;
            curClick = false;
            if (!mario.isJumping()) {
                Gdx.app.log("blah", "blahblah");
                mario.getB2body().setLinearVelocity(new Vector2(0, 0));
            }
        }
    }
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport( 0,Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);

        renderer2.render();
        //b2dr.render(world, gameCam.combined);

        /**
        app.batch.setProjectionMatrix(gameCam.combined);
        app.batch.begin();
        //mario.draw(app.batch);
        app.batch.end(); */

        Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );
        //app.batch.setProjectionMatrix(hud.getCamera().combined);
        //hud.draw();
        app.batch.setProjectionMatrix(c2.combined);
        app.batch.begin();
        app.batch.draw(panelTexture, 0, 0);
        app.batch.draw(texture, 50, 300, 380, 380);
        app.batch.draw(textureB, 600, 350, 230, 230);
        app.batch.draw(textureA, 820, 450, 230, 230);
        app.batch.draw(startButton, 230, -20, 230, 230);
        app.batch.draw(selectButton, 650, -20, 230, 230);


        if (displayTextBox) {
            app.batch.draw(textBox, 40, 968, 1000, 218); //Was 178
            tb.render(app.batch, regularFont, textBoxTriangle);
        }
        if (displayMapName) {
            //Render Map Box
            //Render Map Name
            app.batch.draw(mapNameBoxTexture, mapNameX, 1800, 464, 88);
            regularFont.draw(app.batch, mapHeaderName, mapNameX + 232 - ((int)TextFormater.getWordLengthValue(mapHeaderName) * 12), 1862);
        }
        overworldMenu.render(app.batch);

        app.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        //b2dr.dispose();
        renderer2.dispose();
        map.dispose();
        texture.dispose();
        hud.dispose();
        panelTexture.dispose();
        textureA.dispose();
        textureB.dispose();
        textBox.dispose();
        atlas.dispose();
        berryAtlas.dispose();
        regularFont.dispose();
        textBoxTriangle.dispose();
        mapNameBoxTexture.dispose();
        overworldMenu.dispose();
        startButton.dispose();
        selectButton.dispose();

    }
}
