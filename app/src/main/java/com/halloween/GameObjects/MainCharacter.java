package com.halloween.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

import java.util.ArrayList;
import java.util.Random;

public class MainCharacter {
    private static MainCharacter instance = null;
    public boolean isJumping, isAttacking, isAlive, isActive, isInvincible, isInUltimateForm, isFullyTransformToUltimateForm;
    private Animation currentAnimation, idleAnimation, walkAnimation, jumpAnimation, dieAnimation;
    private Animation idleAnimationUlti, walkAnimationUlti, jumpAnimationUlti, appearAnimationUtil, disappearAnimationUlti, attackAnimationUlti;
    private Animation[] attackAnimation;
    private PointF position, velocity;
    private int currentScore, currentHealth, currentMana, attackPower;
    private boolean isFlip, allowLeft, allowRight;
    private Paint paint, normalPaint, redPaint;
    private Random rand = new Random();
    private RectF attackRect;
    private long invincibleStartTime, blinkTime, jumpTime;
    MediaPlayer hitSound;

    private MainCharacter(int positionX, int positionY) {
        this.loadAnimation();
        this.resetAllValue();
        this.position = new PointF(positionX, positionY);
        this.normalPaint = new Paint();
        this.redPaint = new Paint();
        this.redPaint.setColorFilter(new LightingColorFilter(Color.argb(255, 100, 20, 20), 0));
        this.redPaint.setColor(Color.RED);
        this.attackRect = new RectF();
        this.paint = this.normalPaint;
        this.hitSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.hit_sound);
    }

    public static MainCharacter getInstance() {
        if (MainCharacter.instance == null)
            MainCharacter.instance = new MainCharacter(100, 100);
        return MainCharacter.instance;
    }

    public static MainCharacter getInstance(int positionX, int positionY) {
        if (MainCharacter.instance == null)
            MainCharacter.instance = new MainCharacter(positionX, positionY);
        else MainCharacter.instance.setPosition(positionX, positionY);
        return MainCharacter.instance;
    }

    public void resetAllValue() {
        this.currentAnimation = idleAnimation;
        this.isFlip = true;
        this.currentAnimation.flip(true);
        this.velocity = new PointF(0, 0);
        this.currentScore = this.currentMana = 0;
        this.currentHealth = Constants.MAX_HEALTH_MAIN_CHARACTER;
        this.attackPower = Constants.MAIN_CHARACTER_ATTACK_POWER;
        this.isActive = this.isAlive = true;
        this.isJumping = this.isAttacking = this.isInvincible = this.isInUltimateForm = this.isFullyTransformToUltimateForm = false;
    }

    private void loadAnimation() {
        float SCALE = 1.5f;
//        this.idleAnimation = new Animation(R.drawable.main_character_idle_103x97x8, 103*SCALE,97*SCALE,8, 100);
        this.idleAnimation = new Animation(R.drawable.main_character_idle_103x97x8, 103 * SCALE, 97 * SCALE, 8, 100, new PointF(60 * SCALE, 26 * SCALE));
        this.walkAnimation = new Animation(R.drawable.main_character_walk_103x97x4, 103 * SCALE, 97 * SCALE, 4, 100, new PointF(60 * SCALE, 26 * SCALE));
        this.jumpAnimation = new Animation(R.drawable.main_character_jump_1, 103 * SCALE, 97 * SCALE, 1, 300, new PointF(60 * SCALE, 26 * SCALE));
        this.dieAnimation = new Animation(R.drawable.main_character_die_12, 103 * SCALE, 97 * SCALE, 12, 100, new PointF(47 * SCALE, 26 * SCALE));
        this.attackAnimation = new Animation[4];
        this.attackAnimation[0] = new Animation(R.drawable.main_character_attack_6, 103 * SCALE, 97 * SCALE, 6, 75, new PointF(SCALE * 60, SCALE * 26));
        this.attackAnimation[1] = new Animation(R.drawable.main_character_attack2_4, 120 * SCALE, 73 * SCALE, 4, 75, new PointF(SCALE * 60, SCALE * 0), new PointF(SCALE * 17, SCALE * 2));
        this.attackAnimation[2] = new Animation(R.drawable.main_character_attack4_4, 131 * SCALE, 85 * SCALE, 4, 25, new PointF(SCALE * 54, SCALE * 13), new PointF(SCALE * 35, 0));
        this.attackAnimation[3] = new Animation(R.drawable.main_character_attack3_7, 180 * SCALE, 102 * SCALE, 7, 75, new PointF(SCALE * 84, SCALE * 14), new PointF(SCALE * 56, SCALE * 13));

        float SCALE2 = SCALE / 1.86597938144f;
        this.idleAnimationUlti = new Animation(R.drawable.main_character_ulti_idle_139x181x8, 139 * SCALE2, 181 * SCALE2, 8, 100, new PointF(29 * SCALE2, 44 * SCALE2), new PointF(45 * SCALE2, 0));
        this.walkAnimationUlti = new Animation(R.drawable.main_character_ulti_walk_133x179x8, 133 * SCALE2, 179 * SCALE2, 8, 100, new PointF(24 * SCALE2, 44 * SCALE2), new PointF(45 * SCALE2, 0));
        this.jumpAnimationUlti = idleAnimationUlti;
        this.attackAnimationUlti = new Animation(R.drawable.main_character_ulti_attack_772x348x20_488x135_564x265, 772 * SCALE2, 348 * SCALE2, 20, 40, new PointF(492 * SCALE2, 140 * SCALE2), new PointF(214 * SCALE2, 83 * SCALE2));
        this.appearAnimationUtil = new Animation(R.drawable.main_character_ulti_appear_215x247x7, 215 * SCALE2, 247 * SCALE2, 7, 75, new PointF(73 * SCALE2, 69 * SCALE2), new PointF(63 * SCALE2, 26 * SCALE2));
        this.disappearAnimationUlti = new Animation(R.drawable.main_character_ulti_disappear_215x247x7, 215 * SCALE2, 247 * SCALE2, 7, 75, new PointF(73 * SCALE2, 69 * SCALE2), new PointF(63 * SCALE2, 26 * SCALE2));
    }

    public void draw(Canvas canvas, Paint paint) {
        this.currentAnimation.draw(canvas, new PointF(Constants.getRelativeXPosition(this.position.x), this.position.y), paint);
    }


    public void draw(Canvas canvas) {
//        RectF sur = currentAnimation.getSurroundingBox(position);
//        canvas.drawRect(Constants.getRelativeXPosition(sur.left), sur.top, Constants.getRelativeXPosition(sur.right), sur.bottom, this.paint);
//        RectF atk = getAttackRange();
//        if (atk != null)
//            canvas.drawRect(Constants.getRelativeXPosition(atk.left), atk.top, Constants.getRelativeXPosition(atk.right), atk.bottom, this.redPaint);
        this.currentAnimation.draw(canvas, new PointF(Constants.getRelativeXPosition(this.position.x), this.position.y), this.paint);
    }

    public void update(ArrayList<RectF> boxes) {
        this.updateMovement(boxes);
        this.updateInvincibleState();
        this.updateAnimation();
        this.updateMana();
        Constants.CURRENT_SCORE = this.currentScore;
        if (!this.isActive)
            Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.GAME_OVER;
    }

    private void updateMovement(ArrayList<RectF> boxes) {
        if (this.isAlive) {
            if (Constants.JOYSTICK_TRANSFORM_STATE) {
                Constants.JOYSTICK_TRANSFORM_STATE = false;
                this.isInUltimateForm = true;
            }
            if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.LEFT) {
                this.velocity.x = -Constants.MAIN_CHARACTER_V_X;
                this.allowLeft = true;
            } else if (Constants.CURRENT_JOYSTICK_STATE == Constants.JOYSTICK_STATE.RIGHT) {
                this.velocity.x = Constants.MAIN_CHARACTER_V_X;
                this.allowRight = true;
            } else this.velocity.x = 0;

            if (this.currentAnimation == this.attackAnimationUlti) {
                this.allowLeft = this.allowRight = false;
                this.isAttacking = !this.attackAnimationUlti.isLastFrame();
            } else if (Constants.JOYSTICK_ATK_STATE) {
                this.isAttacking = true;
                this.attackPower = this.isInUltimateForm ? Constants.MAIN_CHARACTER_ULTIMATE_ATTACK_POWER : Constants.MAIN_CHARACTER_ATTACK_POWER;
            } else {
                this.isAttacking = false;
                this.position.y += 10;
            }
            if (!isJumping && Constants.JOYSTICK_JUMP_STATE) {

                this.velocity.y = Constants.MAIN_CHARACTER_V_Y;
                this.position.y += this.velocity.y;
                this.isJumping = true;
//                this.jumpTime = System.nanoTime();
            } else if (isJumping && this.currentAnimation != this.attackAnimationUlti) {
//                if ((System.nanoTime() - this.jumpTime) / 1000000 > Constants.JUMP_TIME){
                this.position.y += this.velocity.y;
                this.velocity.y = Constants.GRAVITY + this.velocity.y;
//                    this.jumpTime = System.nanoTime();
//                }
            }
            this.isJumping = true;
            RectF surroundingBox = this.getSurroundingBox();
            for (RectF box : boxes) {
                if (surroundingBox.bottom > box.top && surroundingBox.top < box.bottom && surroundingBox.bottom - box.top > 20) {
                    if (surroundingBox.right >= box.left && surroundingBox.right - box.left <= 2 * Constants.MAIN_CHARACTER_V_X) {
                        this.position.x = box.left - surroundingBox.width();
                        allowRight = false;
                    } else if (surroundingBox.left <= box.right && box.right - surroundingBox.left <= 2 * Constants.MAIN_CHARACTER_V_X) {
                        this.position.x = box.right;
                        allowLeft = false;
                    }
                }
                if (surroundingBox.right > box.left && surroundingBox.left < box.right) {
                    if (surroundingBox.bottom >= box.top && surroundingBox.top < box.top) {
                        this.position.y = box.top - surroundingBox.height();
                        this.velocity.y = 0;
                        this.isJumping = false;
                        Constants.JOYSTICK_JUMP_STATE = false;
                    } else if (surroundingBox.top < box.bottom && surroundingBox.bottom > box.bottom) {
                        this.position.y = box.bottom;
                        this.velocity.y = 0;
                    }
                }
            }
            if (this.allowLeft || this.allowRight) {
                this.position.x += this.velocity.x;
                this.allowLeft = this.allowRight = false;
            }
            if (surroundingBox.bottom > Constants.SCREEN_HEIGHT * 0.8)
                this.position.y = Constants.SCREEN_HEIGHT * 0.8f - surroundingBox.height();
        } else {
            if (isJumping) {
//                if ((System.nanoTime() - this.jumpTime) / 1000000 > Constants.JUMP_TIME){
                this.position.y += this.velocity.y;
                this.velocity.y = Constants.GRAVITY + this.velocity.y;
//                    this.jumpTime = System.nanoTime();
//                }
            }
            this.isJumping = true;
            RectF surroundingBox = this.getSurroundingBox();
            for (RectF box : boxes) {
                if (surroundingBox.right > box.left + 3 && surroundingBox.left < box.right - 3) {
                    if (surroundingBox.bottom >= box.top && surroundingBox.top < box.top) {
                        this.position.y = box.top - surroundingBox.height();
                        this.velocity.y = 0;
                        this.isJumping = false;
                    }
                }
            }
        }
    }

    private void updateInvincibleState() {
        if (!isInvincible)
            return;
        long elapseTime = System.nanoTime();
        if ((elapseTime - invincibleStartTime) / 1000000 > Constants.INVINCIBLE_TIME) {
            this.isInvincible = false;
            this.paint = normalPaint;
        } else if ((elapseTime - blinkTime) / 1000000 > Constants.BLINK_TIME) {
            this.blinkTime = System.nanoTime();
            if (this.paint == this.normalPaint)
                this.paint = this.redPaint;
            else this.paint = normalPaint;
        }
    }

    private void updateAnimation() {
        if (this.currentHealth <= 0) {
            currentHealth = 0;
            this.isAlive = false;
            this.currentAnimation = this.dieAnimation;
            if (this.dieAnimation.isLastFrame())
                this.isActive = false;
        } else if (this.isInUltimateForm && !this.isFullyTransformToUltimateForm) {
            this.currentAnimation = this.appearAnimationUtil;
            if (this.appearAnimationUtil.isLastFrame()) {
                this.appearAnimationUtil.reset();
                this.isFullyTransformToUltimateForm = true;
            }

        } else if (!this.isInUltimateForm && this.isFullyTransformToUltimateForm) {
            this.currentAnimation = this.disappearAnimationUlti;
            if (this.disappearAnimationUlti.isLastFrame()) {
                this.disappearAnimationUlti.reset();
                this.isFullyTransformToUltimateForm = false;
            }
        } else if (this.isAttacking) {
            if (isInUltimateForm)
                this.currentAnimation = this.attackAnimationUlti;
            else if (
                    this.currentAnimation != this.attackAnimation[0] &&
                            this.currentAnimation != this.attackAnimation[1] &&
                            this.currentAnimation != this.attackAnimation[2] &&
                            this.currentAnimation != this.attackAnimation[3]) {
                this.currentAnimation = this.attackAnimation[rand.nextInt(4)];
            } else if (this.currentAnimation.isLastFrame()) {
                this.currentAnimation.reset();
                this.isAttacking = false;
                this.currentAnimation = this.attackAnimation[rand.nextInt(4)];
            }
        } else if (!this.isJumping && this.velocity.x != 0)
            this.currentAnimation = isInUltimateForm ? this.walkAnimationUlti : this.walkAnimation;
        else if (this.isJumping)
            this.currentAnimation = isInUltimateForm ? this.jumpAnimationUlti : this.jumpAnimation;
        else this.currentAnimation = isInUltimateForm ? this.idleAnimationUlti : this.idleAnimation;

        if (this.velocity.x > 0) {
            this.isFlip = true;
        } else if (this.velocity.x < 0) {
            this.isFlip = false;
        }
        this.currentAnimation.flip(this.isFlip);
        this.currentAnimation.update();
    }

    private void updateMana() {
        Constants.MAIN_CHARACTER_IS_FULL_MANA = this.currentMana >= Constants.MAIN_CHARACTER_MAX_MANA;
        if (this.isInUltimateForm) {
            this.decreaseMana(Constants.MANA_DECREASE_SPEED);
        } else {
            if (this.currentAnimation == this.idleAnimation)
                this.increaseMana(Constants.MANA_INCREASE_SPEED);
        }
    }

    public void decreaseHealth(int damage) {
        if (damage==0) return;
        if (!this.hitSound.isPlaying()){
            this.hitSound.start();
        }
        if (this.isInvincible)
            return;
        else {
            currentHealth -= damage;
            if (currentHealth <= 0)
                this.isAlive = false;
            else {
                this.isInvincible = true;
                this.invincibleStartTime = blinkTime = System.nanoTime();
            }
        }
    }

    public void increaseHealth(int health) {
        this.currentHealth += health;
        if (currentHealth > Constants.MAX_HEALTH_MAIN_CHARACTER)
            currentHealth = Constants.MAX_HEALTH_MAIN_CHARACTER;
    }

    public int getHealthPoint() {
        return this.currentHealth;
    }

    public void decreaseMana(int mana) {
        this.currentMana -= mana;
        if (this.currentMana < 0) {
            this.currentMana = 0;
            this.isInUltimateForm = false;
        }
    }

    public void increaseMana(int mana) {
        this.currentMana += mana;
        if (this.currentMana > Constants.MAIN_CHARACTER_MAX_MANA)
            this.currentMana = Constants.MAIN_CHARACTER_MAX_MANA;
    }

    public int getManaPoint() {
        return this.currentMana;
    }

    public RectF getAttackRange() {
        if (!isAttacking)
            return null;
        int frameIndex = currentAnimation.getCurrentFrameIndex();
        if (isInUltimateForm) {
            if (frameIndex >= 4 && frameIndex <= 15) {
                float top = this.position.y;
                float bottom = this.position.y + this.currentAnimation.getAbsoluteFrameHeight() / 2;
                float left, right;
                float width = currentAnimation.getAbsoluteAnimationWidth();
                if (currentAnimation.isFlip) {
                    right = this.position.x + width + currentAnimation.getAbsoluteOffsetTopLeftX();
                    ;
                    left = right - currentAnimation.getAbsoluteFrameWidth();
                } else {
                    left = this.position.x - currentAnimation.getAbsoluteOffsetTopLeftX();
                    right = left + currentAnimation.getAbsoluteFrameWidth();
                }
                this.attackRect.set(left + width, top, right - width, bottom);
                return this.attackRect;
            } else return null;
        } else {
            if (currentAnimation == attackAnimation[0])
                if (frameIndex < 3)
                    return null;
            if (currentAnimation == attackAnimation[1])
                if ((frameIndex == 1 || frameIndex == 2))
                    return null;
            if (currentAnimation == attackAnimation[3])
                if ((frameIndex > 1 && frameIndex < 5))
                    return null;

            float top = this.position.y - currentAnimation.getAbsoluteOffsetTopLeftY();
            float bottom = top + currentAnimation.getAbsoluteFrameHeight();
            float left, right;
            if (currentAnimation.isFlip) {
                left = this.position.x + currentAnimation.getAbsoluteAnimationWidth();
                right = left + currentAnimation.getAbsoluteOffsetTopLeftX();
            } else {
                left = this.position.x - currentAnimation.getAbsoluteOffsetTopLeftX();
                right = this.position.x;
            }
            this.attackRect.set(left, top, right, bottom);
            return this.attackRect;
        }
    }

    public PointF getCurrentPosition() {
        return this.position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void move(int deltaX) {
        this.position.x += deltaX;
    }

    public RectF getSurroundingBox() {
        return currentAnimation.getSurroundingBox(this.position);
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void increaseScore(int score){
        currentScore+=score;
        currentScore = currentScore > Constants.MAX_SCORE?Constants.MAX_SCORE:currentScore;
    }

}