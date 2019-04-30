package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Zombie extends Enemy {


    public Zombie(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.ZOMBIE_STARTING_HP, leftLandMark, rightLandMark, Constants.ZOMBIE_FOLLOW_DISTANCE, Constants.ZOMBIE_ATTACK_DISTANCE);

        LoadAnimation();

        this.v_x = Constants.ZOMBIE_V_X;
        this.v_y = 0;

        currentState = previousState = State.Move;

        currentAnimation = moveAnimation;

        this.surroundingBox = new RectF();

        this.currentPosition = new PointF(700, 0.8f * Constants.SCREEN_HEIGHT - currentAnimation.frameHeight);

        this.currentAnimation.play();

        this.isMovingForward = false;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.zombie_move_83x97x4, 83 * Constants.ZOMBIE_SCALE, 97 * Constants.ZOMBIE_SCALE, 4, 100,
                new PointF(0, 0), new PointF(0, 0));
        this.attackAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 4, 100, new PointF(0, 0), new PointF(0, 0));
        this.diedAnimation = new Animation(R.drawable.zombie_die_83x97x11, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 11, 100, new PointF(0, 0), new PointF(0, 0));
        this.hurtAnimation = new Animation(R.drawable.attack_83x97x4, 83 * Constants.ZOMBIE_SCALE,
                97 * Constants.ZOMBIE_SCALE, 4, 100, new PointF(0, 0), new PointF(0, 0));
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isActive) {
            if (this.IsInScreen()) {
                this.currentAnimation.draw(canvas, new PointF(this.currentPosition.x - Constants.BACKGROUND_X_AXIS, this.currentPosition.y));
            }
        }
    }

    @Override
    public void update(RectF playerSurroundingBox) {
        super.update();
        if (isActive) {
            if (currentHP <= 0) {
                isAlive = false;
                ChangeState(State.Died);
            }
            switch (currentState) {
                case Died:
                    ChangeState(State.Died);
                    if (this.currentAnimation.isLastFrame())
                        isActive = false;
                    break;
                case Hurt:
                    ChangeState(State.Hurt);
                    break;
                case Attack:
                    if (!IsPlayerInRange(playerSurroundingBox, attackDistance)) {
                        ChangeState(State.Move);
                    } else
                        ChangeState(State.Attack);
                    isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
                    break;
                case Move:
                    ChangeState(State.Move);
                    if (isAlive) {
                        currentHP -= 0.01f;
                        if (isMovingForward) {
                            currentPosition.x = currentPosition.x + this.v_x;
                            currentPosition.y = currentPosition.y + this.v_y;
                        } else {
                            currentPosition.x = currentPosition.x - this.v_x;
                            currentPosition.y = currentPosition.y - this.v_y;
                        }
                        if (currentPosition.x <= leftLandMark.x - Constants.BACKGROUND_X_AXIS) {
                            isMovingForward = true;
                        } else if (currentPosition.x >= rightLandMark.x - Constants.BACKGROUND_X_AXIS) {
                            isMovingForward = false;
                        }
                    }
                    if (IsPlayerInRange(playerSurroundingBox, followDistance)) {
                        if (IsPlayerInRange(playerSurroundingBox, attackDistance))
                            ChangeState(State.Attack);
                        isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
                    }
                    break;
                default:
                    break;
            }
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }
    }




}
