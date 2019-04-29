package com.halloween.GameObjects.Enemies;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;

import com.halloween.Animation;
import com.halloween.Constants;
import com.halloween.GameObjects.GameObject;


public class Enemy implements GameObject {

    int HP;
    public Animation currentAnimation;
    Animation diedAnimation;
    Animation moveAnimation;
    Animation attackAnimation;
    Animation defenseAnimation;
    Animation hurtAnimation;
    Animation ultimateAttackAnimation;


    Point currentPosition;
    Point leftLandMark;


    Point rightLandMark;
    Rect surroundingBox;

    //Check if the object is in the playing screen
    public boolean isInScreen(){
//        return Constants.BACKGROUND_X_AXIS <= initPosition.x + currentPosition.x &&
//                initPosition.x + currentPosition.x <= Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH;

        return currentPosition.x + currentAnimation.frameWidth >= Constants.BACKGROUND_X_AXIS && currentPosition.x <= Constants.BACKGROUND_X_AXIS + Constants.SCREEN_WIDTH;
    }

    public Point getCurrentPosition() {
        return currentPosition;
    }

    boolean isAlive, isMovingForward;

    public Enemy(){
        isAlive = true;
    }

    public enum State { Move , Attack, Defense , UltimateAttack, Hurt, Died} //Các state có thể có của Enemy
    protected State currentState, previousState;

    @Override
    public void draw(Canvas canvas){}

    @Override
    public void update(){

    }

    public Rect getSurroundingBox(){
        this.surroundingBox.left = currentPosition.x;
        this.surroundingBox.top =  currentPosition.y;
        this.surroundingBox.right = currentPosition.x + currentAnimation.frameWidth;
        this.surroundingBox.bottom = currentPosition.y + currentAnimation.frameHeight;
        return this.surroundingBox;
    }

    public void ChangeState(State state){
        previousState = currentState;
        currentState = state;

        if (currentState != previousState){
            switch (currentState){
                case Move:
                    currentAnimation = moveAnimation;
                    break;
                case Died:
                    currentAnimation = diedAnimation;
                    break;
                case Attack:
                    currentAnimation = attackAnimation;
                    break;
                case Defense:
                    currentAnimation = defenseAnimation;
                    break;
                case Hurt:
                    currentAnimation = hurtAnimation;
                    break;
                case UltimateAttack:
                    currentAnimation = ultimateAttackAnimation;
                    break;
                default:
                    break;

            }
        }
    }
}