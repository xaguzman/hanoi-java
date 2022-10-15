package com.xavierguzman;

import java.awt.*;

public class Disk extends Rectangle {
    private Color startGradientColor;
    private Color endGradientColor;
    public static final int HEIGHT = 25;
    private final int size;


    public Disk(int midX, int y, int size){
        this.height = HEIGHT;
        this.width = 60 + (size * 15);
        this.x = midX - (width / 2);
        this.y = y;
        this.size = size;

        switch(size){
            case 1:
                startGradientColor = Constants.redGradientStart;
                endGradientColor = Constants.redGradientEnd;
                break;
            case 2:
                startGradientColor = Constants.greenGradientStart;
                endGradientColor = Constants.greenGradientEnd;
                break;
            case 3:
                startGradientColor = Constants.blueGradientStart;
                endGradientColor = Constants.blueGradientEnd;
        }


    }

    public void paint(Graphics2D g){
        GradientPaint gradient = new GradientPaint(
            x,
            y,
            startGradientColor,
            x,
            y + height,
            endGradientColor);
        g.setPaint(gradient);
        g.fillRect(x, y, width, height);
    }
}
