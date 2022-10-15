package com.xavierguzman;

public interface HanoiEventListener {

    void onMoveDiskToRod(int disk, char toRod, char fromRod);

    void onSolved();
}
