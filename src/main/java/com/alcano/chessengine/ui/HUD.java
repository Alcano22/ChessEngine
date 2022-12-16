package com.alcano.chessengine.ui;

import com.alcano.chessengine.display.FenInputWindow;
import com.alcano.chessengine.display.GameWindow;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HUD {

    private static final float HEADER_SIZE = 48f;
    private static final float SUB_HEADER_SIZE = 32f;
    private static final float TEXT_SIZE = 14f;

    public final List<Button> buttons = new ArrayList<>();

    private final Font font;
    private final Board board;

    private Button resetButton;

    public HUD(Board board) {
        Font tryFont = null;
        try {
            tryFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets\\fonts\\Roboto-Bold.ttf")).deriveFont(Font.PLAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.font = tryFont;
        this.board = board;

        this.init();
    }

    private void init() {
        this.resetButton = new Button(this.getCenterX(), 350, 200, 50, "RESET", font.deriveFont(SUB_HEADER_SIZE), onPress -> new FenInputWindow(), false);
        this.buttons.add(this.resetButton);
    }

    public void render(Graphics g) {
        this.renderText(g);
        this.renderButtons(g);
    }

    private void renderText(Graphics g) {
        if (board.isMated()) {
            g.setColor(Color.WHITE);

            g.setFont(this.font.deriveFont(HEADER_SIZE));
            this.drawCenteredString(g, "CHECKMATE", 0, 200);

            Side winningSide = board.getSideToMove().flip();
            String winningSideText = winningSide.toString().toLowerCase().replace("w", "W").replace("b", "B") + " wins";

            g.setFont(this.font.deriveFont(SUB_HEADER_SIZE));
            this.drawCenteredString(g, winningSideText, 0, 275);

            this.resetButton.enabled = true;
        } else if (board.isStaleMate()) {
            g.setColor(Color.WHITE);

            g.setFont(font.deriveFont(HEADER_SIZE));
            this.drawCenteredString(g, "STALEMATE", 0, 200);

            g.setFont(font.deriveFont(SUB_HEADER_SIZE));
            this.drawCenteredString(g, "Nobody wins", 0, 275);
            this.resetButton.enabled = true;
        } else if (board.isInsufficientMaterial()) {
            g.setColor(Color.WHITE);

            g.setFont(font.deriveFont(HEADER_SIZE));
            this.drawCenteredString(g, "INSUFFICIENT", 0, 175);
            this.drawCenteredString(g, "MATERIAL", 0, 225);

            g.setFont(font.deriveFont(SUB_HEADER_SIZE));
            this.drawCenteredString(g, "Nobody wins", 0, 275);
            this.resetButton.enabled = true;
        } else {
            this.resetButton.enabled = false;
        }
    }

    private void renderButtons(Graphics g) {
        this.buttons.forEach(button -> button.render(g));
    }

    private int getCenterX() {
        return GameWindow.WIDTH / 2 + 430;
    }

    private void drawCenteredString(Graphics g, String str, int xOffset, int y) {
        int textX = this.getCenterX() - g.getFontMetrics().stringWidth(str) / 2 + xOffset;
        int textY = y - g.getFontMetrics().getHeight() / 2;

        g.drawString(str, textX, textY);
    }

}
