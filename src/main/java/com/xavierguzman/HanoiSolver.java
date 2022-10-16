package com.xavierguzman;

import java.util.ArrayList;

public class HanoiSolver {

    ArrayList<HanoiEventListener> listeners = new ArrayList<>();

    public void addListener(HanoiEventListener listener){
        listeners.add(listener);
    }
    public void towerOfHanoi(int n, char from_rod, char to_rod, char aux_rod)
    {
        if (n == 0)
        {
            return;
        }
        towerOfHanoi(n-1, from_rod, aux_rod, to_rod);
//        System.out.println("Move disk " + n + " from rod " +  from_rod + " to rod " + to_rod);
        for (HanoiEventListener listener: listeners) {
            listener.onMoveDiskToRod(n, to_rod, from_rod);
        }
        towerOfHanoi(n-1, aux_rod, to_rod, from_rod);
    }

    public void solve(int disksNumber){
        towerOfHanoi(disksNumber, 'A', 'C', 'B');

        for (HanoiEventListener listener: listeners) {
            listener.onSolved();
        }
    }

}
