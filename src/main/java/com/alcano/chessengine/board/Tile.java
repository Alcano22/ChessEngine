package com.alcano.chessengine.board;

import com.alcano.chessengine.core.Engine;
import com.alcano.chessengine.core.Game;
import com.alcano.chessengine.core.Stockfish;
import com.alcano.chessengine.resource.AssetPool;
import com.alcano.chessengine.util.ChessUtils;
import com.alcano.chessengine.util.Sound;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

import java.awt.*;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;

import static com.alcano.chessengine.board.BoardDisplay.TILE_SIZE;

public class Tile {

    private static final Color LIGHT_COLOR = new Color(0xF0D9B5);
    private static final Color DARK_COLOR = new Color(0xB58863);
    private static final Color SELECTED_TINT = new Color(69, 112, 51, 150);
    private static final Color CHECK_TINT = new Color(243, 65, 65, 175);
    private static final BufferedImage LEGAL_MOVE_TEXTURE = AssetPool.loadTexture("textures/legal_move.png");
    private static final BufferedImage LEGAL_MOVE_CAPTURE_TEXTURE = AssetPool.loadTexture("textures/legal_move_capture.png");
    private static final BufferedImage CHECK_TEXTURE = AssetPool.loadTexture("textures/check.png");
    private static final Sound PIECE_MOVE_SOUND = new Sound.Builder("sounds/piece_move.wav").volume(.3f).build();
    private static final Sound PIECE_CAPTURE_SOUND = new Sound.Builder("sounds/piece_capture.wav").volume(.3f).build();
    private static final Sound NOTIFY_SOUND = new Sound.Builder("sounds/notify.wav").volume(.3f).build();

    public final int x, y;

    private final boolean light;
    private final Board board;

    private boolean legalMove;

    public Tile(int x, int y, boolean light) {
        this.x = x;
        this.y = y;
        this.light = light;
        this.board = Engine.get().board;
    }

    public void onMouseClick(int button) {
        if (button == 1) {
            if (Game.selectedTile == null) {
                if (!this.isOccupied() || this.getPiece().getPieceSide() != Side.WHITE) return;

                Game.selectedTile = this;
                return;
            }

            if (Game.selectedTile == this) {
                Game.selectedTile = null;
                return;
            }

            if (this.isOccupied() && this.getPiece().getPieceSide() == this.board.getSideToMove()) {
                Game.selectedTile = this;
                return;
            }

            Move move = new Move(Game.selectedTile.getSquare(), this.getSquare());
            if (this.board.legalMoves().contains(move)) {
                this.board.doMove(move);

                this.playMoveSound(this);

                Game.selectedTile = null;

                if (this.board.isMated() || this.board.isStaleMate()) {
                    NOTIFY_SOUND.playOneShot();
                } else if (this.board.getSideToMove() == Side.BLACK) {
                    Stockfish stockfish = Engine.get().stockfish;
                    String bestMoveStr = stockfish.getBestMove(500);
                    Move bestMove = new Move(bestMoveStr, Side.BLACK);

                    this.board.doMove(bestMove);

                    Tile to = ChessUtils.squareToTile(bestMove.getTo());
                    this.playMoveSound(to);
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(this.light ? LIGHT_COLOR : DARK_COLOR);
        this.fill(g);

        if (this.isSelected()) {
            g.setColor(SELECTED_TINT);
            this.fill(g);

            for (Move move : this.board.legalMoves()) {
                if (move.getFrom() != this.getSquare()) continue;

                Tile moveTo = ChessUtils.squareToTile(move.getTo());
                moveTo.legalMove = true;
            }
        }

        if (this.legalMove) {
            BufferedImage legalMoveTexture = this.isOccupied() ? LEGAL_MOVE_CAPTURE_TEXTURE : LEGAL_MOVE_TEXTURE;
            g.drawImage(legalMoveTexture, this.getScreenX(), this.getScreenY(), TILE_SIZE, TILE_SIZE, null);

            this.legalMove = false;
        }

        Piece piece = this.getPiece();
        if (this.board.isKingAttacked() && piece.getPieceType() == PieceType.KING && piece.getPieceSide() == this.board.getSideToMove()) {
            g.setColor(CHECK_TINT);
            this.fill(g);
        }

        if (piece != Piece.NONE) {
            BufferedImage texture = ChessUtils.getPieceTexture(piece);
            g.drawImage(texture, this.getScreenX(), this.getScreenY(), TILE_SIZE, TILE_SIZE, null);
        }
    }

    private void playMoveSound(Tile tile) {
        if (tile.isOccupied()) {
            PIECE_CAPTURE_SOUND.playOneShot();
        } else {
            PIECE_MOVE_SOUND.playOneShot();
        }
    }

    public void fill(Graphics g) {
        g.fillRect(this.getScreenX(), this.getScreenY(), TILE_SIZE, TILE_SIZE);
    }

    public Square getSquare() {
        return ChessUtils.tileToSquare(this.x, this.y);
    }

    public int getScreenX() {
        return this.x * TILE_SIZE + Engine.get().boardDisplay.getCenterX();
    }

    public int getScreenY() {
        return this.y * TILE_SIZE + Engine.get().boardDisplay.getCenterY();
    }

    public int getCenterX() {
        return this.getScreenX() + TILE_SIZE / 2;
    }

    public int getCenterY() {
        return this.getScreenY() + TILE_SIZE / 2;
    }

    public Piece getPiece() {
        return this.board.getPiece(this.getSquare());
    }

    public boolean isOccupied() {
        return this.getPiece() != Piece.NONE;
    }

    public boolean isSelected() {
        return Game.selectedTile == this;
    }
}
