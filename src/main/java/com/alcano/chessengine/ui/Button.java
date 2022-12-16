package com.alcano.chessengine.ui;

import java.awt.*;

public class Button {

    private static final Color COLOR = new Color(66, 66, 66);
    private static final Color OUTLINE_COLOR = new Color(52, 52, 52);
    private static final int OUTLINE_THICKNESS = 5;

    public final int x, y, width, height;

    private final PressAction action;
    private final Font font;

    public String text;
    public boolean enabled;

    public Button(int x, int y, int width, int height, String text, Font font, PressAction action, boolean enabled) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.font = font;
        this.action = action;
        this.enabled = enabled;
    }

    public Button(int x, int y, int width, int height, String text, Font font, PressAction action) {
        this(x, y, width, height, text, font, action, true);
    }

    public void render(Graphics g) {
        if (!this.enabled) return;

        g.setColor(OUTLINE_COLOR);
        g.fillRect(this.getTopLeftX(), this.getTopLeftY(), this.width, this.height);

        int outlineX = this.x - this.width / 2 + OUTLINE_THICKNESS;
        int outlineY = this.y - this.height / 2 + OUTLINE_THICKNESS;
        g.setColor(COLOR);
        g.fillRect(outlineX, outlineY, this.width - OUTLINE_THICKNESS * 2, this.height - OUTLINE_THICKNESS * 2);

        g.setColor(Color.WHITE);
        g.setFont(this.font);
        int textX = this.x - g.getFontMetrics().stringWidth(this.text) / 2;
        int textY = this.y + g.getFontMetrics().getHeight() / 3;
        g.drawString(this.text, textX, textY);
    }

    public void onPress(int button) {
        if (!this.enabled) return;

        this.action.onPress(button);
    }

    public int getTopLeftX() {
        return this.x - this.width / 2;
    }

    public int getTopLeftY() {
        return this.y - this.height / 2;
    }

    public int getBottomRightX() {
        return this.getTopLeftX() + this.width;
    }

    public int getBottomRightY() {
        return this.getTopLeftY() + this.height;
    }

}
