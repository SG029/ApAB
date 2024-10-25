package com.angrybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.angrybird.screens.HomeScreen;

public class AngryBirdGame extends Game {
    public SpriteBatch batch;
    private Music backgroundMusic;
    private float musicVolume = 1.0f; // Default volume level (100%)

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load background music, but don't play it immediately
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("bgm.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(musicVolume);

        // Start the game with HomeScreen
        this.setScreen(new HomeScreen(this));
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume); // Apply volume to the music
        }
    }

    public void playMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}
