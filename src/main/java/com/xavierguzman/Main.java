package com.xavierguzman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Canvas canvas = new Canvas();
        JFrame window = new JFrame();
        window.setLayout(new BorderLayout(5, 0));
        window.add(canvas, BorderLayout.CENTER);
        window.add(new InfoPanel(canvas), BorderLayout.WEST);
        window.setTitle("Hannoi Towers Visualization - Xavier Guzman");
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    public static class InfoPanel extends JPanel{

        public InfoPanel(final Canvas canvas){
            JButton start = new JButton("Play Simulation");
            start.addActionListener(e -> canvas.startSimulation());

            add(start);
        }
    }
}