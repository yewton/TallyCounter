package com.yewton;

import com.docomostar.StarApplication;
import com.docomostar.ui.Canvas;
import com.docomostar.ui.Display;
import com.docomostar.ui.Frame;
import com.docomostar.ui.Graphics;
import com.docomostar.util.TimeKeeper;

public class MainCanvas extends Canvas implements TimerListener2 {
    public StarApplication app;
    public Screen s;
    public Animater animater = null;

    public MainCanvas(StarApplication app, Screen s) {
	this.app = app;
	animater = new Animater(this);
	setCurrentScreen(s);
	(new Thread(animater)).start();
    }

    public void setCurrentScreen(Screen s) {
	this.s = s;
	s.setCanvas(this);
	s.init();
    }

    public void setSoftLabel1(String label) {
	setSoftLabel(Frame.SOFT_KEY_1, label);
    }

    public void setSoftLabel2(String label) {
	setSoftLabel(Frame.SOFT_KEY_2, label);
    }

    public void setSoftLabel3(String label) {
	setSoftLabel(Frame.SOFT_KEY_3, label);
    }

    public void setSoftLabel4(String label) {
	setSoftLabel(Frame.SOFT_KEY_4, label);
    }

    public void setSelectLabel(String label) {
	setSoftLabel(Frame.SELECT_KEY, label);
    }

    public void paint(Graphics g) {
	g.lock();
	s.paint(g);
	g.unlock(true);
    }

    public void processEvent(int type, int param) {
	KeyRepeatTimer krt = KeyRepeatTimer.getInstance(param);
	krt.setListener(this);
	switch (type) {
	case Display.KEY_PRESSED_EVENT:
	    krt.control(KeyRepeatTimer.START);
	    s.keyPressedEvent(param);
	    break;
	case Display.KEY_RELEASED_EVENT:
	    krt.control(KeyRepeatTimer.STOP);
	    s.keyReleasedEvent(param);
	    break;
	}
	repaint();
    }

    public void timerExpired(TimeKeeper t) {
	if (t instanceof KeyRepeatTimer) {
	    KeyRepeatTimer krt = (KeyRepeatTimer) t;
	    int key = krt.getKey();
	    s.keyRepeating(key);
	    krt.control(KeyRepeatTimer.RESTART);
	    repaint();
	}
    }

    public boolean isKeyPressed(int key) {
	int s = getKeypadState();
	return (0 < ((1 << key) & s));
    }
}
