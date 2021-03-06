package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Pair;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.HealthBarMainCharacter;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameContents.Portal;
import com.halloween.GameObjects.Enemies.Dragon;
import com.halloween.GameObjects.Enemies.Enemy;
import com.halloween.GameObjects.Enemies.Gargoyle;
import com.halloween.GameObjects.Enemies.Skeleton;
import com.halloween.GameObjects.Enemies.Zombie;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.GameObjects.Potion;
import com.halloween.GameObjects.Potions.BigHealthPotion;
import com.halloween.GameObjects.Potions.BigManaPotion;
import com.halloween.GameObjects.Trap;
import com.halloween.GameObjects.Traps.CampFire;
import com.halloween.GameObjects.Traps.FireTrap;
import com.halloween.GameObjects.Traps.Spear;
import com.halloween.GameObjects.Traps.SpearHorizontal;
import com.halloween.GameObjects.Traps.SpearVertical;
import com.halloween.R;

import java.util.ArrayList;

public class GraveyardScreen implements GameScreen {

    //for transition
    private boolean isStarting;
    private long startingTime;
    private int startingAlpha;

    private MainCharacter mainCharacter;
    private JoyStick joyStick;
    private HealthBarMainCharacter healthBarMainCharacter;
    private Portal portal;

    private Bitmap background, backgroundBlock, backgroundCloud, backgroundCloudSmall, backgroundMoon;
    private Rect backgroundBlockWhat;
    private RectF backgroundBlockWhere;
    private float backgroundCloudOffset, backgroundCloudSmallOffset;
    private int backgroundCloudCount, backgroundCloudSmallCount;
    private ArrayList<RectF> boxes;
    private Paint paint;
    private RectF tempSurrounding, tempSurroundingMain;
    private RectF tempAttackRange, tempAttackRangeMain;

    private ArrayList<Trap> traps;
    private ArrayList<Enemy> enemies;
    private ArrayList<Potion> potions;

    public GraveyardScreen() {
        this.paint = new Paint();
        this.paint.setAlpha(0);
        this.boxes = new ArrayList<>();
        this.initBoxes();
        // Map Stuff
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.backgroundMoon = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_moon);
        this.backgroundMoon = Bitmap.createScaledBitmap(backgroundMoon, (int) (Constants.SCREEN_HEIGHT * 156 * 0.2 / 137), (int) (Constants.SCREEN_HEIGHT * 0.2), false);

        this.backgroundBlock = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_merged);
        this.backgroundBlock = Bitmap.createScaledBitmap(backgroundBlock, 10000, 578, false);
        this.backgroundBlockWhat = new Rect(0, 0, (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT), backgroundBlock.getHeight());
        this.backgroundBlockWhere = new RectF((float) 0.0, (float) 0.0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        this.backgroundCloud = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_cloud);
        this.backgroundCloud = Bitmap.createScaledBitmap(backgroundCloud, (int) (Constants.SCREEN_HEIGHT * 800 * 0.4 / 320), (int) (Constants.SCREEN_HEIGHT * 0.4), false);
        this.backgroundCloudCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloud.getWidth()) + 2;

        this.backgroundCloudSmall = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.map_cloud_small);
        this.backgroundCloudSmall = Bitmap.createScaledBitmap(backgroundCloudSmall, (int) (Constants.SCREEN_HEIGHT * 1818 * 0.3 / 158), (int) (Constants.SCREEN_HEIGHT * 0.3), false);
        this.backgroundCloudSmallCount = Math.round((float) Constants.SCREEN_WIDTH / this.backgroundCloudSmall.getWidth()) + 2;

        this.joyStick = new JoyStick();
        this.portal = new Portal();

        this.healthBarMainCharacter = new HealthBarMainCharacter();
        this.potions = new ArrayList<>();
        this.initPotions();

        this.traps = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.initEnemies();
        this.initTraps();
        this.tempSurroundingMain = new RectF();
        this.tempSurrounding = new RectF();
        this.tempAttackRange = new RectF();
        this.tempAttackRangeMain = new RectF();
        this.reset();
    }

    private void initPotions() {
        this.potions.add(new BigHealthPotion(new PointF(2765f, 0f), this.boxes));
        this.potions.add(new BigHealthPotion(new PointF(6310f, 0.8f * Constants.SCREEN_HEIGHT * 450f / 578f), this.boxes));
        this.potions.add(new BigHealthPotion(new PointF(7495f, 0f), this.boxes));
        this.potions.add(new BigHealthPotion(new PointF(8465f, 0.8f * Constants.SCREEN_HEIGHT * 120f / 578f), this.boxes));
        this.potions.add(new BigManaPotion(new PointF(8410f, 0.8f * Constants.SCREEN_HEIGHT * 275f / 578f), this.boxes));
    }

    private void initTraps() {
        float scale = Constants.SCREEN_HEIGHT / 578f;

        float spearVerticalTrapScale = scale * 1.0f;
        traps.add(new SpearVertical(new PointF(4990, (float) (0.8 * Constants.SCREEN_HEIGHT - 93 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(5400, (float) (0.8 * Constants.SCREEN_HEIGHT - 93 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(5765, (float) (0.8 * Constants.SCREEN_HEIGHT - 315 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(6237, (float) (0.8 * Constants.SCREEN_HEIGHT - 268 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(6295, (float) (0.8 * Constants.SCREEN_HEIGHT - 268 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));
        traps.add(new SpearVertical(new PointF(8250, (float) (0.8 * Constants.SCREEN_HEIGHT - 93 * spearVerticalTrapScale)), 1000, spearVerticalTrapScale));

        float spearHorizontalTrapScale = scale * 1.0f;
        traps.add(new SpearHorizontal(new PointF(8145, (float) (0.8 * Constants.SCREEN_HEIGHT - 373 * spearHorizontalTrapScale)), 1000, spearHorizontalTrapScale));
        traps.add(new SpearHorizontal(new PointF(8145, (float) (0.8 * Constants.SCREEN_HEIGHT - 193 * spearHorizontalTrapScale)), 1000, spearHorizontalTrapScale));
        traps.add(new SpearHorizontal(new PointF(8145, (float) (0.8 * Constants.SCREEN_HEIGHT - 63 * spearHorizontalTrapScale)), 1000, spearHorizontalTrapScale));

        float campFireTrapScale = scale * 1.0f;
        traps.add(new CampFire(new PointF(7375, (float) (0.8 * Constants.SCREEN_HEIGHT - 120 * campFireTrapScale)), campFireTrapScale));

        float spearTrapScale = scale * 1.0f;
        traps.add(new Spear(new PointF(5620, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(5690, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(5815, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(5890, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(6020, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(6090, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));
        traps.add(new Spear(new PointF(6160, (float) (0.8 * Constants.SCREEN_HEIGHT - 75 * spearTrapScale)), 0, spearTrapScale));

        float fireTrapScale = scale * 1.0f;
        traps.add(new FireTrap(new PointF(7755, (float) (0.8 * Constants.SCREEN_HEIGHT - 201 * fireTrapScale)), 2000, fireTrapScale));
        traps.add(new FireTrap(new PointF(7885, (float) (0.8 * Constants.SCREEN_HEIGHT - 377 * fireTrapScale)), 2000, fireTrapScale));
    }

    private void initEnemies() {
        enemies.add(new Zombie(new PointF(400, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(850, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(200, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(550, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(900, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(1600, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(1710, 0.575f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(2330, 0.575f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(2400, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(2900, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(3500, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(4000, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(4050, 0.65f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(4450, 0.65f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(6830, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(7330, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(7130, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(7750, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(8360, 0.575f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(8700, 0.575f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));
        enemies.add(new Zombie(new PointF(8400, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE),
                new PointF(8800, 0.8f * Constants.SCREEN_HEIGHT - Constants.ZOMBIE_HEIGHT * Constants.ZOMBIE_SCALE)));

        enemies.add(new Gargoyle(new PointF(950, 0.8f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(1400, 0.15f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(2200, 0.6f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(2600, 0.2f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(2500, 0.79f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(3100, 0.79f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(2900, 0.2f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(3350, 0.75f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(5000, 0.65f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(5300, 0.25f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(6500, 0.45f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(7000, 0.44f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(7000, 0.8f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(7600, 0.8f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));
        enemies.add(new Gargoyle(new PointF(8900, 0.8f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE),
                new PointF(9900, 0.7f * Constants.SCREEN_HEIGHT - Constants.GARGOYLE_HEIGHT * Constants.GARGOYLE_SCALE)));

        enemies.add(new Skeleton(new PointF(370, 0.575f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(750, 0.575f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
        enemies.add(new Skeleton(new PointF(2470, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(3900, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
        enemies.add(new Skeleton(new PointF(3450, 0.35f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(3650, 0.35f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
        enemies.add(new Skeleton(new PointF(4110, 0.65f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(4460, 0.65f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
        enemies.add(new Skeleton(new PointF(4580, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(5500, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
        enemies.add(new Skeleton(new PointF(8820, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(9520, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
        enemies.add(new Skeleton(new PointF(9130, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE),
                new PointF(9950, 0.8f * Constants.SCREEN_HEIGHT - Constants.SKELETON_HEIGHT * Constants.SKELETON_SCALE)));
    }

    @Override
    public void reset() {
        this.mainCharacter = MainCharacter.getInstance(200, 600);
        this.mainCharacter.resetAllValue();
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
        for (Potion potion : potions) {
            potion.setActive(true);
        }
    }

    @Override
    public void update() {
//        if (Constants.JOYSTICK_ATK_STATE)
//            this.potions.add(new SmallHealthPotion(new PointF(mainCharacter.getCurrentPosition().x, mainCharacter.getCurrentPosition().y), this.boxes));
//        if (Constants.JOYSTICK_JUMP_STATE)
//            this.potions.add(new SmallManaPotion(new PointF(mainCharacter.getCurrentPosition().x, mainCharacter.getCurrentPosition().y), this.boxes));
        if (Constants.IS_SWITCH_GAME_STATE) {
            Constants.IS_SWITCH_GAME_STATE = false;
            this.isStarting = true;
            this.startingTime = System.currentTimeMillis();
            this.startingAlpha = 0;
            this.paint.setAlpha(this.startingAlpha);
            this.reset();
        }
        if (this.isStarting) {
            long now = System.currentTimeMillis();
            if (now - this.startingTime > 50) {
                this.startingTime = now;
                this.startingAlpha += 10;
                this.paint.setAlpha(Math.min(this.startingAlpha, 255));
                if (startingAlpha >= 255) {
                    this.isStarting = false;
                }
            }
        }

        backgroundCloudOffset += 1;
        backgroundCloudSmallOffset += 1.5f;
        if (backgroundCloudOffset > backgroundCloud.getWidth()) backgroundCloudOffset = 0f;
        if (backgroundCloudSmallOffset > backgroundCloudSmall.getWidth())
            backgroundCloudSmallOffset = 0f;

        joyStick.update();

        mainCharacter.update(boxes);

        tempSurroundingMain = mainCharacter.getSurroundingBox();
        tempAttackRangeMain = mainCharacter.getAttackRange();
        for (Trap trap : traps) {
            tempSurrounding = trap.getSurroundingBox();
            if (tempSurrounding != null) {
                if (tempSurrounding.intersect(tempSurroundingMain))
                    mainCharacter.decreaseHealth(trap.getDamage());
            }
            trap.update();
        }

        for (Potion potion : potions) {
            if (!potion.isActive()) continue;
            if (potion.getSurroundingBox().intersect(tempSurroundingMain)) {
                if (potion.isHealth)
                    mainCharacter.increaseHealth(potion.getVolume());
                else
                    mainCharacter.increaseMana(potion.getVolume());
                potion.setActive(false);
            }
            potion.update();
        }

        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                tempSurrounding = enemy.getSurroundingBox();
                if (tempAttackRangeMain != null) {
                    if (tempAttackRangeMain.intersect(tempSurrounding)) {
                        enemy.decreaseHealth(mainCharacter.getAttackPower());
                    }
                }
                enemy.update(tempSurroundingMain);
                tempAttackRange = enemy.getAttackRange();
                if (tempAttackRange != null) {
                    if (tempAttackRange.intersect(tempSurroundingMain))
                        mainCharacter.decreaseHealth(enemy.getAttack());
                }
                if (tempSurrounding != null) {
                    if (tempSurrounding.intersect(tempSurroundingMain))
                        mainCharacter.decreaseHealth(enemy.getDamage());
                }
                if (!enemy.isAlive()) {
                    if (enemy.currentAnimation.isLastFrame())
                        mainCharacter.increaseMana(Constants.MANA_WHEN_KILL_ENEMY);
                    if (enemy instanceof Zombie)
                        mainCharacter.increaseScore(Constants.ZOMBIE_POINT);
                    else if (enemy instanceof Skeleton)
                        mainCharacter.increaseScore(Constants.SKELETON_POINT);
                    else if (enemy instanceof Gargoyle)
                        mainCharacter.increaseScore(Constants.GARGOYLE_POINT);
                }
            }
        }
//        System.out.println(mainCharacter.getManaPoint());
        healthBarMainCharacter.setNewHealth(mainCharacter.getHealthPoint());
        healthBarMainCharacter.setNewMana(mainCharacter.getManaPoint());
        healthBarMainCharacter.update();

        PointF mainPosition = mainCharacter.getCurrentPosition();
        if (mainPosition.x < Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT) * 0.3) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT) * 0.3);
        } else if (mainPosition.x > Constants.BACKGROUND_X_AXIS + 0.6f * (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT)) {
            Constants.BACKGROUND_X_AXIS = (float) (mainPosition.x - 0.6f * (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT));
        }
        Constants.BACKGROUND_X_AXIS = Math.max(Constants.BACKGROUND_X_AXIS, (float) 0.0);
        Constants.BACKGROUND_X_AXIS = Math.min(Constants.BACKGROUND_X_AXIS, (float) backgroundBlock.getWidth() - (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT));
        this.backgroundBlockWhat.set((int) Constants.BACKGROUND_X_AXIS, (int) 0, (int) (Constants.BACKGROUND_X_AXIS + (Constants.SCREEN_WIDTH * backgroundBlock.getHeight() / Constants.SCREEN_HEIGHT)), backgroundBlock.getHeight());

        Pair<Boolean, PointF> pair = portal.isInSuckingRange(mainCharacter.getSurroundingBox());
        if (pair.first) {
            MainCharacter.getInstance((int) (pair.second.x * Constants.MAIN_CHARACTER_V_X * 0.75f + mainCharacter.getCurrentPosition().x), (int) (mainCharacter.getCurrentPosition().y));
        }
        if (portal.isInTransitionRange(mainCharacter.getSurroundingBox())) {
            Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.BOSS;
            Constants.IS_SWITCH_GAME_STATE = true;
        }
        if (portal.isInRange()) {
            this.portal.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(backgroundMoon, (float) (Constants.SCREEN_WIDTH * 0.7), (float) (Constants.SCREEN_HEIGHT * 0.125), paint);
        for (int i = 0; i < backgroundCloudCount; i++) {
            canvas.drawBitmap(backgroundCloud, -backgroundCloudOffset + backgroundCloud.getWidth() * i, Constants.SCREEN_HEIGHT * 0.8f - backgroundCloud.getHeight(), paint);
        }
        for (int i = 0; i < backgroundCloudSmallCount; i++) {
            canvas.drawBitmap(backgroundCloudSmall, -backgroundCloudSmallOffset + backgroundCloudSmall.getWidth() * i, Constants.SCREEN_HEIGHT * 0.3f - backgroundCloudSmall.getHeight(), paint);
        }
        canvas.drawBitmap(backgroundBlock, backgroundBlockWhat, backgroundBlockWhere, paint);
        if (portal.isInRange()) {
            this.portal.draw(canvas);
        }
//        RectF temp = new RectF();
//        for (RectF box : boxes) {
//            temp.set(Constants.getRelativeXPosition(box.left, Constants.CURRENT_GAME_STATE), box.top, Constants.getRelativeXPosition(box.right, Constants.CURRENT_GAME_STATE), box.bottom);
//            canvas.drawRect(temp, paint);
//        }

//        if (Constants.JOYSTICK_JUMP_STATE)
//            mainCharacter.hurt(1);
//        RectF temp = this.mainCharacter.getAttackRange();
//        if (temp!=null){
//            temp.left = Constants.getRelativeXPosition(temp.left, Constants.GAME_STATE.PLAY);
//            temp.right = Constants.getRelativeXPosition(temp.right, Constants.GAME_STATE.PLAY);
//            canvas.drawRect(temp, paint);
//        }


        this.healthBarMainCharacter.draw(canvas);
        for (Trap trap : traps) {
            trap.draw(canvas);
        }

        for (Potion potion : potions) {
            if (potion.isActive())
                potion.draw(canvas);
        }

        for (Enemy enemy : enemies) {
            if (this.isStarting){
                enemy.draw(canvas, paint);
            }else enemy.draw(canvas);

        }

        if (this.isStarting) {
            this.mainCharacter.draw(canvas, paint);
        } else {
            this.mainCharacter.draw(canvas);
        }

        this.joyStick.draw(canvas);
    }

    @Override
    public void terminate() {
    }

    private void initBoxes() {
        this.boxes.add(new RectF(0, 0, 1, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(10000f, 0, 10010f, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(0, 0.8f * Constants.SCREEN_HEIGHT, 10000, Constants.SCREEN_HEIGHT));
        this.boxes.add(new RectF(382, Constants.SCREEN_HEIGHT * 0.578f, 816, Constants.SCREEN_HEIGHT * 0.656f));
        this.boxes.add(new RectF(650, Constants.SCREEN_HEIGHT * 0.347f, 883, Constants.SCREEN_HEIGHT * 0.424f));
        this.boxes.add(new RectF(1116.3f, Constants.SCREEN_HEIGHT * 0.578f, 1283, Constants.SCREEN_HEIGHT * 0.656f));
        this.boxes.add(new RectF(1249.6f, Constants.SCREEN_HEIGHT * 0.347f, 1416.3f, Constants.SCREEN_HEIGHT * 0.424f));
        this.boxes.add(new RectF(1449.6f, Constants.SCREEN_HEIGHT * 0.116f, 1683, Constants.SCREEN_HEIGHT * 0.193f));
        this.boxes.add(new RectF(1716.296296f, Constants.SCREEN_HEIGHT * 0.57840617f, 2349.62963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(2416.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 2449.62963f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(2549.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 2582.962963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(2649.62963f, Constants.SCREEN_HEIGHT * 0.115681234f, 2882.962963f, Constants.SCREEN_HEIGHT * 0.192802057f));
        this.boxes.add(new RectF(2949.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 2982.962963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(3082.962963f, Constants.SCREEN_HEIGHT * 0.424164524f, 3116.296296f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(3449.62963f, Constants.SCREEN_HEIGHT * 0.347043702f, 3682.962963f, Constants.SCREEN_HEIGHT * 0.424164524f));
        this.boxes.add(new RectF(3782.962963f, Constants.SCREEN_HEIGHT * 0.501285347f, 3949.62963f, Constants.SCREEN_HEIGHT * 0.57840617f));
        this.boxes.add(new RectF(4049.62963f, Constants.SCREEN_HEIGHT * 0.655526992f, 4482.962963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(4049.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 4349.62963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(5082.962963f, Constants.SCREEN_HEIGHT * 0.57840617f, 5116.296296f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(5216.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 5249.62963f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(5349.62963f, Constants.SCREEN_HEIGHT * 0.269922879f, 5382.962963f, Constants.SCREEN_HEIGHT * 0.347043702f));
        this.boxes.add(new RectF(5575f, Constants.SCREEN_HEIGHT * 0.269922879f, 5625f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(5545f, Constants.SCREEN_HEIGHT * 0.45f, 5600f, Constants.SCREEN_HEIGHT * 0.51f));
        this.boxes.add(new RectF(5625f, Constants.SCREEN_HEIGHT * 0.36f, 5655f, Constants.SCREEN_HEIGHT * 0.43f));
        this.boxes.add(new RectF(5775f, Constants.SCREEN_HEIGHT * 0.424164524f, 5825f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(5820f, Constants.SCREEN_HEIGHT * 0.467128027f, 5850f, Constants.SCREEN_HEIGHT * 0.536332179f));
        this.boxes.add(new RectF(5975f, Constants.SCREEN_HEIGHT * 0.269922879f, 6025f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(5935f, Constants.SCREEN_HEIGHT * 0.389273356f, 5980f, Constants.SCREEN_HEIGHT * 0.458477509f));
        this.boxes.add(new RectF(6010f, Constants.SCREEN_HEIGHT * 0.302768166f, 6045f, Constants.SCREEN_HEIGHT * 0.380622837f));
        this.boxes.add(new RectF(6010f, Constants.SCREEN_HEIGHT * 0.553633218f, 6095f, Constants.SCREEN_HEIGHT * 0.6f));
        this.boxes.add(new RectF(6249.62963f, Constants.SCREEN_HEIGHT * 0.501285347f, 6382.962963f, Constants.SCREEN_HEIGHT * 0.57840617f));
        this.boxes.add(new RectF(6366.666667f, Constants.SCREEN_HEIGHT * 0.44344473f, 6400f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(6316.296296f, Constants.SCREEN_HEIGHT * 0.57840617f, 6382.962963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(6416.296296f, Constants.SCREEN_HEIGHT * 0.655526992f, 6549.62963f, Constants.SCREEN_HEIGHT * 0.9f));
        this.boxes.add(new RectF(6382.962963f, Constants.SCREEN_HEIGHT * 0.501285347f, 6416.296296f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(6716.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 7082.962963f, Constants.SCREEN_HEIGHT * 0.501285347f));
        this.boxes.add(new RectF(7149.62963f, Constants.SCREEN_HEIGHT * 0.192802057f, 7182.962963f, Constants.SCREEN_HEIGHT * 0.269922879f));
        this.boxes.add(new RectF(7482.962963f, Constants.SCREEN_HEIGHT * 0.115681234f, 7516.296296f, Constants.SCREEN_HEIGHT * 0.192802057f));
        this.boxes.add(new RectF(8316.296296f, Constants.SCREEN_HEIGHT * -0.642673522f, 8516.296296f, Constants.SCREEN_HEIGHT * 0.115681234f));
        this.boxes.add(new RectF(8516.296296f, Constants.SCREEN_HEIGHT * -0.642673522f, 8549.62963f, Constants.SCREEN_HEIGHT * 0.424164524f));
        this.boxes.add(new RectF(8349.62963f, Constants.SCREEN_HEIGHT * 0.347043702f, 8516.296296f, Constants.SCREEN_HEIGHT * 0.424164524f));
        this.boxes.add(new RectF(8316.296296f, Constants.SCREEN_HEIGHT * 0.347043702f, 8349.62963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(8349.62963f, Constants.SCREEN_HEIGHT * 0.57840617f, 8749.62963f, Constants.SCREEN_HEIGHT * 0.655526992f));
        this.boxes.add(new RectF(8049.62963f, Constants.SCREEN_HEIGHT * 0.115681234f, 8149.62963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7982.962963f, Constants.SCREEN_HEIGHT * 0.269922879f, 8049.62963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7916.296296f, Constants.SCREEN_HEIGHT * 0.424164524f, 7982.962963f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7849.62963f, Constants.SCREEN_HEIGHT * 0.57840617f, 7916.296296f, Constants.SCREEN_HEIGHT * 0.809768638f));
        this.boxes.add(new RectF(7782.962963f, Constants.SCREEN_HEIGHT * 0.732647815f, 7849.62963f, Constants.SCREEN_HEIGHT * 0.809768638f));
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        joyStick.receiveTouch(event);
    }
}