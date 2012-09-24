package com.idi.arau;

import android.widget.ProgressBar;

public class TimeThread extends Thread {

	private boolean running = false;
	ProgressBar timeBar;
	int currentValue;
	Game main;

	public TimeThread(Game activity, ProgressBar timeBar, int maxValue) {
		this.timeBar = timeBar;
		main = activity;
		currentValue = maxValue;
		timeBar.setMax(maxValue);
		timeBar.setProgress(currentValue);
	}

	public void setRunning(boolean run) {
		running = run;
	}

	public void setTime(int time) {
		currentValue = time;
	}

	public int getTime() {
		return currentValue;
	}

	public void resetTime(int maxValue) {
		timeBar.setMax(maxValue);
		currentValue = maxValue;
	}

	@Override
	public void run() {
		long ticksPS = 1000;
		long startTime;
		long sleepTime;
		while (running) {
			startTime = System.currentTimeMillis();
			--currentValue;
			timeBar.setProgress(currentValue);
			if (currentValue == 0) {
				running = false;
				main.onTimeOut();
			}
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {
			}
		}
	}
}