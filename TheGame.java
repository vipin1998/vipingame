package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.content.Context;
import android.os.Bundle;

public class TheGame extends GameThread {

    //Will store the image of a ball
    private Bitmap mBall;

    //The X and Y position of the ball on the screen (middle of ball)
    private float mBallX = -100;
    private float mBallY = -100;

    //The speed (pixel/second) of the ball in direction X and Y
    private float mBallSpeedX = 0;
    private float mBallSpeedY = 0;
    private Bitmap mPaddle;
    private float mPaddleX = 0;
    private float mPaddleSpeedX = 0;
    private float mMinDistanceBetweenBallandPaddle = 0;
    private Bitmap msmileface;
    private float msmilefaceX = 0;
    private float msmilefaceY = 0;
    private float mMinDistanceBetweenBallandSmileface = 0;
    private Bitmap mSadBall;
    private float[] mSadBallX = {-100, -100};
    private float[] mSadBallY = new float[2];
    private float time = 20;
    // private SoundPlayer sound;

    /*private SoundPool sounds;
    private int sExplosion;*/


    //This is run before anything else, so we can prepare things here
    public TheGame(GameView gameView) {

        //House keeping
        super(gameView);

        //Prepare the image so we can draw it on the screen (using a canvas)
        mBall = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.small_red_ball);
        mPaddle
                = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.yellow_ball);
        msmileface
                = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.smiley_ball);
        mSadBall
                = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.sad_ball);
    }

    //This is run before a new game (also after an old game)
    @Override
    public void setupBeginning() {
        //Initialise speeds
        //    sound=new SoundPlayer(this);
        mBallSpeedX = mCanvasWidth / 2;
        mBallSpeedY = mCanvasHeight / 2;

        //Place the ball in the middle of the screen.
        //mBall.Width() and mBall.getHeigh() gives us the height and width of the image of the ball
        mBallX = mCanvasWidth / 2;
        mBallY = mCanvasHeight / 2;
        mPaddleX = mCanvasWidth / 2;
        mPaddleSpeedX = 0;
        mMinDistanceBetweenBallandPaddle = (mPaddle.getWidth() / 2 + mBall.getWidth() / 2) * (mPaddle.getWidth() / 2 + mBall.getWidth() / 2);
        msmilefaceX = mCanvasWidth / 2;
        msmilefaceY = msmileface.getHeight() / 2;
        mSadBallX[0] = mCanvasWidth / 3;
        mSadBallY[0] = mCanvasHeight / 3;
        mSadBallX[1] = mCanvasWidth - mCanvasWidth / 3;
        mSadBallY[1] = mCanvasHeight / 3;
        mMinDistanceBetweenBallandSmileface = (msmileface.getWidth() / 2 + mBall.getWidth() / 2) * (msmileface.getWidth() / 2 + mBall.getWidth() / 2);
        time = 20;
    }

    @Override
    protected void doDraw(Canvas canvas) {
        //If there isn't a canvas to draw on do nothing
        //It is oot understanding what is happening here.
        if (canvas == null) return;

        super.doDraw(canvas);

        //draw the image of the ball using the X and Y of the ball
        //drawBitmap uses top left corner as reference, we use middle of picture
        //null means that we will use the image without any extra features (called Paint)
        canvas.drawBitmap(mBall, mBallX - mBall.getWidth() / 2, mBallY - mBall.getHeight() / 2, null);
        canvas.drawBitmap(mPaddle, mPaddleX - mPaddle.getWidth() / 2, mCanvasHeight - mPaddle.getHeight() / 2, null);
        canvas.drawBitmap(msmileface, msmilefaceX - msmileface.getWidth() / 2, msmilefaceY - msmileface.getHeight() / 2, null);
        for (int i = 0; i < mSadBallX.length; i++) {
            canvas.drawBitmap(mSadBall, mSadBallX[i] - msmileface.getWidth() / 2, mSadBallY[i] - msmileface.getHeight() / 2, null);
        }
        Paint textpaint = new Paint();
        textpaint.setColor(Color.BLACK);
        textpaint.setTextSize(30);
        canvas.drawText("TIME:" + (int) time, 0, 30f, textpaint);
        if (time <= 0 || mBallY >= mCanvasHeight) {
            Paint text = new Paint();
            text.setColor(Color.BLUE);
            text.setTextSize(30);
            canvas.drawText("Your Score:" + score, mCanvasWidth / 3, (2 * mCanvasHeight) / 3, text);
        }
        Paint textp = new Paint();
        textp.setColor(Color.RED);
        textp.setTextSize(45);
        if (score >= 8 && (time <= 0 || mBallY >= mCanvasHeight)) {
            canvas.drawText("Excellient", mCanvasWidth / 3, (3 * mCanvasHeight) / 4, textp);
        }
        if ((score >= 5 && score < 8) && (time <= 0 || mBallY >= mCanvasHeight)) {
            canvas.drawText("Good Work", (27 * mCanvasWidth) / 100, (3 * mCanvasHeight) / 4, textp);
        }
        if ((score >= 2 && score < 5) && (time <= 0 || mBallY >= mCanvasHeight)) {
            canvas.drawText("Average", mCanvasWidth / 3, (3 * mCanvasHeight) / 4, textp);
        }
        if (score <= 1 && (time <= 0 || mBallY >= mCanvasHeight)) {
            canvas.drawText("Bad Playing", (27 * mCanvasWidth) / 100, (3 * mCanvasHeight) / 4, textp);
        }
        Paint textpa = new Paint();
        textpa.setColor(Color.BLACK);
        textpa.setTextSize(45);
        if (time <= 0 || mBallY >= mCanvasHeight) {
            canvas.drawText("Tap To Restart", (20 * mCanvasWidth) / 100, (6 * mCanvasHeight) / 7, textpa);

        }
    }

    //This is run whenever the phone is touched by the user
    @Override
    protected void actionOnTouch(float x, float y) {
        mPaddleX = x;
        //   sound.playhitsound();
      /*  sounds.play(sExplosion,1.0f,1.0f,0,0,1.5f);*/
        //Increase/decrease the speed of the ball making the ball move towards the touch
	/*	mBallSpeedX = x - mBallX;
		mBallSpeedY = y - mBallY;*/
      /*  mBallX = x;
        mBallY = y;*/
    }


    //This is run whenever the phone moves around its axises
    @Override
    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
		/*
		Increase/decrease the speed of the ball.
		If the ball moves too fast try and decrease 70f
		If the ball moves too slow try and increase 70f
		 */
        mPaddleSpeedX = mBallSpeedX + 70f * xDirection;
        if (mPaddleX <= 0 && mPaddleSpeedX < 0) {
            mPaddleSpeedX = 0;
            mPaddleX = 0;
        }
        if (mPaddleX >= mCanvasWidth && mPaddleSpeedX > 0) {
            mPaddleSpeedX = 0;
            mPaddleX = mCanvasWidth;
        }

	/*	mBallSpeedX = mBallSpeedX + 70f * xDirection;
		mBallSpeedY = mBallSpeedY - 70f * yDirection;*/
    }


    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {
        float distanceBetweenBallandPaddle;
        if (mBallSpeedY > 0) {
            distanceBetweenBallandPaddle = (mPaddleX - mBallX) * (mPaddleX - mBallX) + (mCanvasHeight - mBallY) * (mCanvasHeight - mBallY);
            if (mMinDistanceBetweenBallandPaddle >= distanceBetweenBallandPaddle) {
                float SpeedofBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallX - mPaddleX;
                mBallSpeedY = mBallY - mCanvasHeight;
                float newSpeedofBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallSpeedX * (SpeedofBall / newSpeedofBall);
                mBallSpeedY = mBallSpeedY * (SpeedofBall / newSpeedofBall);
                //  updateScore(1);
            }
        }
        float distanceBetweenBallandsmileface;
        distanceBetweenBallandsmileface = (msmilefaceX - mBallX) * (msmilefaceX - mBallX) + (msmilefaceY - mBallY) * (msmilefaceY - mBallY);
        if (mMinDistanceBetweenBallandSmileface >= distanceBetweenBallandsmileface) {
            float SpeedofBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
            mBallSpeedX = mBallX - msmilefaceX;
            mBallSpeedY = mBallY - msmilefaceY;
            float newSpeedofBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
            mBallSpeedX = mBallSpeedX * (SpeedofBall / newSpeedofBall);
            mBallSpeedY = mBallSpeedY * (SpeedofBall / newSpeedofBall);
            updateScore(1);

        }
        for (int i = 0; i < mSadBallX.length; i++) {
            distanceBetweenBallandsmileface = (mSadBallX[i] - mBallX) * (mSadBallX[i] - mBallX) + (mSadBallY[i] - mBallY) * (mSadBallY[i] - mBallY);
            if (mMinDistanceBetweenBallandSmileface >= distanceBetweenBallandsmileface) {
                float SpeedofBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallX - mSadBallX[i];
                mBallSpeedY = mBallY - mSadBallY[i];
                float newSpeedofBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallSpeedX * (SpeedofBall / newSpeedofBall);
                mBallSpeedY = mBallSpeedY * (SpeedofBall / newSpeedofBall);


            }
        }


        //Move the ball's X and Y using the speed (pixel/sec)
        mBallX = mBallX + secondsElapsed * mBallSpeedX;
        mBallY = mBallY + secondsElapsed * mBallSpeedY;
        time = time - secondsElapsed;

        if ((mBallX <= mBall.getWidth() / 2 && mBallSpeedX < 0) || (mBallX >= mCanvasWidth - mBall.getWidth() / 2 && mBallSpeedX > 0)) {
            mBallSpeedX = -mBallSpeedX;
        }
        if ((mBallY <= mBall.getWidth() / 2 && mBallSpeedY < 0)) {
            mBallSpeedY = -mBallSpeedY;
        }
        mPaddleX = mPaddleX + secondsElapsed * mPaddleSpeedX;
        if (mBallY >= mCanvasHeight) {
            setState(GameThread.STATE_LOSE);
        }
        if (time <= 0) {
            setState(GameThread.STATE_LOSE);
        }
    }
}

