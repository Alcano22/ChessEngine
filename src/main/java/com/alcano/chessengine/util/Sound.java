package com.alcano.chessengine.util;

import com.alcano.chessengine.resource.AssetPool;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    private final Clip audioClip;

    private float volume;
    private boolean loop;

    public Sound(Clip audioClip) {
        this.audioClip = audioClip;
        this.audioClip.setLoopPoints(0, this.audioClip.getFrameLength() - 2);
    }

    public void play() {
        this.audioClip.start();
    }

    public void playOneShot() {
        this.audioClip.setFramePosition(0);
        this.play();
    }

    public void stop() {
        this.audioClip.setFramePosition(0);
        this.audioClip.stop();
    }

    public void pause() {
        this.audioClip.stop();
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;

        FloatControl volumeControl = (FloatControl) this.audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        float db = (float) (Math.log(this.volume) / Math.log(10.0) * 20.0);
        volumeControl.setValue(db);

        this.stop();
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;

        if (this.loop) {
            this.audioClip.loop(Integer.MAX_VALUE);
        } else {
            this.audioClip.loop(0);
        }

        this.stop();
    }

    public static class Builder {

        private final Sound sound;

        public Builder(Sound sound) {
            this.sound = sound;
        }

        public Builder(Clip audioClip) {
            this(new Sound(audioClip));
        }

        public Builder(String path) {
            this(AssetPool.loadSound(path));
        }

        public Builder volume(float volume) {
            this.sound.setVolume(volume);
            return this;
        }

        public Builder loop(boolean loop) {
            this.sound.setLoop(loop);
            return this;
        }

        public Sound build() {
            return this.sound;
        }

    }

}
