package com.yewton;

import com.docomostar.io.ConnectionException;
import com.docomostar.media.MediaImage;
import com.docomostar.media.MediaManager;
import com.docomostar.ui.Graphics;
import com.docomostar.ui.Image;
import com.docomostar.ui.Sprite;
import com.docomostar.ui.SpriteSet;

/**
 * @author yuto_sasaki
 *
 */
public class CounterDigitSet extends Animation {
    public static final int MAX_NOD = 5;
    public static final int MIN_NOD = 1;

    /**
     * @return width
     */
    public int getWidth() {
	return width;
    }

    /**
     * @return height
     */
    public int getHeight() {
	return height;
    }

    private int width = 0;
    private int height = 0;
    private int pos_x = 0;
    private int pos_y = 0;
    private int nod = 4;
    private int value = 0;
    private int max_value = 0;
    private int a_y = pos_y;
    private CounterDigit[] digits = new CounterDigit[MAX_NOD];
    private CounterDigit[] prev_digits = new CounterDigit[MAX_NOD];
    private SpriteSet ss_digits = null;

    public class CounterDigit {
	private Sprite s;
	private Image img;
	private int value;
	private int sx;
	private int sy;
	public static final int WIDTH = 50;
	public static final int HEIGHT = 100;

	public CounterDigit() throws ConnectionException {
	    MediaImage m = MediaManager.getImage("resource:///digits.gif");
	    m.use();
	    img = m.getImage();
	    s = new Sprite(img, 0, 0, WIDTH, HEIGHT);
	    s.setLocation(-WIDTH, -HEIGHT);
	}

	/**
	 * @return sx
	 */
	public int getSx() {
	    return sx;
	}

	/**
	 * @return sy
	 */
	public int getSy() {
	    return sy;
	}

	public void setLocation(int x, int y) {
	    s.setLocation(x, y);
	}

	public Sprite getSprite() {
	    return s;
	}

	public Image getImage() {
	    return img;
	}

	public void setValue(int n) {
	    value = n;
	    int sx = 0, sy = 0;
	    if ((0 <= n) && (n <= 4)) {
		sx = n * WIDTH;
		sy = 0;
	    } else if ((5 <= n) && (n <= 9)) {
		n -= 5;
		sx = n * WIDTH;
		sy = HEIGHT;
	    }
	    s.setImage(img, sx, sy, WIDTH, HEIGHT);
	    this.sx = sx;
	    this.sy = sy;
	}

	public int getValue() {
	    return value;
	}
    }

    /**
     * @param num
     * @throws ConnectionException
     */
    public CounterDigitSet(int num) throws ConnectionException {
	Sprite[] ss = new Sprite[MAX_NOD * 2];
	for (int i = 0; i < prev_digits.length; ++i) {
	    prev_digits[i] = new CounterDigit();
	    ss[i] = prev_digits[i].getSprite();
	}
	for (int i = 0; i < digits.length; ++i) {
	    int idx = i + prev_digits.length;
	    digits[i] = new CounterDigit();
	    ss[idx] = digits[i].getSprite();
	}
	ss_digits = new SpriteSet(ss);
	setNumOfDigits(num);
    }

    /**
     * @param num
     */
    public void setNumOfDigits(int num) {
	if (num < MIN_NOD)
	    num = MIN_NOD;
	else if (MAX_NOD < num)
	    num = MAX_NOD;
	for (int i = 0; i < digits.length; ++i) {
	    CounterDigit cd = digits[i];
	    CounterDigit pcd = prev_digits[i];
	    Sprite d = cd.getSprite();
	    Sprite pd = pcd.getSprite();
	    if ((i + 1) <= num) {
		d.setVisible(true);
		pd.setVisible(true);
	    } else {
		d.setVisible(false);
		pd.setVisible(false);
		cd.setValue(0);
		pcd.setValue(0);
	    }
	}
	max_value = 0;
	for (int i = 0, j = 1; i < num; ++i, j *= 10) {
	    max_value += 9 * j;
	}
	width = CounterDigit.WIDTH * num;
	height = CounterDigit.HEIGHT;
	nod = num;
    }

    /**
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
	movePrevious(x, y);
	moveCurrent(x, y);
	pos_x = x;
	if (pos_y != y) {
	    a_y = y - CounterDigit.HEIGHT;
	}
	pos_y = y;
    }

    /**
     * @return
     */
    public int getX() {
	return pos_x;
    }

    /**
     * @return
     */
    public int getY() {
	return pos_y;
    }

    /**
     * @param g
     */
    public void paint(Graphics g) {
	g.drawSpriteSet(ss_digits);
    }

    public void execute(Graphics g) {
	if (pos_y < a_y) {
	    a_y = pos_y;
	}
	for (int i = 0; i < nod; ++i) {
	    if (digits[i].getValue() == prev_digits[i].getValue())
		continue;
	    CounterDigit d = digits[i];
	    Sprite s = d.getSprite();
	    s.setVisible(true);
	    int h = pos_y - a_y;
	    int sx = d.getSx();
	    int sy = d.getSy() + h;
	    int sw = CounterDigit.WIDTH;
	    int sh = CounterDigit.HEIGHT - h;
	    s.setImage(d.getImage(), sx, sy, sw, sh);
	}
	g.drawSpriteSet(ss_digits);
	if (pos_y == a_y) {
	    disanimate();
	    return;
	}
	a_y += 25;
    }

    public void disanimate() {
	super.disanimate();
	a_y = pos_y - CounterDigit.HEIGHT;
    }

    private void movePrevious(int x, int y) {
	move(prev_digits, x, y);
    }

    private void moveCurrent(int x, int y) {
	move(digits, x, y);
    }

    private void move(CounterDigit[] ds, int x, int y) {
	for (int i = 0; i < nod; ++i) {
	    ds[i].setLocation(x + (i * CounterDigit.WIDTH), y);
	}
    }

    /**
     * @param value
     */
    synchronized public void setValue(int value) {
	if (alive) {
	    disanimate();
	}
	if (value < 0) {
	    value = 0;
	} else if (max_value < value) {
	    value = max_value;
	}
	this.value = value;
	for (int i = nod - 1, j = this.value; 0 <= i; --i, j /= 10) {
	    prev_digits[i].setValue(digits[i].getValue());
	    digits[i].getSprite().setVisible(false);
	    digits[i].setValue(j % 10);
	}
	animate();
    }

    /**
     * @return nod
     */
    public int getNumberOfDigits() {
	return nod;
    }

    /**
     * @return max_value
     */
    public int getMaxValue() {
	return max_value;
    }
}
