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

        int colorIdx = size % 3;

        switch(colorIdx){
            case 0:
                startGradientColor = Constants.redGradientStart;
                endGradientColor = Constants.redGradientEnd;
                break;
            case 1:
                startGradientColor = Constants.greenGradientStart;
                endGradientColor = Constants.greenGradientEnd;
                break;
            case 2:
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

        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(size), (int) (getCenterX() - 5), (int) (getCenterY() + 7));
    }
}
