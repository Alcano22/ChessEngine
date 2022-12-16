package com.alcano.chessengine.util;

import com.alcano.chessengine.board.BoardDisplay;
import com.alcano.chessengine.board.Tile;
import com.alcano.chessengine.ui.Button;
import com.alcano.chessengine.ui.HUD;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.alcano.chessengine.board.BoardDisplay.TILE_SIZE;

public class Input extends MouseAdapter {

    private final BoardDisplay boardDisplay;
    private final HUD hud;

    public Input(BoardDisplay boardDisplay, HUD hud) {
        this.boardDisplay = boardDisplay;
        this.hud = hud;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point mousePos = e.getPoint();
        for (Tile tile : this.boardDisplay.tiles) {
            if (tile.getScreenX() > mousePos.x ||
                    tile.getScreenY() > mousePos.y ||
                    tile.getScreenX() + TILE_SIZE <= mousePos.x ||
                    tile.getScreenY() + TILE_SIZE <= mousePos.y) continue;

            tile.onMouseClick(e.getButton());
            break;
        }

        for (Button button : this.hud.buttons) {
            if (button.getTopLeftX() > mousePos.x ||
                    button.getTopLeftY() > mousePos.y ||
                    button.getBottomRightX() <= mousePos.x ||
                    button.getBottomRightY() <= mousePos.y) continue;

            button.onPress(e.getButton());
            break;
        }
    }
}
