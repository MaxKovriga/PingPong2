package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Paddle {
    Texture texture;
    int x, y = 100;

    Paddle(){
        texture = new Texture("paddle.bmp");
    }

    void draw(SpriteBatch batch){
        batch.draw(texture, x, y);
    }

    void move() {
        if(Gdx.input.isTouched()){
            x = Gdx.input.getX() - texture.getWidth() / 2;

            //не даём битке уходить за левый край экрана
            if(x < 0){
                x = 0;
            }

            //не даём битке уходить за правый край экрана
            if(x >= Gdx.graphics.getWidth() - texture.getWidth()){
                x = Gdx.graphics.getWidth() - texture.getWidth();
            }
        }
    }

    void restartPaddle(){
        x = (Gdx.graphics.getWidth() - texture.getWidth()) / 2;
    }

    void free(){
        texture.dispose();
    }
}