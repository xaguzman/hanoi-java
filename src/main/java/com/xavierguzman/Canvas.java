package com.xavierguzman;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Canvas extends JComponent implements HanoiEventListener {
    private final HanoiSolver solver;
    private boolean isSimulationRunning;

    TweenManager tweenManager;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 400;
    static final int MAX_FRAMESKIP = 10;
    static final int FLOOR_HEIGHT=100;
    static final int BOARD_HEIGHT = 25;
    private Disk[] disks;
    private final Rod[] rods;
    private final BufferedImage buffer;
    private final Font uiFont;

    ArrayList<Timeline> animationTimelines = new ArrayList<>();

    public Canvas() throws IOException {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT ));
//        setIgnoreRepaint(true);
        rods = new Rod[] {
                new Rod(138, 'A'),
                new Rod(288, 'B'),
                new Rod(438,'C')
        };

        initDisks();

        buffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        uiFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        Tween.registerAccessor(Disk.class, new DiskAccesor());
        tweenManager = new TweenManager();

        solver = new HanoiSolver();
        solver.addListener(this);
    }

    private void initDisks(){
        disks = new Disk[]{
                new Disk((int) rods[0].getCenterX(), (int) (rods[0].getMaxY() - Disk.HEIGHT), 3),
                new Disk((int) rods[0].getCenterX(), (int) (rods[0].getMaxY() - (Disk.HEIGHT * 2)), 2),
                new Disk((int) rods[0].getCenterX(), (int) (rods[0].getMaxY() - (Disk.HEIGHT * 3)), 1)
        };
    }

//    public boolean isSimulationRunning() {
//        return isSimulationRunning;
//    }
//
//    public void setSimulationRunning(boolean simulationRunning) {
//        isSimulationRunning = simulationRunning;
//    }

    @Override
    protected void paintComponent(Graphics g) {
        //for smoother rendering, using an intermediate image
        Graphics2D g2d = (Graphics2D) buffer.getGraphics();
        g2d.setFont(uiFont);
        Color clearColor = Color.LIGHT_GRAY;

        //clear the screen
        g2d.setColor(clearColor);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // floor
        g2d.setColor(new Color(83, 132, 63));
        g2d.fillRect(0, SCREEN_HEIGHT-FLOOR_HEIGHT, SCREEN_WIDTH, FLOOR_HEIGHT);

        // board
        int boardHeight = 25;
        GradientPaint floorGradient =
                new GradientPaint(0, SCREEN_HEIGHT - FLOOR_HEIGHT - boardHeight,
                        Constants.woodGradientStart,
                        0,
                        SCREEN_HEIGHT - FLOOR_HEIGHT,
                        Constants.woodGradientEnd);
        g2d.setPaint(floorGradient);
        g2d.fillRect(0, SCREEN_HEIGHT - FLOOR_HEIGHT - boardHeight, SCREEN_WIDTH, boardHeight);

        // rods
        for (Rod r : rods){
            r.paint(g2d);
        }

        for(Disk d : disks){
            d.paint(g2d);
        }


        //render info
        g2d.setColor(Color.GRAY);
        g2d.drawString("Hola Hanoi", 100, 50);
        g2d.dispose();

        //render to component
        g.drawImage(buffer, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }

    public void stop(){
        isSimulationRunning = false;
    }

    public void startSimulation(){
        if (isSimulationRunning)
            return;

        initDisks();
        repaint();
        isSimulationRunning = true;
        solver.solve(3);
    }

    public void run() {
//        requestFocusInWindow();

        long lastUpdate = System.nanoTime();
        float nextRenderTime = 0;
        long renderTime = 1000000000 / 30;
        int frameSkipsLeft = 0;
        Color clearColor = Color.LIGHT_GRAY;

        while (isSimulationRunning) {
            long now = System.nanoTime();
            float delta = (now - lastUpdate) / 1000000000f; //change the time to milliseconds isntead of nano seconds.
            lastUpdate = now;

            delta = Math.min(delta,  1 / 60f);

            tweenManager.update(delta);
            nextRenderTime -= delta;
            if ( nextRenderTime <= 0  || frameSkipsLeft == 0){
//                draw();
                nextRenderTime = now + renderTime;
                frameSkipsLeft = MAX_FRAMESKIP;
            }else{
                frameSkipsLeft--;
            }

            if (tweenManager.size() == 0 )
                stop();
        }
    }

    @Override
    public void onMoveDiskToRod(int disk, char toRod, char fromRod) {
        int toRodIndex = toRod - 'A';
        Rod r = rods[toRodIndex];
        Disk d = disks[disk-1];
        int targetY = (int) r.getMaxY();
        Timeline newAnimation = Timeline.createSequence()
                .push(
                        Tween.to(d, DiskAccesor.Y, 0.5f).target(50)
                )
                .push(
                        Tween.to(d, DiskAccesor.X, 0.5f).target((float) (r.getCenterX() - d.width))
                )
                .push(
                        Tween.to(d, DiskAccesor.Y, 0.5f).target(targetY)
                ).build();

        animationTimelines.add(newAnimation);
    }

    @Override
    public void onSolved() {
        animationTimelines.forEach( x -> tweenManager.add(x));
    }
}
