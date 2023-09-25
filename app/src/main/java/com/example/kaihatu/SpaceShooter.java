package com.example.kaihatu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class SpaceShooter extends View {
    // 敵の宇宙船がショットを発射する間隔を設定（例：800ミリ秒ごとにショットを発射）
    long shotInterval = 700;
    boolean Haikei = false;
    static Context context;
    Bitmap background, lifeImage;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    boolean paused = false;
    OurSpaceship ourSpaceship;
    static EnemySpaceship enemySpaceship;
    Random random;
    static ArrayList<Shot> enemyShots, ourShots;
    Explosion explosion;
    ArrayList<Explosion> explosions;
    static boolean enemyShotAction = false;
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    public SpaceShooter(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyShots = new ArrayList<>();
        ourShots = new ArrayList<>();
        explosions = new ArrayList<>();
        ourSpaceship = new OurSpaceship(context);
        enemySpaceship = new EnemySpaceship(context);
        handler = new Handler();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawText("Pt: " + points, 0, TEXT_SIZE, scorePaint);
        for(int i=life; i>=1; i--){
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if(life == 0){
            paused = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        // Move enemySpaceship
        enemySpaceship.ex += enemySpaceship.enemyVelocity;

        // 敵の宇宙船が右側の壁に衝突すると、敵の速度が逆転します
        if(enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() >= screenWidth){
            enemySpaceship.enemyVelocity *= -1;
        }
        // 敵の宇宙船が左の壁に衝突すると、再び敵の速度が逆転します
        if(enemySpaceship.ex <=0){
            enemySpaceship.enemyVelocity *= -1;
        }



        if (points > 50)
        {
            shotInterval = 500;
        }
        if (points == 80 && Haikei == false)
        {
            background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
            shotInterval = 400;
            Haikei = true;
        }
        if (points > 100)
        {
            shotInterval = 300;
        }
        // ショットの最後の発射からの経過時間を計算
        long currentTime = System.currentTimeMillis();
        long timeSinceLastShot = currentTime - enemySpaceship.lastShotTime;

        if (timeSinceLastShot >= shotInterval) {
            // 一定間隔ごとにショットを発射する
            Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey);
            Shot enemyShot2 = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 1, enemySpaceship.ey);
            enemyShots.add(enemyShot);
            enemyShots.add(enemyShot2);
            enemySpaceship.lastShotTime = currentTime; // 最後の発射時間を更新
        }

        // emoneShotAction が false になるまで、敵はランダムな移動距離からショットを発射する必要があります
        if(enemyShotAction == false){
            if (enemySpaceship.ex >= 200 + random.nextInt(400)) {
                for (int i = 0; i < 3; i++) { // 一度に3つのショットを発射する
                    Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey);
                    Shot enemyShot2 = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 1, enemySpaceship.ey);
                    enemyShots.add(enemyShot);
                    enemyShots.add(enemyShot2);
                }
                // emoneShotAction を true にしています。
                enemyShotAction = true;
            }
            if(enemySpaceship.ex >= 400 + random.nextInt(800)){
                Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey );
                Shot enemyShot2 = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 1, enemySpaceship.ey);
                enemyShots.add(enemyShot);
                enemyShots.add(enemyShot2);
                // 敵が一度にショートできるように、emoneShotAction を true にしています。
                enemyShotAction = true;
            }
            else{
                Shot enemyShot = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey );
                Shot enemyShot2 = new Shot(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 1, enemySpaceship.ey);
                enemyShots.add(enemyShot);
                enemyShots.add(enemyShot2);
                // 敵が一度にショートできるように、emoneShotAction を true にしています。
                enemyShotAction = true;
            }
        }
        // Draw the enemy Spaceship
        canvas.drawBitmap(enemySpaceship.getEnemySpaceship(), enemySpaceship.ex, enemySpaceship.ey, null);
        // 画面の左端と右端の間に宇宙船を描きます
        if(ourSpaceship.ox > screenWidth - ourSpaceship.getOurSpaceshipWidth()){
            ourSpaceship.ox = screenWidth - ourSpaceship.getOurSpaceshipWidth();
        }else if(ourSpaceship.ox < 0){
            ourSpaceship.ox = 0;
        }
        // Draw our Spaceship
        canvas.drawBitmap(ourSpaceship.getOurSpaceship(), ourSpaceship.ox, ourSpaceship.oy, null);
        // 撃たれた敵を宇宙船の下に描き、攻撃を受けている場合はライフを減少させ、除去します
        // 敵ショット ArrayList からショット オブジェクトを取得し、爆発を表示します。
        // それ以外の場合は、画面の下端からも削除されます
        // 敵ショットからのショット オブジェクト。
        // 画面にenemyShotsが存在しない場合は、emoneShotActionをfalseに変更し、敵が
        // 撃てます。
        for(int i=0; i < enemyShots.size(); i++){
            enemyShots.get(i).shy += 15;
            canvas.drawBitmap(enemyShots.get(i).getShot(), enemyShots.get(i).shx, enemyShots.get(i).shy, null);
            if((enemyShots.get(i).shx >= ourSpaceship.ox)
                    && enemyShots.get(i).shx <= ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth()
                    && enemyShots.get(i).shy >= ourSpaceship.oy
                    && enemyShots.get(i).shy <= screenHeight-200){
                life--;
                System.out.println(ourSpaceship.oy +"プラス"+enemyShots.get(i).shy+"プラス"+screenHeight);
                enemyShots.remove(i);
                explosion = new Explosion(context, ourSpaceship.ox, ourSpaceship.oy);
                explosions.add(explosion);
            }else if(enemyShots.get(i).shy >= screenHeight-200){
                enemyShots.remove(i);
            }
            if(enemyShots.size() < 1){
                enemyShotAction = false;
            }

        }

        // Draw our spaceship shots towards the enemy. If there is a collision between our shot and enemy
        // spaceship, increment points, remove the shot from ourShots and create a new Explosion object.
        // Else if, our shot goes away through the top edge of the screen also remove
        // the shot object from enemyShots ArrayList.
        for(int i=0; i < ourShots.size(); i++){
            ourShots.get(i).shy -= 15;
            canvas.drawBitmap(ourShots.get(i).getShot(), ourShots.get(i).shx, ourShots.get(i).shy, null);
            if((ourShots.get(i).shx >= enemySpaceship.ex)
                    && ourShots.get(i).shx <= enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth()
                    && ourShots.get(i).shy <= enemySpaceship.getEnemySpaceshipWidth()
                    && ourShots.get(i).shy >= enemySpaceship.ey){
                points++;
                ourShots.remove(i);
                explosion = new Explosion(context, enemySpaceship.ex, enemySpaceship.ey);
                explosions.add(explosion);
            }else if(ourShots.get(i).shy <=0){
                ourShots.remove(i);
            }
        }
        // Do the explosion
        for(int i=0; i < explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).eX, explosions.get(i).eY, null);
            explosions.get(i).explosionFrame++;
            if(explosions.get(i).explosionFrame > 8){
                explosions.remove(i);
            }
        }
        // If not paused, we’ll call the postDelayed() method on handler object which will cause the
        // run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        // UPDATE_MILLIS.
        if(!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();
        //event.getAction() が MotionEvent.ACTION_UP の場合、ourShots 配列リスト サイズ < 1 の場合、
        // 新しいショットを作成します。
        // このようにして、画面上で一度に 1 つのショットのみを作成するように制限します。
        if(event.getAction() == MotionEvent.ACTION_UP){
            if(ourShots.size() < 7){
                Shot ourShot = new Shot(context, ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth() / 2, ourSpaceship.oy);
                ourShots.add(ourShot);
            }
        }
        // When event.getAction() is MotionEvent.ACTION_DOWN, control ourSpaceship
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            ourSpaceship.ox = touchX;
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control ourSpaceship
        // along with the touch.
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            ourSpaceship.ox = touchX;
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true;
    }
}
