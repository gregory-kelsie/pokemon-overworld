package com.mygdx.game.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gregorykelsie on 2018-12-02.
 */

public class OverworldMenu implements Disposable {

    public enum MenuPosition { POKEDEX(0), POKEMON(1), BAG(2), PROFILE(3), SAVE(4), OPTIONS(5),
        EXIT(6);
        private int value;
        private MenuPosition(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
        private static Map<Integer, MenuPosition> map = new HashMap<Integer, MenuPosition>();

        static {
            for (MenuPosition legEnum : MenuPosition.values()) {
                map.put(legEnum.getValue(), legEnum);
            }
        }

        public static MenuPosition valueOf(int value) {
            return map.get(value);
        }

    };

    private final int STOP_POSITION = 680;
    private MenuPosition position;
    private boolean exiting; // When the menu is exiting the screen.
    private boolean display;
    private int x;
    private int y;
    private float elapsedTime;
    private Texture border;
    private Texture selector;
    private Texture pokedex;
    private Texture darkPokedex;
    private Texture pokemon;
    private Texture darkPokemon;
    private Texture bag;
    private Texture darkBag;
    private Texture profile;
    private Texture darkProfile;
    private Texture save;
    private Texture darkSave;
    private Texture options;
    private Texture darkOptions;
    private Texture exit;
    private Texture darkExit;

    public OverworldMenu() {
        initMenu();
        initTextures();
    }

    public void initMenu() {
        position = MenuPosition.POKEDEX;
        x = 1080;
        y = 1080;
        display = false;
        exiting = false;
        elapsedTime = 0;
    }

    private void initTextures() {
        border = new Texture("hud/menu/menu.png");
        selector = new Texture("hud/menu/selector.png");
        pokedex = new Texture("hud/menu/pokedex.png");
        darkPokedex = new Texture("hud/menu/pokedex-d.png");
        pokemon = new Texture("hud/menu/pokemon.png");
        darkPokemon = new Texture("hud/menu/pokemon-d.png");
        bag = new Texture("hud/menu/bag.png");
        darkBag = new Texture("hud/menu/bag-d.png");
        profile = new Texture("hud/menu/profile.png");
        darkProfile = new Texture("hud/menu/profile-d.png");
        save = new Texture("hud/menu/save.png");
        darkSave = new Texture("hud/menu/save-d.png");
        options = new Texture("hud/menu/options.png");
        darkOptions = new Texture("hud/menu/options-d.png");
        exit = new Texture("hud/menu/exit.png");
        darkExit = new Texture("hud/menu/exit-d.png");
    }

    public void clickDown() {
        if (position != MenuPosition.EXIT) {
            position = MenuPosition.valueOf(position.getValue() + 1);
        }
    }

    public void clickUp() {
        if (position != MenuPosition.POKEDEX) {
            position = MenuPosition.valueOf(position.getValue() - 1);
        }
    }

    public void close() {
        exiting = true;
        elapsedTime = 0;
    }

    public void open() {
        display = true;
    }

    public boolean isFinishedClosing() {
        if (exiting) {
          if (x >= 1080) {
              return true;
          }
        }
        return false;
    }

    public boolean isOpen() {
        return display;
    }

    public MenuPosition getPosition() {
        return position;
    }

    public void update(float dt) {
        if (display) {
            if (!exiting && x > STOP_POSITION) {
                elapsedTime += dt;
                if (elapsedTime >= 0.016) {
                    x -= 20;
                    if (x <= STOP_POSITION) {
                        x = STOP_POSITION;
                    }
                }
            } else if (exiting) {
                elapsedTime += dt;
                if (elapsedTime >= 0.016) {
                    x += 20;
                    if (x >= 1080) {
                        initMenu();
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (display) {
            batch.draw(border, x, y, 400, 720);
            batch.draw(selector, x + 15, getSelectorY(), 368, 104);
            drawSelectedIcon(batch);
            drawUnselectedIcons(batch);
        }
    }

    private int getSelectorY() {
        return y + 600 - (position.getValue() * 97);
    }

    private int iconX() {
        return x + 25;
    }

    private int iconY(MenuPosition iconPosition) {
        return y + 600 - (iconPosition.getValue() * 96);
    }

    private void drawSelectedIcon(SpriteBatch batch) {
        if (position == MenuPosition.POKEDEX) {
            batch.draw(pokedex, iconX(), iconY(MenuPosition.POKEDEX), 96, 96);
        } else if (position == MenuPosition.POKEMON) {
            batch.draw(pokemon, iconX(), iconY(MenuPosition.POKEMON), 96, 96);
        } else if (position == MenuPosition.BAG) {
            batch.draw(bag, iconX(), iconY(MenuPosition.BAG), 96, 96);
        } else if (position == MenuPosition.PROFILE) {
            batch.draw(profile, iconX(), iconY(MenuPosition.PROFILE), 96, 96);
        } else if (position == MenuPosition.SAVE) {
            batch.draw(save, iconX(), iconY(MenuPosition.SAVE), 96, 96);
        } else if (position == MenuPosition.OPTIONS) {
            batch.draw(options, iconX(), iconY(MenuPosition.OPTIONS), 96, 96);
        } else if (position == MenuPosition.EXIT) {
            batch.draw(exit, iconX(), iconY(MenuPosition.EXIT), 96, 96);
        }
    }

    private void drawUnselectedIcons(SpriteBatch batch) {
        if (position != MenuPosition.POKEDEX) {
            batch.draw(darkPokedex, iconX(), iconY(MenuPosition.POKEDEX), 96, 96);
        }
        if (position != MenuPosition.POKEMON) {
            batch.draw(darkPokemon, iconX(), iconY(MenuPosition.POKEMON), 96, 96);
        }
        if (position != MenuPosition.BAG) {
            batch.draw(darkBag, iconX(), iconY(MenuPosition.BAG), 96, 96);
        }
        if (position != MenuPosition.PROFILE) {
            batch.draw(darkProfile, iconX(), iconY(MenuPosition.PROFILE), 96, 96);
        }
        if (position != MenuPosition.SAVE) {
            batch.draw(darkSave, iconX(), iconY(MenuPosition.SAVE), 96, 96);
        }
        if (position != MenuPosition.OPTIONS) {
            batch.draw(darkOptions, iconX(), iconY(MenuPosition.OPTIONS), 96, 96);
        }
        if (position != MenuPosition.EXIT) {
            batch.draw(darkExit, iconX(), iconY(MenuPosition.EXIT), 96, 96);
        }
    }

    public void dispose() {
        border.dispose();
        selector.dispose();
        pokedex.dispose();
        darkPokedex.dispose();
        pokemon.dispose();
        darkPokemon.dispose();
        bag.dispose();
        darkBag.dispose();
        profile.dispose();
        darkProfile.dispose();
        save.dispose();
        darkSave.dispose();
        options.dispose();
        darkOptions.dispose();
        exit.dispose();
        darkExit.dispose();
    }
}
