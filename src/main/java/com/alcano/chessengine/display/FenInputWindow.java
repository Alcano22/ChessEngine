package com.alcano.chessengine.display;

import com.alcano.chessengine.core.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class FenInputWindow extends JFrame {

    private static final int WIDTH = 600, HEIGHT = 100;

    private static List<FenInputWindow> instances = new ArrayList<>();

    public FenInputWindow() {
        super("Starting FEN");

        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                instances.remove(this);
            }
        });

        GridBagLayout layout = new GridBagLayout();
        layout.setConstraints(this, new GridBagConstraints());
        this.setLayout(layout);

        JTextField fenField = new JTextField();
        fenField.setBounds(0, 0, WIDTH, 50);
        fenField.setText("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        this.add(fenField);

        JButton startButton = new JButton("Start");
        startButton.setBounds(0, 50, 200, 50);
        startButton.addActionListener(action -> {
            Engine.get().startBoard(fenField.getText());
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        this.add(startButton);

        this.setLocationRelativeTo(null);

        instances.add(this);

        this.setVisible(true);

        this.requestFocus();
    }

    public static boolean exists() {
        return !instances.isEmpty();
    }
}
