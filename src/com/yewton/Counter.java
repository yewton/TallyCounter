package com.yewton;

public class Counter {
    public static final int DEFAULT_COUNT = 0;
    public static final int DEFAULT_MAX = 9999;
    public static final int DEFAULT_STEP = 1;
    int count;
    int step;
    int max;

    public Counter() {
	this(DEFAULT_COUNT, DEFAULT_MAX, DEFAULT_STEP);
    }

    public Counter(int count) {
	this(count, DEFAULT_MAX, DEFAULT_STEP);
    }

    public Counter(int count, int max) {
	this(count, max, DEFAULT_STEP);
    }

    public Counter(int count, int max, int step) {
	this.count = count;
	this.max = max;
	this.step = step;
    }

    public Counter up() {
	if (max < (count += step)) {
	    count = max;
	}
	return this;
    }

    public Counter down() {
	if ((count -= step) < 0) {
	    count = 0;
	}
	return this;
    }

    public Counter setCount(int n) {
	if ((0 <= n) && (n <= max)) {
	    count = n;
	}
	return this;
    }

    public Counter setMax(int max) {
	this.max = max;
	if (max < count) {
	    count = max;
	}
	return this;
    }

    public Counter setStep(int step) {
	if (0 < step) this.step = step;
	return this;
    }

    public int getCount() {
	return count;
    }

    public int getMax() {
	return max;
    }

    public int getStep() {
	return step;
    }
}
