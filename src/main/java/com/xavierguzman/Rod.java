package com.xavierguzman;

import java.awt.*;

public class Rod extends Rectangle {

    private final char label;
    private final GradientPaint gradient;
    private final String caption;
    private int diskCount;

    public Rod(int x, char label){
        super(x, 110, 25, 165);
        this.label = label;
        this.caption = "Rod " + label;
        gradient = new GradientPaint(x, y,
                Constants.woodGradientStart,
                x + width,
                y,
                Constants.woodGradientEnd);
    }

    public void paint(Graphics2D g){
        g.setPaint(gradient);
        g.fillRect(x, y, width, height);

        g.setColor(Color.white);
        g.drawString(caption, x - 15, y + height + 18);
    }

    public void setDiskCount(int i) {
        this.diskCount = i;
    }

    public void addDisk(){
        setDiskCount(diskCount + 1);
    }

    public void removeDisk(){
        setDiskCount( diskCount - 1);
    }

    public int getBottomY(){
        return (int) (getMaxY() - (diskCount * Disk.HEIGHT));
    }
}
