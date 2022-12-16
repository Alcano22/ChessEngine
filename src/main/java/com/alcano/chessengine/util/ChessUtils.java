package com.alcano.chessengine.util;

import com.alcano.chessengine.resource.AssetPool;
import com.alcano.chessengine.board.Tile;
import com.alcano.chessengine.core.Engine;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

import java.awt.image.BufferedImage;

public final class ChessUtils {

    private ChessUtils() {}

    public static Tile squareToTile(Square square) {
        char[] chars = square.toString().toCharArray();

        int x = chars[0] - 65;
        int y = 8 - Integer.parseInt(String.valueOf(chars[1]));

        return Engine.get().boardDisplay.getTile(x, y);
    }

    public static Square tileToSquare(int x, int y) {
        char a = (char) (x + 65);
        int b = 8 - y;

        return Square.valueOf("" + a + b);
    }

    public static Square tileToSquare(Tile tile) {
        return tileToSquare(tile.x, tile.y);
    }

    public static String pieceToId(Piece piece) {
        return piece.toString().toLowerCase();
    }

    public static BufferedImage getPieceTexture(Piece piece) {
        return AssetPool.loadTexture("textures\\piece\\" + pieceToId(piece) + ".png");
    }
}
