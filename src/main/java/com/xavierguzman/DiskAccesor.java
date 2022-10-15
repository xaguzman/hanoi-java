package com.xavierguzman;

import aurelienribon.tweenengine.TweenAccessor;

import java.awt.*;

public class DiskAccesor implements TweenAccessor<Disk> {

    public static final int X = 1;
    public static final int Y = 2;
    public static final int XY = 3;

    @Override
    public int getValues(Disk target, int tweenType, float[] returnValues) {
        System.out.println("tween getValues: " + tweenType);
        switch (tweenType) {

            case X:
                returnValues[0] = target.x;
                return 1;
            case Y:
                returnValues[0] = target.y;
                return 1;
            case XY:
                returnValues[0] = target.x;
                returnValues[1] = target.y;
                return 2;
            default:
                assert false;
                return 0;
        }
    }

    @Override
    public void setValues(Disk target, int tweenType, float[] newValues) {
        System.out.println("tween setValues: " + tweenType);
        switch (tweenType) {
            case X:
                target.x = (int) newValues[0];
                break;
            case Y:
                target.y = (int) newValues[1];
                break;
            case XY:
                target.x = (int) newValues[0];
                target.y = (int) newValues[1];
                break;
            default:
                assert false;
                break;
        }
    }
}
