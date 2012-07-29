package com.yewton;

import java.util.Vector;

import com.docomostar.ui.Canvas;
import com.docomostar.ui.Graphics;

public class Animater implements Runnable {
    private Vector animations = new Vector();
    private Canvas cvs;

    public Animater(Canvas cvs) {
	this.cvs = cvs;
    }

    public void addAnimation(Animation a) {
	animations.addElement(a);
    }

    public void run() {
	for (;;) {
	    try {
		Graphics g = cvs.getGraphics();
		g.lock();
		for (int i = 0; i < animations.size(); ++i) {
		    ((Animation) animations.elementAt(i)).update(g);
		}
		g.unlock(true);
		cvs.repaint();
		Thread.sleep(33);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

}
