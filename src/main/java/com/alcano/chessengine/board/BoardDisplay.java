package com.alcano.chessengine.board;

import com.alcano.chessengine.display.GameWindow;
import com.github.bhlangonijr.chesslib.Board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardDisplay {

    public static final int NUM_TILES = 8;
    public static final int TILE_SIZE = 100;
    public static final int BOARD_SIZE = TILE_SIZE * NUM_TILES;
    public static final int BOARD_OFFSET = -193;

    private static final int BOARD_OUTLINE_THICKNESS = 10;
    private static final Color OUTLINE_COLOR = new Color(0x383838);

    public final List<Tile> tiles = new ArrayList<>();

    private final Board board;

    public BoardDisplay(Board board) {
        this.board = board;

        this.createTiles();
    }

    private void createTiles() {
        boolean light = false;
        for (int y = 0; y < NUM_TILES; y++) {
            light = !light;
            for (int x = 0; x < NUM_TILES; x++) {
                light = !light;
                Tile tile = new Tile(x, y, light);
                this.tiles.add(tile);
            }
        }
    }

    public void render(Graphics2D g) {
        this.renderOutline(g);

        this.tiles.forEach(tile -> tile.render(g));
    }

    private void renderOutline(Graphics2D g) {
        int x = this.getCenterX() - BOARD_OUTLINE_THICKNESS;
        int y = this.getCenterY() - BOARD_OUTLINE_THICKNESS;
        int outlineSize = BOARD_SIZE + BOARD_OUTLINE_THICKNESS * 2;

        g.setColor(OUTLINE_COLOR);
        g.fillRect(x, y, outlineSize, outlineSize);
    }

    public Tile getTile(int x, int y) {
        for (Tile tile : this.tiles) {
            if (x != tile.x || y != tile.y) continue;

            return tile;
        }

        return null;
    }

    public int getCenterX() {
        return GameWindow.WIDTH / 2 - BOARD_SIZE / 2 + BOARD_OFFSET;
    }

    public int getCenterY() {
        return GameWindow.HEIGHT / 2 - BOARD_SIZE / 2;
    }

}
