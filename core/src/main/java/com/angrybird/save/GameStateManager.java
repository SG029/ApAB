package com.angrybird.save;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class GameStateManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE_NAME = "angry_birds_save.dat";
    private static final int TOTAL_LEVELS = 3;

    private Set<Integer> completedLevels;
    private int currentLevel;
    private float musicVolume;
    private boolean isMusicEnabled;

    private class LevelProgress implements Serializable {
        private static final long serialVersionUID = 1L;
        int levelNumber;
        int score;
        int remainingPigs;
        float timeRemaining;

        LevelProgress(int levelNumber) {
            this.levelNumber = levelNumber;
            this.score = 0;
            this.remainingPigs = 0;
            this.timeRemaining = 30f;
        }
    }

    private LevelProgress currentLevelProgress;

    public GameStateManager() {
        completedLevels = new HashSet<>();
        currentLevel = 1;
        musicVolume = 1.0f;
        isMusicEnabled = true;
        currentLevelProgress = new LevelProgress(currentLevel);
    }

    public void saveGameState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_NAME))) {
            out.writeObject(this);
            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
        }
    }

    public static GameStateManager loadGameState() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE_NAME))) {
            GameStateManager loadedState = (GameStateManager) in.readObject();
            System.out.println("Game state loaded successfully.");
            return loadedState;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game state: " + e.getMessage());
            return new GameStateManager();
        }
    }

    public boolean isLevelUnlocked(int levelNumber) {
        if (levelNumber == 1) return true;
        return completedLevels.contains(levelNumber - 1) || (completedLevels.size() >= levelNumber - 1);
    }

    public int getHighestUnlockedLevel() {
        int highestUnlocked = Math.min(completedLevels.size() + 1, TOTAL_LEVELS);
        return Math.max(1, highestUnlocked);
    }

    public void addCompletedLevel(int levelNumber) {
        completedLevels.add(levelNumber);
    }

    public boolean isLevelCompleted(int levelNumber) {
        return completedLevels.contains(levelNumber);
    }

    public void setCurrentLevel(int levelNumber) {
        currentLevel = levelNumber;
        currentLevelProgress = new LevelProgress(levelNumber);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void updateLevelProgress(int score, int remainingPigs, float timeRemaining) {
        if (currentLevelProgress != null) {
            currentLevelProgress.score = score;
            currentLevelProgress.remainingPigs = remainingPigs;
            currentLevelProgress.timeRemaining = timeRemaining;
        }
    }

    public void setMusicSettings(float volume, boolean enabled) {
        this.musicVolume = volume;
        this.isMusicEnabled = enabled;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public int getCompletedLevelsCount() {
        return completedLevels.size();
    }

    public boolean hasProgress() {
        return !completedLevels.isEmpty();
    }
}
