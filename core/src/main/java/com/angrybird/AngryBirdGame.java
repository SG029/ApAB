package com.angrybird;

import com.angrybird.screens.HomeScreen;
import com.angrybird.save.GameStateManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AngryBirdGame extends Game {
    public SpriteBatch batch;
    public Camera camera;
    private Music backgroundMusic;

    private GameStateManager gameStateManager;

    @Override
    public void create() {
        batch = new SpriteBatch();

        gameStateManager = GameStateManager.loadGameState();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm.mp3"));
        backgroundMusic.setLooping(true);

        backgroundMusic.setVolume(gameStateManager.getMusicVolume());

        this.setScreen(new HomeScreen(this));
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public void saveGame() {
        if (gameStateManager != null) {
            gameStateManager.saveGameState();
        }
    }

    public float getMusicVolume() {
        return gameStateManager.getMusicVolume();
    }

    public void setMusicVolume(float volume) {
        gameStateManager.setMusicSettings(volume, volume > 0);

        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);

            if (volume == 0) {
                stopMusic();
            } else {
                playMusic();
            }
        }
    }

    public void playMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying() &&
            gameStateManager.getMusicVolume() > 0) {
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    public void dispose() {
        saveGame();

        batch.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}
