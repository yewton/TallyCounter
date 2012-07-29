package com.yewton;

import java.util.Hashtable;

import com.docomostar.ui.UIException;
import com.docomostar.util.TimeKeeper;
import com.docomostar.util.Timer;
import com.docomostar.util.TimerListener;

public class KeyRepeatTimer implements TimeKeeper, TimerListener {
    private static Hashtable h = new Hashtable();
    private int key;
    private Timer t;
    private TimerListener2 listener = null;
    private static final int ACCELERATION = 60;
    private static final int INITIAL_INTERVAL = 300;
    private static final int MIN_INTERVAL = 50;
    private int interval = INITIAL_INTERVAL;
    private boolean alive = false;
    public static final int START = 0;
    public static final int STOP = 1;
    public static final int RESTART = 2;

    private KeyRepeatTimer(int key) {
	this.key = key;
	t = new Timer();
	t.setListener(this);
	t.setRepeat(false);
    }

    public static synchronized KeyRepeatTimer getInstance(int key) {
	Integer k = new Integer(key);
	if (h.get(k) == null) {
	    h.put(k, new KeyRepeatTimer(key));
	}
	return (KeyRepeatTimer) h.get(k);
    }

    public int getKey() {
	return key;
    }

    public synchronized void control(int op) {
	switch (op) {
	case START:
	    start();
	    break;
	case STOP:
	    stop();
	    break;
	case RESTART:
	    restart();
	    break;
	}
    }

    public void start() {
	if (alive == true) {
	    return;
	}
	alive = true;
	t.setTime(INITIAL_INTERVAL);
	t.start();
    }

    public void restart() {
	if (alive == false) {
	    return;
	}
	interval -= ACCELERATION;
	if (interval < MIN_INTERVAL)
	    interval = MIN_INTERVAL;
	t.setTime(interval);
	t.start();
    }

    public void stop() {
	if (alive == false) {
	    return;
	}
	alive = false;
	interval = INITIAL_INTERVAL;
	t.stop();
    }

    public void dispose() {
	t.dispose();
    }

    public void setRepeat(boolean b) {
	t.setRepeat(b);
    }

    public void setTime(int time) {
	t.setTime(time);
    }

    public void setListener(TimerListener2 listener) {
	this.listener = listener;
    }

    public int getMinTimeInterval() {
	return t.getMinTimeInterval();
    }

    public int getResolution() {
	return t.getResolution();
    }

    public void timerExpired(Timer source) {
	listener.timerExpired(this);
    }
}
