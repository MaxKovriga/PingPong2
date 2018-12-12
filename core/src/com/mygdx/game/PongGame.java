package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PongGame extends ApplicationAdapter {
	SpriteBatch batch;
	final int CATCH_BALL_BONUS = 100;
	Ball ball;
	Paddle paddle;
	SoundManager soundManager;
	int score;
	final int INITIAL_LIVES_COUNT = 3;
	int livesCount = INITIAL_LIVES_COUNT;
	BitmapFont font;
	Texture gameOverLogoTexture;
	Texture skyTexture;
	boolean isGameOver = false;
	Button closeBtn, replayBtn;

	@Override
	public void create () {
		batch = new SpriteBatch();
		skyTexture = new Texture("sky_jpeg.jpg");
		soundManager = new SoundManager();
		soundManager.loadSounds();
		font = new BitmapFont();
		font.getData().setScale(5);
		ball = new Ball();
		paddle = new Paddle();
		paddle.restartPaddle();
		ball.restart(paddle);
	}

	@Override
	public void render () {
		if(closeBtn != null && closeBtn.isClicked()){
			System.exit(0);
		}
		if(replayBtn != null && replayBtn.isClicked()){
			restartGame();
		}

		if(!isGameOver) {
			paddle.move();
			ball.move(paddle);
			ball.speedUpIfNeeded();
			collideBall();

			if (ball.y + ball.texture.getHeight() < 0) {
				soundManager.playLoseBallSound();
				livesCount--;

				if (livesCount == 0) {
					isGameOver = true;
					showGameOver();
				} else {
					ball.restart(paddle);
				}
			}
		}

		draw();
	}

	private void showGameOver() {
		gameOverLogoTexture = new Texture("game_over_logo.jpg");
		closeBtn = new Button("close_btn.png");
		closeBtn.x = Gdx.graphics.getWidth() - closeBtn.texture.getWidth();
		closeBtn.y = 25;
		replayBtn = new Button("replay_btn.png");
		replayBtn.y = 25;
	}

	private void draw() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(skyTexture, 0, 0);
		ball.draw(batch);
		paddle.draw(batch);
		if(gameOverLogoTexture != null){
			batch.draw(gameOverLogoTexture, (Gdx.graphics.getWidth() - gameOverLogoTexture.getWidth()) / 2,
					(Gdx.graphics.getHeight() - gameOverLogoTexture.getHeight()) / 2);
		}
		if(closeBtn != null){
			closeBtn.draw(batch);
		}
		if(replayBtn != null){
			replayBtn.draw(batch);
		}
		font.draw(batch, "Score: " + score + "  Lives: " + livesCount, 0, Gdx.graphics.getHeight());
		if(isGameOver){
			font.draw(batch, "Coded by Max", 100, 500);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		ball.free();
		paddle.free();
		soundManager.unloadSounds();
		font.dispose();
		if(gameOverLogoTexture != null){
			gameOverLogoTexture.dispose();
		}
		if(closeBtn != null){
			closeBtn.free();
		}
		if(replayBtn != null){
			replayBtn.free();
		}
		skyTexture.dispose();
	}

	void collideBall(){
		//мяч отскакивает от правой стенки
		if(ball.x >= Gdx.graphics.getWidth() - ball.texture.getWidth()){
			ball.velocityX = -ball.velocityX;
			soundManager.playRandomBounceSound();
		}

		//мяч отскакивает от верхней стенки
		if(ball.y >= Gdx.graphics.getHeight() - ball.texture.getHeight()){
			ball.velocityY = -ball.velocityY;
			soundManager.playRandomBounceSound();
		}

		//мяч отскакивает от левой стенки
		if(ball.x < 0){
			ball.velocityX = -ball.velocityX;
			soundManager.playRandomBounceSound();
		}

		//мяч отскакивает от верха битки
		if(ball.x + ball.texture.getWidth() / 2 >= paddle.x
				&& ball.x + ball.texture.getWidth() / 2 < paddle.x + paddle.texture.getWidth()){
			if(ball.y < paddle.y + paddle.texture.getHeight() && ball.y > paddle.y - ball.texture.getHeight()){
				ball.velocityY = -ball.velocityY;
				score += CATCH_BALL_BONUS * Math.abs(ball.velocityX);
				soundManager.playRandomBounceSound();
				return;
			}
		}
		//мяч отскакивает от левого края битки
		if(ball.x > paddle.x - ball.texture.getWidth() && ball.x < paddle.x - ball.texture.getWidth() / 2 + 1){
			if(ball.y < paddle.y + paddle.texture.getHeight()){
				if(ball.velocityX > 0) {
					ball.velocityX = -ball.velocityX;
					soundManager.playRandomBounceSound();
				}
			}
		}
		//мяч отскакивает от правого края битки
		if(ball.x > paddle.x + paddle.texture.getWidth() - ball.texture.getWidth() / 2 - 1
				&& ball.x < paddle.x + paddle.texture.getWidth()){
			if(ball.y < paddle.y + paddle.texture.getHeight()){
				if(ball.velocityX < 0) {
					ball.velocityX = -ball.velocityX;
					soundManager.playRandomBounceSound();
				}
			}
		}
	}

	void restartGame(){
		isGameOver = false;
		paddle.restartPaddle();
		ball.velocityX = ball.VELOCITY;
		ball.velocityY = ball.VELOCITY;
		ball.restart(paddle);
		livesCount = INITIAL_LIVES_COUNT;
		score = 0;
		gameOverLogoTexture.dispose();
		gameOverLogoTexture = null;
		closeBtn.free();
		closeBtn = null;
		replayBtn.free();
		replayBtn = null;
	}
}