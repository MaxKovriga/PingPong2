package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ball {
    Texture texture;
    int x, y = 100;
    final int VELOCITY = 5;
    int velocityX = VELOCITY, velocityY = VELOCITY;
    int ballStartFrameCounter;
    int ballFlyFrameCounter;
    final int FRAMES_TO_WAIT_BEFORE_BALL_START = 60;
    final int FRAMES_TO_WAIT_BEFORE_SPEEDING_UP_BALL = 100;

    Ball(){
        texture = new Texture("ball_small.png");
    }

    void draw(SpriteBatch batch){
        batch.draw(texture, x, y);
    }

    void move(Paddle paddle) {
        ballStartFrameCounter++;
        if(ballStartFrameCounter >= FRAMES_TO_WAIT_BEFORE_BALL_START) {
            x += velocityX;
            y += velocityY;
            ballFlyFrameCounter++;
        }else {
            x = paddle.x + paddle.texture.getWidth() / 2 - texture.getWidth() / 2;
        }
    }

    void restart(Paddle paddle){
        x = (Gdx.graphics.getWidth() - texture.getWidth()) / 2;
        y = paddle.y + paddle.texture.getHeight();
        velocityY = Math.abs(velocityY);
        ballStartFrameCounter = 0;
    }

    void speedUpIfNeeded(){
        if(ballFlyFrameCounter > FRAMES_TO_WAIT_BEFORE_SPEEDING_UP_BALL){
            if(velocityX > 0){
                velocityX++;
            }else{
                velocityX--;
            }

            if(velocityY > 0){
                velocityY++;
            }else{
                velocityY--;
            }
            ballFlyFrameCounter = 0;
        }
    }

    void free(){
        texture.dispose();
    }
}