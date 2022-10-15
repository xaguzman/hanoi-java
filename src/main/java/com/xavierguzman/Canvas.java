package com.xavierguzman;

import aurelienribon.tweenengine.*;

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
    Thread simulation;

    long lastUpdate;

    float fps = 1f / 30f;
    float nextFrameAccum = 0f;

    String message = "";

    ArrayList<Timeline> animationTimelines = new ArrayList<>();

    public Canvas(){
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT ));
        setIgnoreRepaint(true);
        rods = new Rod[] {
                new Rod(138, 'A'),
                new Rod(288, 'B'),
                new Rod(438,'C')
        };

        rods[0].setDiskCount(3);

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
                new Disk((int) rods[0].getCenterX(), (int) (rods[0].getMaxY() - (Disk.HEIGHT * 3)), 1),
                new Disk((int) rods[0].getCenterX(), (int) (rods[0].getMaxY() - (Disk.HEIGHT * 2)), 2),
                new Disk((int) rods[0].getCenterX(), (int) (rods[0].getMaxY() - Disk.HEIGHT), 3)
        };
    }


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
        g2d.drawString(message, 20, 20);
        g2d.dispose();

        //render to component
        g.drawImage(buffer, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }

    public void startSimulation(){
        if (isSimulationRunning)
            return;

        initDisks();
        repaint();

        simulation = new Thread(() -> {
            lastUpdate = System.currentTimeMillis();
            int frameSkipsLeft = 0;

            while (isSimulationRunning) {

                long now = System.currentTimeMillis();
                double ellapsed = (now - lastUpdate) / 1000f;
                lastUpdate = now;
                float delta = Math.min((float)ellapsed, fps);
                nextFrameAccum += delta;

                tweenManager.update(delta);

                repaint();
            }

        });

        simulation.start();
        isSimulationRunning = true;
        solver.solve(3);
    }

    @Override
    public void onMoveDiskToRod(int disk, char toRod, char fromRod) {
        int destRodIndex = toRod - 'A';
        int srcRodIndex = fromRod - 'A';
        int distanceX = Math.abs(toRod - fromRod);
        Rod destRod = rods[destRodIndex];
        Rod srcRod = rods[srcRodIndex];
        Disk d = disks[disk-1];
        int targetY = destRod.getBottomY() - Disk.HEIGHT;
        Timeline newAnimation = Timeline.createSequence()
                .push(Tween.call((int type, BaseTween<?> source) -> {
                    message = "Move disk " + disk + " from rod " + fromRod + " to rod " + toRod;
                }))
                .push(
                        Tween.to(d, DiskAccesor.Y, 0.5f).target(50)
                )
                .push(
                        Tween.to(d, DiskAccesor.X, 0.5f * distanceX).target((float) (destRod.getCenterX() - (d.width /2)))
                )
                .push(
                        Tween.to(d, DiskAccesor.Y, 0.5f).target(targetY)
                ).build();
        destRod.addDisk();
        srcRod.removeDisk();

        animationTimelines.add(newAnimation);
    }

    @Override
    public void onSolved() {
        Timeline timeline = Timeline.createSequence();
        animationTimelines.forEach( x -> timeline.pushPause(1f).push(x));

        timeline.push(Tween.call((int type, BaseTween<?> source) -> {
            isSimulationRunning = false;
        }));
        timeline.start(tweenManager);
    }
}
