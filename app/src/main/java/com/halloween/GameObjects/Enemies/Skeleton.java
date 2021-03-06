package com.halloween.GameObjects.Enemies;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.R;

public class Skeleton extends Enemy {


    public Skeleton(PointF leftLandMark, PointF rightLandMark) {
        super(Constants.SKELETON_STARTING_HP, leftLandMark, rightLandMark);

        LoadAnimation();

        this.v_x = Constants.SKELETON_V;
        this.v_y = 0;

        currentState = previousState = State.Move;

        currentAnimation = moveAnimation;

        this.surroundingBox = new RectF();

        this.currentPosition = new PointF(rightLandMark.x, rightLandMark.y);

        this.currentAnimation.play();

        this.isMovingForward = false;

        this.damage = Constants.SKELETON_DAMAGE;
        this.attack = Constants.SKELETON_ATTACK;
    }

    public void LoadAnimation() {
        this.moveAnimation = new Animation(R.drawable.skeleton_move_129x127x5, 129 * Constants.SKELETON_SCALE, 127 * Constants.SKELETON_SCALE, 5, 100,
                new PointF(30 * Constants.SKELETON_SCALE, 0), new PointF(30 * Constants.SKELETON_SCALE, 0));
        this.attackAnimation = new Animation(R.drawable.skeleton_attack_215x132x8, 215 * Constants.SKELETON_SCALE,
                132 * Constants.SKELETON_SCALE, 8, 50,
                new PointF(70 * Constants.SKELETON_SCALE, 0), new PointF(70 * Constants.SKELETON_SCALE, 0));
        this.diedAnimation = new Animation(R.drawable.skeleton_died_132x134x12, 132 * Constants.SKELETON_SCALE,
                134 * Constants.SKELETON_SCALE, 12, 100,
                new PointF(33 * Constants.SKELETON_SCALE, 0), new PointF(28 * Constants.SKELETON_SCALE, 0));
        this.hurtAnimation = new Animation(R.drawable.skeleton_hurt_132x134x1, 132 * Constants.SKELETON_SCALE,
                134 * Constants.SKELETON_SCALE, 1, 1000,
                new PointF(33 * Constants.SKELETON_SCALE, 0), new PointF(28 * Constants.SKELETON_SCALE, 0));
    }

    @Override
    public void reset(){
        super.reset();
        this.currentAnimation = moveAnimation;
        this.isMovingForward = false;
        this.currentState = State.Move;
        this.currentAnimation.play();
        this.currentHP = Constants.SKELETON_STARTING_HP;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public RectF getAttackRange() {
        if (currentAnimation != attackAnimation) {
            return null;
        }
        int frameIndex = currentAnimation.getCurrentFrameIndex();
        if (frameIndex >= 4 && frameIndex <= 5) {
            float top = this.currentPosition.y;
            float bottom = this.currentPosition.y + this.currentAnimation.getAbsoluteFrameHeight();
            float left, right;
            float width = currentAnimation.getAbsoluteFrameWidth();
            if (currentAnimation.isFlip) {
                right = this.currentPosition.x + width - (currentAnimation.getAbsoluteOffsetTopLeftX() + currentAnimation.getAbsoluteOffsetBottomRightX()) / 2;
                left = right - Constants.getAbsoluteXLength(currentAnimation.getAbsoluteFrameWidth());
            } else {
                left = this.currentPosition.x - currentAnimation.getAbsoluteOffsetTopLeftX();
                right = left + Constants.getAbsoluteXLength(currentAnimation.getAbsoluteFrameWidth());
            }
            this.attackRect.set(left, top, right, bottom);
            return this.attackRect;
        } else return null;
    }

    @Override
    public void update(RectF playerSurroundingBox) {
        super.update();
        if (isActive) {
            if (currentHP <= 0) {
                isAlive = false;
                ChangeState(State.Died);
            }
//            System.out.println("Current state " + currentState);
//            System.out.println("Current position " + currentPosition);
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
                    if (!IsPlayerInRange(playerSurroundingBox, Constants.SKELETON_ATTACK_DISTANCE_X, Constants.SKELETON_ATTACK_DISTANCE_Y)) {
                        ChangeState(State.Move);
                    } else
                        ChangeState(State.Attack);
                    isMovingForward = playerSurroundingBox.centerX() > getSurroundingBox().centerX();
                    break;
                case Move:
                    ChangeState(State.Move);
                    if (isAlive) {
                        if (IsPlayerInRange(playerSurroundingBox, Constants.SKELETON_ATTACK_DISTANCE_X, Constants.SKELETON_ATTACK_DISTANCE_Y)) {
                            ChangeState(State.Attack);
                        } else {
                            temp.set(playerSurroundingBox.left, playerSurroundingBox.top);
                            if (currentPosition.x <= leftLandMark.x) {
                                isMovingForward = true;
                            } else if (currentPosition.x >= rightLandMark.x) {
                                isMovingForward = false;
                            } else if (IsPlayerInRange(playerSurroundingBox, Constants.SKELETON_FOLLOW_DISTANCE_X, Constants.SKELETON_FOLLOW_DISTANCE_Y)
                                    && IsInReach(temp)) {
                                isMovingForward = playerSurroundingBox.left > getSurroundingBox().left;
                            }
                            if (isMovingForward) {
                                MoveToDestination(rightLandMark, Constants.SKELETON_V);
                            } else {
                                MoveToDestination(leftLandMark, Constants.SKELETON_V);
                            }


                        }
                    }
                    break;
                default:
                    break;
            }
            this.currentAnimation.flip(isMovingForward);
            currentAnimation.update();
        }
    }

    @Override
    public boolean IsInReach(PointF position) {
        if (position.x < rightLandMark.x && position.x > leftLandMark.x) {
            return true;
        }
        return false;
    }


}
