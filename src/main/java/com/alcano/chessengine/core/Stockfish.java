package com.alcano.chessengine.core;

import com.github.bhlangonijr.chesslib.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish {

    private static final String EXE_PATH = "assets\\stockfish\\stockfish-15.exe";

    private final Board board;
    private final Runtime runtime;

    private Process engine;
    private BufferedReader engineReader;
    private OutputStreamWriter engineWriter;

    public Stockfish(Board board) {
        this.board = board;
        this.runtime = Runtime.getRuntime();

        this.startEngine();
    }

    private void startEngine() {
        try {
            this.engine = this.runtime.exec(EXE_PATH);
            this.engineReader = new BufferedReader(new InputStreamReader(this.engine.getInputStream()));
            this.engineWriter = new OutputStreamWriter(this.engine.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String command) {
        try {
            this.engineWriter.write(command + "\n");
            this.engineWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutput(int waitTime) {
        StringBuffer buffer = new StringBuffer();
        try {
            Thread.sleep(waitTime);
            this.sendCommand("isready");
            while (true) {
                String text = this.engineReader.readLine();
                if (text.equals("readyok"))
                    break;
                else
                    buffer.append(text + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public String getBestMove(int waitTime) {
        this.sendCommand("position fen " + this.board.getFen());
        this.sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime + 20);

        return this.readBestMove(output, waitTime);
    }

    private String readBestMove(String output, int waitTime) {
        if (!output.contains("bestmove")) {
            try {
                Thread.sleep(waitTime);
                return this.readBestMove(this.getOutput(waitTime + 20), waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return output.split("bestmove ")[1].split(" ")[0];
    }

    public String getBestMove(String fen, int waitTime) {
        this.sendCommand("position fen " + fen);
        this.sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime + 20);

        if (!output.contains("bestmove")) return null;

        return output.split("bestmove ")[1].split(" ")[0];
    }

    public void stopEngine() {
        try {
            sendCommand("quit");
            this.engineReader.close();
            this.engineWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
