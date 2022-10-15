package com.xavierguzman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {

    public Main() {
        createAndShowUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    /**
     * Here we will create our swing UI as well as initialise and setup our
     * sprites, scene, and game loop and other buttons etc
     */
    private void createAndShowUI() {
//        JFrame frame = new JFrame("MyGame");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        player = new Sprite(/*ImageIO.read(getClass().getResourceAsStream("...."))*/);
//
//        this.scene = new Scene();
//        this.scene.add(player);
//
//        this.addKeyBindings();
//        this.setupGameLoop();
//
//        frame.add(scene);
//        frame.pack();
//        frame.setVisible(true);
//
//        // after setting the frame visible we start the game loop, this could be done in a button or wherever you want
//        this.isRunning = true;
//        this.gameLoop.start();

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