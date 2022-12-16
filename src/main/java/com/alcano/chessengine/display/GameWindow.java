package com.alcano.chessengine.display;

import com.alcano.chessengine.ui.HUD;
import com.alcano.chessengine.resource.AssetPool;
import com.alcano.chessengine.board.BoardDisplay;
import com.alcano.chessengine.util.Input;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GameWindow extends JFrame {

    public static final int WIDTH = 1320, HEIGHT = 935;

    private static final BufferedImage BACKGROUND = AssetPool.loadTexture("textures/background.png");

    public final Canvas canvas;

    private final BoardDisplay boardDisplay;
    private final HUD hud;

    public GameWindow(BoardDisplay boardDisplay, HUD hud) {
        FlatDarculaLaf.setup();

        this.setTitle("Chess Engine");
        this.boardDisplay = boardDisplay;
        this.hud = hud;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        this.canvas = new Canvas();
        this.canvas.setSize(WIDTH, HEIGHT);
        this.canvas.addMouseListener(new Input(this.boardDisplay, this.hud));
        this.add(this.canvas);

        this.pack();

        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    public void render() {
        BufferStrategy bs = this.canvas.getBufferStrategy();
        if (bs == null) {
            this.canvas.createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.drawImage(BACKGROUND, 0, 0, WIDTH, HEIGHT, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        this.boardDisplay.render(g);
        this.hud.render(g);

        bs.show();
        g.dispose();
    }

}
