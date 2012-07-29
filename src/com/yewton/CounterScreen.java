package com.yewton;

import com.docomostar.io.ConnectionException;
import com.docomostar.ui.Frame;
import com.docomostar.ui.Graphics;

public class CounterScreen extends Screen {
    private Counter c;
    private CounterDigitSet cds = null;
    private static final int MODE_1 = 0;
    private static final int MODE_2 = 1;
    private int mode = MODE_1;

    public CounterScreen() {
	try {
	    cds = new CounterDigitSet(4);
	} catch (ConnectionException e) {
	    e.printStackTrace();
	    cvs.app.terminate();
	}
	c = new Counter();
    }

    public void init() {
	cvs.setSoftLabel1("ﾓｰﾄﾞ切替");
	cvs.setSoftLabel2("終了");
	cvs.setSoftLabel3("リセット");
	cvs.setSelectLabel("↑↑");
	cvs.setSoftArrowLabel(Frame.ARROW_DOWN | Frame.ARROW_LEFT | Frame.ARROW_RIGHT | Frame.ARROW_UP);
	cvs.animater.addAnimation(cds);
    }

    public void paint(Graphics g) {
	g.setColor(Graphics.getColorOfRGB(0x33, 0x33, 0x33));
	g.fillRect(0, 0, cvs.getWidth(), cvs.getHeight());
	g.setColor(Graphics.getColorOfName(Graphics.WHITE));
	int w = cds.getWidth();
	int cw = cvs.getWidth();
	cds.setLocation((cw - w) / 2, 100);
	String modestring = "";
	String desc = "";
	switch (mode) {
	case MODE_1:
	    modestring = "モード 1";
	    desc = "ステップ数増減";
	    break;
	case MODE_2:
	    modestring = "モード 2";
	    desc = "カウンタ増減";
	    break;
	default:
	    modestring = "謎のモード";
	    desc = "????";
	}
	g.drawString("←→で桁数増減", 12, 48);
	g.drawString("↑↓で" + desc, 12, 72);
	g.drawString(modestring, 12, 160);
	g.drawString("step = " + c.getStep(), 12, 190);
	cds.paint(g);
    }

    public void update(Graphics g) {
	cds.update(g);
    }

    public void keyPressedEvent(int key) {
	switch (key) {
	case Key.SELECT:
	    cds.setValue(c.up().getCount());
	    break;
	}
	keyPressOrRepeat(key);
    }

    private void toggleMode() {
	if (mode == MODE_1)
	    mode = MODE_2;
	else
	    mode = MODE_1;
    }

    public void keyReleasedEvent(int key) {
	switch (key) {
	case Key.SOFT1:
	    toggleMode();
	    break;
	case Key.SOFT2:
	    cvs.app.terminate();
	    break;
	case Key.SOFT3:
	    c.setCount(0);
	    cds.setValue(0);
	    break;
	}
    }

    public void keyRepeating(int key) {
	keyPressOrRepeat(key);
    }

    private void keyPressOrRepeat(int key) {
	int nod = cds.getNumberOfDigits();
	switch (key) {
	case Key.UP:
	    switch (mode) {
	    case MODE_1:
		c.setStep(c.getStep() + 1);
		break;
	    case MODE_2:
		c.setCount(c.getCount() + 1);
		cds.setValue(c.getCount());
		break;
	    }
	    break;
	case Key.DOWN:
	    switch (mode) {
	    case MODE_1:
		c.setStep(c.getStep() - 1);
		break;
	    case MODE_2:
		c.setCount(c.getCount() - 1);
		cds.setValue(c.getCount());
		break;
	    }
	    break;
	case Key.RIGHT:
	    setNOD(++nod);
	    break;
	case Key.LEFT:
	    setNOD(--nod);
	    break;
	}
    }

    private void setNOD(int nod) {
	if (CounterDigitSet.MAX_NOD < nod) {
	    nod = CounterDigitSet.MAX_NOD;
	} else if (nod < CounterDigitSet.MIN_NOD) {
	    nod = CounterDigitSet.MIN_NOD;
	}
	if (nod == cds.getNumberOfDigits()) {
	    return;
	}
	cds.setNumOfDigits(nod);
	c.setMax(cds.getMaxValue());
	cds.setValue(c.getCount());
    }
}
