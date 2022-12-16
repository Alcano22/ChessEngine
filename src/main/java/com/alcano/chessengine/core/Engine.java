package com.alcano.chessengine.core;

import com.alcano.chessengine.board.BoardDisplay;
import com.alcano.chessengine.display.FenInputWindow;
import com.alcano.chessengine.display.GameWindow;
import com.alcano.chessengine.ui.HUD;
import com.github.bhlangonijr.chesslib.Board;

public class Engine {

    private static Engine instance;

    public final Board board;
    public final BoardDisplay boardDisplay;
    public final HUD hud;
    public final Stockfish stockfish;
    public final GameWindow window;

    private boolean running;

    private Engine() {
        instance = this;

        this.board = new Board();
        this.board.loadFromFen("1QR5/8/8/K7/8/8/8/6k1 w - - 0 1");
        this.boardDisplay = new BoardDisplay(this.board);
        this.hud = new HUD(this.board);
        this.stockfish = new Stockfish(this.board);
        this.window = new GameWindow(this.boardDisplay, this.hud);

        new Thread(this::render).start();
    }

    public void render() {
        this.running = true;
        while (this.running) {
            if (!FenInputWindow.exists()) {
                this.window.requestFocus();
            }
            this.window.render();
        }
    }

    public void resetBoard() {
        this.startBoard(this.board.getContext().getStartFEN());
    }

    public void startBoard(String fen) {
        this.board.loadFromFen(fen);
    }

    public void stop() {
        this.running = false;
        this.stockfish.stopEngine();
    }

    public static Engine get() {
        return instance;
    }

    public static void main(String[] args) {
        new Engine();
    }

}
