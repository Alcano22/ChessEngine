package com.alcano.chessengine.resource;

import com.alcano.chessengine.util.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static final Map<String, BufferedImage> TEXTURES = new HashMap<>();
    private static final Map<String, Clip> AUDIO_CLIPS = new HashMap<>();

    public static BufferedImage loadTexture(String path) {
        if (TEXTURES.containsKey(path)) {
            return TEXTURES.get(path);
        }

        try {
            BufferedImage texture = ImageIO.read(new File("assets\\" + path));
            TEXTURES.put(path, texture);
            return texture;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Clip loadAudioClip(String path) {
        if (AUDIO_CLIPS.containsKey(path)) {
            return AUDIO_CLIPS.get(path);
        }

        try {
            Clip audioClip = AudioSystem.getClip();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("assets\\" + path));
            audioClip.open(audioIn);

            AUDIO_CLIPS.put(path, audioClip);
            return audioClip;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Sound loadSound(String path) {
        return new Sound(loadAudioClip(path));
    }

}
