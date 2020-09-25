package com.dsi.controller;

import com.dsi.model.bll.BLLException;
import com.dsi.view.PageHubAdmin;

import java.util.Timer;
import java.util.TimerTask;

public class RepeatAction {
    Timer t;

    public RepeatAction() {
        t = new Timer();
        t.schedule(new MonAction(), 0, 1*10000);
    }

    class MonAction extends TimerTask {
        int nbrRepetitions = 3;

        public void run() {
            if (nbrRepetitions > 0) {
                System.out.println("Ca bosse dur!");
                nbrRepetitions--;
            } else {
                System.out.println("Terminé!");
                t.cancel();
            }
        }
    }
    
}//fin class
