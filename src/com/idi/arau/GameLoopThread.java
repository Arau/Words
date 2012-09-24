package com.idi.arau;

import android.graphics.Canvas;

class GameLoopThread extends Thread {
	private ViewGame view;
	private boolean running;
	static final long FPS = 10;

	public GameLoopThread(ViewGame view) {
		this.view = view;
	}

	public void setRunning(boolean run) {
		running = run;
	}
	

	@Override
	public void run() {
		long ticksPS = 1000/FPS;
		long startTime;
		long sleepTime;
		while (running){
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try{
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.onDraw(c);
				}
			}
			finally{
				if (c != null){
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
			try{
				if (sleepTime > 0) {
					sleep(sleepTime);
				}
				else 
					sleep(10);
			} catch (Exception e) {}
		}			
	}
}