package com.packtpub.mygdx.retromario.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

	public static final AudioManager instance = new AudioManager();

	private Music playingMusic;

	// singleton: prevent instantiation from other classes
	private AudioManager() {
	}

	/**
	 * Play the passed sound
	 * 
	 * @param sound
	 */

	public void play(Sound sound) {
		play(sound, 1);
	}

	/**
	 * Play sound based on volume
	 * 
	 * @param sound
	 * @param volume
	 */

	public void play(Sound sound, float volume) {
		play(sound, volume, 1);
	}

	/**
	 * Play sound according to vol/pitch
	 * 
	 * @param sound
	 * @param volume
	 * @param pitch
	 */
	public void play(Sound sound, float volume, float pitch) {
		play(sound, volume, pitch, 0);
	}

	/**
	 * Play sound if sound not already playing based on params
	 * 
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public void play(Sound sound, float volume, float pitch, float pan) {
		if (!GamePreferences.instance.sound)
			return;
		sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	}

	/**
	 * Plays music, stops whatever is currently playing and replaces it w/ @param
	 * music
	 */
	public void play(Music music) {
		stopMusic();
		playingMusic = music;
		if (GamePreferences.instance.music) {
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
	}

	/**
	 * Stop music method
	 */
	public void stopMusic() {
		if (playingMusic != null)
			playingMusic.stop();
	}

	/**
	 * Method to begin playing/pasuing music
	 */
	public void onSettingsUpdated() {
		if (playingMusic == null)
			return;
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		if (GamePreferences.instance.music) {
			if (!playingMusic.isPlaying())
				playingMusic.play();
		} else {
			playingMusic.pause();
		}
	}
}