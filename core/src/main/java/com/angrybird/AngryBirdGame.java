package com.angrybird;

import com.angrybird.screens.HomeScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AngryBirdGame extends Game {
    public SpriteBatch batch;
    private Music backgroundMusic;
    private float musicVolume = 1.0f; // Default volume level (100%)
    private boolean isMusicPlaying = false; // Track music play state

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Load background music, but don't play it immediately
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/bgm.mp3"));
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

            // If volume is 0, stop the music; if not, ensure music plays if it was previously playing
            if (volume == 0) {
                stopMusic();
            } else if (isMusicPlaying) {
                playMusic();
            }
        }
    }

    public void playMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying() && musicVolume > 0) {
            backgroundMusic.play();
            isMusicPlaying = true;
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
            isMusicPlaying = false;
        }
    }

    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
            isMusicPlaying = false;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}
