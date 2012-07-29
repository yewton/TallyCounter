package com.yewton;

import com.docomostar.StarApplication;
import com.docomostar.ui.Display;

public class TallyCounter extends StarApplication
{
    Counter c;
    public void started(int launchType) {
	Screen cs = new CounterScreen();
	MainCanvas mc = new MainCanvas(this, cs);
	Display.setCurrent(mc);
    }
}
