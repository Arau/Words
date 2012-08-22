package com.idi.arau;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Toast;

public class ViewGame extends SurfaceView {
	private Context context;
	private ManagerGame manager;

	private Word word;
	private Bitmap picture;
	private ArrayList<Letter> alphabet;
	private List<Letter> drawablesWord;
	private List<Letter> touchedLetters = new ArrayList<Letter>();

	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private long lastClick = 0;

	public ViewGame(Context context) {
		super(context);
		this.context = context;
		this.manager = ManagerGame.getInstanceManager(context);
		if (!manager.isLast()) {
			obtainNextWord();
			doSurfaceJob();
		} else {
			finishGame();
		}
	}

	private void obtainNextWord() {
		this.word = manager.getNextWord();
		int resource = manager.getNextPicture();
		this.picture = BitmapFactory.decodeResource(getResources(), resource);
		//this.picture = redimensionarImagenMaximo(this.picture, 100,100);		
	}

	private Bitmap resizePicture() {
		int width = this.picture.getWidth();
		int height = this.picture.getHeight();
		float scaleWidth = ((float) 100) / width;
		float scaleHeight = ((float) 100) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(this.picture, 0, 0, width, height, matrix, false);		 
	}

	public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth,
			float newHeigth) {
		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeigth) / height;
		Matrix matrix = new Matrix();		
		matrix.postScale(scaleWidth, scaleHeight);		
		return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
	}

	private void doSurfaceJob() {
		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				gameLoopThread.setRunning(false);
				while (retry) {
					try {
						gameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initDrawables();
				defineDrawablesWord();

				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});
	}

	private void finishGame() {
		Intent i = new Intent(this.context, FinishGame.class);
		context.startActivity(i);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(this.picture, 50, 50, null);
		drawLetters(canvas);
		String touchedString = letterArrayToString(touchedLetters);
		word.drawTouchedLetters(canvas, touchedString, this.getWidth(),
				this.getHeight());

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (System.currentTimeMillis() - lastClick > 300) {
			lastClick = System.currentTimeMillis();
			synchronized (getHolder()) {
				letterTouched(event);
			}
		}
		return true;
	}

	private void letterTouched(MotionEvent event) {
		for (int i = drawablesWord.size() - 1; i >= 0; i--) {
			Letter letter = drawablesWord.get(i);
			if (letter.isTouch(event.getX(), event.getY())) {
				touchedLetters.add(letter);
				drawablesWord.remove(letter);
				Boolean correct = word
						.checkWord(letterArrayToString(touchedLetters));
				if (!correct)
					gameOver();
				else if (touchedLetters.size() == word.getString().length())
					restart();
				break;
			}
		}
	}

	public void setWord(String stringWord) {
		this.word = new Word(stringWord);
	}

	private void restart() {
		ViewGame v = new ViewGame(context);
		ViewGroup parent = (ViewGroup) this.getParent();
		parent.addView(v);
		parent.removeView(this);
	}

	private void defineDrawablesWord() {
		boolean[] visits = new boolean[26];
		Arrays.fill(visits, Boolean.FALSE);
		drawablesWord = new ArrayList<Letter>();
		for (char character : word.getString().toCharArray()) {
			int asciiValue = character;
			Letter letter = new Letter();
			if (visits[asciiValue - 97]) {
				letter = createLetter(character);
			} else {
				letter = alphabet.get(asciiValue - 97);
			}
			visits[asciiValue - 97] = Boolean.TRUE;
			drawablesWord.add(letter);
		}
	}

	private Letter createLetter(char character) {
		int resource;
		switch (character) {
		case 'a':
			resource = R.drawable.a;
			break;
		case 'b':
			resource = R.drawable.b;
			break;
		case 'c':
			resource = R.drawable.c;
			break;
		case 'd':
			resource = R.drawable.d;
			break;
		case 'e':
			resource = R.drawable.e;
			break;
		case 'f':
			resource = R.drawable.f;
			break;
		case 'g':
			resource = R.drawable.g;
			break;
		case 'h':
			resource = R.drawable.h;
			break;
		case 'i':
			resource = R.drawable.i;
			break;
		case 'j':
			resource = R.drawable.j;
			break;
		case 'k':
			resource = R.drawable.k;
			break;
		case 'l':
			resource = R.drawable.l;
			break;
		case 'm':
			resource = R.drawable.m;
			break;
		case 'n':
			resource = R.drawable.n;
			break;
		case 'o':
			resource = R.drawable.o;
			break;
		case 'p':
			resource = R.drawable.p;
			break;
		case 'q':
			resource = R.drawable.q;
			break;
		case 'r':
			resource = R.drawable.r;
			break;
		case 's':
			resource = R.drawable.s;
			break;
		case 't':
			resource = R.drawable.t;
			break;
		case 'u':
			resource = R.drawable.u;
			break;
		case 'v':
			resource = R.drawable.v;
			break;
		case 'w':
			resource = R.drawable.w;
			break;
		case 'x':
			resource = R.drawable.x;
			break;
		case 'y':
			resource = R.drawable.y;
			break;
		case 'z':
			resource = R.drawable.z;
			break;
		default:
			resource = R.drawable.a;
		}

		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
		return new Letter(character, this, bmp);
	}

	private void initDrawables() {
		alphabet = new ArrayList<Letter>();

		alphabet.add(createLetter('a'));
		alphabet.add(createLetter('b'));
		alphabet.add(createLetter('c'));
		alphabet.add(createLetter('d'));
		alphabet.add(createLetter('e'));
		alphabet.add(createLetter('f'));
		alphabet.add(createLetter('g'));
		alphabet.add(createLetter('h'));
		alphabet.add(createLetter('i'));
		alphabet.add(createLetter('j'));
		alphabet.add(createLetter('k'));
		alphabet.add(createLetter('l'));
		alphabet.add(createLetter('m'));
		alphabet.add(createLetter('n'));
		alphabet.add(createLetter('o'));
		alphabet.add(createLetter('p'));
		alphabet.add(createLetter('q'));
		alphabet.add(createLetter('r'));
		alphabet.add(createLetter('s'));
		alphabet.add(createLetter('t'));
		alphabet.add(createLetter('u'));
		alphabet.add(createLetter('v'));
		alphabet.add(createLetter('w'));
		alphabet.add(createLetter('x'));
		alphabet.add(createLetter('y'));
		alphabet.add(createLetter('z'));
	}

	private String letterArrayToString(List<Letter> arrayLetters) {
		String stringLetters = new String();
		for (Letter letter : arrayLetters) {
			stringLetters += letter.getChar();
		}
		return stringLetters;
	}

	private void drawLetters(Canvas canvas) {
		for (Letter letter : drawablesWord) {
			letter.onDraw(canvas);
		}
	}

	private void gameOver() {
		Toast toast = Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT);
		toast.show();
	}	
}
