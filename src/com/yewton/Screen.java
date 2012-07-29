package com.yewton;

import com.docomostar.ui.Graphics;

public abstract class Screen {
    Counter c;
    MainCanvas cvs;

    public final void setCanvas(MainCanvas cvs) {
	this.cvs = cvs;
    }

    public void init() {
    }

    abstract public void paint(Graphics g);

    public void keyPressedEvent(int key) {
    }

    public void keyReleasedEvent(int key) {
    }

    public void keyRepeating(int key) {
    }

    public void update(Graphics g) {
    }
}
