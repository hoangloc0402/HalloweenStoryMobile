package com.halloween;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static Activity MAIN_ACTIVITY;
    public static enum GAME_STATE {MAIN_MENU, PLAY, PAUSE, GAME_OVER, BOSS, WIN}
    public static GAME_STATE CURRENT_GAME_STATE = GAME_STATE.MAIN_MENU;
    public static boolean isInGraveyard;
    public static boolean IS_SWITCH_GAME_STATE = false;
    public static enum JOYSTICK_STATE {LEFT, RIGHT, MIDDLE}
    public static JOYSTICK_STATE CURRENT_JOYSTICK_STATE = JOYSTICK_STATE.MIDDLE;
    public static boolean JOYSTICK_ATK_STATE = false;
    public static boolean JOYSTICK_JUMP_STATE = false;
    public static boolean JOYSTICK_TRANSFORM_STATE = false;

    public static float BACKGROUND_X_AXIS = 0.0f;

    public static final int MAX_HEALTH_BOSS = 1000;
    public final static int INVINCIBLE_TIME_ENEMY = 500;
    public final static int INVINCIBLE_TIME = 500;
    public final static int ZOMBIE_ATTACK = 50;
    public final static int ZOMBIE_DAMAGE = 25;
    public final static float ZOMBIE_STARTING_HP = 150;
    public final static int ZOMBIE_POINT = 200;
    public final static int ZOMBIE_V = 1;
    public final static float ZOMBIE_SCALE = 1.3f;
    public final static float ZOMBIE_FOLLOW_DISTANCE_X = 200*ZOMBIE_SCALE;
    public final static float ZOMBIE_FOLLOW_DISTANCE_Y = 20*ZOMBIE_SCALE;
    public final static float ZOMBIE_ATTACK_DISTANCE_X = 10 *ZOMBIE_SCALE;
    public final static float ZOMBIE_ATTACK_DISTANCE_Y = 10 *ZOMBIE_SCALE;
    public final static float ZOMBIE_HEIGHT = 97;

    public final static int SKELETON_ATTACK = 100;
    public final static int SKELETON_DAMAGE = 25;
    public final static float SKELETON_STARTING_HP = 250;
    public final static int SKELETON_POINT = 300;
    public final static int SKELETON_V = 2;
    public final static float SKELETON_SCALE = 1.3f;
    public final static float SKELETON_FOLLOW_DISTANCE_X = 200*SKELETON_SCALE;
    public final static float SKELETON_FOLLOW_DISTANCE_Y = 20*SKELETON_SCALE;
    public final static float SKELETON_ATTACK_DISTANCE_X = 20 *SKELETON_SCALE;
    public final static float SKELETON_ATTACK_DISTANCE_Y = 20 *SKELETON_SCALE;
    public final static float SKELETON_HEIGHT = 127;

    public final static int GARGOYLE_ATTACK= 25;
    public final static int GARGOYLE_DAMAGE = 25;
    public final static float GARGOYLE_STARTING_HP = 100;
    public final static int GARGOYLE_POINT = 150;
    public final static float GARGOYLE_V = 3;
    public final static float GARGOYLE_SCALE = 1.5f;
    public final static float GARGOYLE_FOLLOW_DISTANCE = 4000000;
    public final static float GARGOYLE_ATTACK_DISTANCE = 10000;
    public final static float GARGOYLE_HEIGHT = 118;

    public final static int PHANTOM_ATTACK = 20;
    public final static int PHANTOM_DAMAGE = 10;
    public final static float PHANTOM_STARTING_HP = 30;
    public final static int PHANTOM_POINT = 100;
    public final static float PHANTOM_V = 2;
    public final static float PHANTOM_SCALE = 1.5f;
    public final static float PHANTOM_ATTACK_DISTANCE_X = 20 *PHANTOM_SCALE;
    public final static float PHANTOM_ATTACK_DISTANCE_Y = 20 *PHANTOM_SCALE;
    public final static float PHANTOM_HEIGHT = 91;
    public final static int MAX_PHANTOM_COUNT = 2;

    public final static int DRAGON_ATTACK = 150;
    public final static int DRAGON_DAMAGE = 75;
    public final static float DRAGON_STARTING_HP = 1000;
    public final static int DRAGON_POINT = 1000;
    public final static float DRAGON_V = 4;
    public final static float DRAGON_SCALE = 2f;
    public final static float DRAGON_FOLLOW_DISTANCE_X = 200*DRAGON_SCALE;
    public final static float DRAGON_FOLLOW_DISTANCE_Y = 200*DRAGON_SCALE;
    public final static float DRAGON_ATTACK_DISTANCE_X = 15 *DRAGON_SCALE;
    public final static float DRAGON_ATTACK_DISTANCE_Y = 15 *DRAGON_SCALE;
    public final static float BULLET_V = 8;
    public final static float BULLET_SCALE = 0.9f;
    public final static int BULLET_DAMAGE = 40;

    public static final float MAIN_CHARACTER_V_X = 5f;
    public static final float MAIN_CHARACTER_V_Y = -50f;
    public static final float GRAVITY = 2.5f;
    public static final int  MAIN_CHARACTER_ATTACK_POWER = 50;
    public static final int  MAIN_CHARACTER_ULTIMATE_ATTACK_POWER = 80;
    public static final int MAIN_CHARACTER_MAX_MANA = 10000;
    public static boolean MAIN_CHARACTER_IS_FULL_MANA = false;
    public static final int MANA_INCREASE_SPEED = 1;
    public static final int MANA_DECREASE_SPEED = 25;
    public static final int MANA_WHEN_KILL_ENEMY = 200;
    public static final int MAX_HEALTH_MAIN_CHARACTER = 1000;
    public static final long INVINCIBLE_TIME_DRAGON = 1500;
    public static final long BLINK_TIME = 150;
    public static final int MAX_SCORE = 3000;
    public static int CURRENT_SCORE = 0;
    public static final int SMALL_HEALTH_POTION_VOLUME = 100;
    public static final int BIG_HEALTH_POTION_VOLUME = 500;
    public static final int SMALL_MANA_POTION_VOLUME = 100;
    public static final int BIG_MANA_POTION_VOLUME = 500;

    public static final long BULLET_WAIT_TIME = 3000;

//    public static final long JUMP_TIME = 16;

    public static final int backgroundMapAssetHeight = 578;
    public static final int backgroundBossMapAssetHeight = 780;

    public static final int FIRE_TRAP_DAMAGE = 50;
    public static final int CAMP_FIRE_DAMAGE = 10;
    public static final int SPEAR_DAMAGE = 300;
    public static final int SPEAR_HORIZONTAL_DAMAGE = 30;
    public static final int SPEAR_VERTICAL_DAMAGE = 30;

    public  static  final int HEALTH_POTION_PROB = 20;
    public  static  final int MANA_POTION_PROB = 30;

    public static final int BG_SONG_VOLUME = 50;



    public static float getRelativeXPosition(float x, GAME_STATE game_state)  {
        return getRelativeXPosition(x);
    }

    public static float getRelativeXPosition(float x) {
        switch (CURRENT_GAME_STATE) {
            case PLAY:
                return (x - BACKGROUND_X_AXIS) * SCREEN_WIDTH / (backgroundMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT);
            case BOSS:
                return (x - BACKGROUND_X_AXIS) * SCREEN_WIDTH / (backgroundBossMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT);
            default:
                return -10000;
        }
    }

    public static float getAbsoluteXLength(float x) {
        switch (CURRENT_GAME_STATE) {
            case PLAY:
                return x * (backgroundMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT) / SCREEN_WIDTH;
            case BOSS:
                return x * (backgroundBossMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT) / SCREEN_WIDTH;
            default:
                return 0;
        }
    }
    public static boolean isInScreenRange(float x, float offset, GAME_STATE game_state) {
        return isInScreenRange(x, offset);
    }
    public static boolean isInScreenRange(float x, float offset) {
        GAME_STATE game_state = CURRENT_GAME_STATE;
        float left = getRelativeXPosition(x, game_state);
        float right = getRelativeXPosition(x + offset, game_state);
        switch (game_state) {
            case PLAY:
            case BOSS:
                if (left > 0 || right < SCREEN_WIDTH)
                    return true;
                return false;
            default: return false;
        }
    }
}