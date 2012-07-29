package com.yewton;

import com.docomostar.ui.Graphics;

abstract class Animation {
    protected boolean alive = false;

    synchronized public final void update(Graphics g) {
	if (alive) {
	    execute(g);
	}
    }

    abstract public void execute(Graphics g);

    protected final void animate() {
	alive = true;
    }

    protected void disanimate() {
	alive = false;
    }
}
