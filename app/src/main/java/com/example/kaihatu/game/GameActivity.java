package com.example.kaihatu.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.kaihatu.Account;
import com.example.kaihatu.MainActivity;
import com.example.kaihatu.R;
import com.example.kaihatu.ai.OseroAI;
import com.example.kaihatu.game.GamePresenter;
import com.example.kaihatu.game.GameResultActivity;import com.example.kaihatu.game.GameView;
import com.example.kaihatu.model.OseroGame;
import com.example.kaihatu.model.Place;
import com.example.kaihatu.model.Stone;
import com.example.kaihatu.model.ai.AINone;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements GameView {
    private List<List<ImageView>> placeList;
    private GamePresenter presenter;
    private int boardSize;
    private Handler mHandler = new Handler();
    private OseroGame game = new OseroGame();
    /** テキストオブジェクト */
    private Runnable delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("オセロ");
        }

        presenter = new GamePresenter();
        boardSize = presenter.getBoardSize();

        placeList = new ArrayList<>();
        GridLayout gamePlacesGrid = findViewById(R.id.gamePlacesGrid);

        // アクションバーに戻るを表示する
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for (int x = 0; x < boardSize; x++) {
            List<ImageView> row = new ArrayList<>();
            for (int y = 0; y < boardSize; y++) {
                View place = LayoutInflater.from(this).inflate(R.layout.grid_place, null);
                int finalX = x;
                int finalY = y;
                place.setOnClickListener(v -> presenter.onClickPlace(finalX, finalY));

                ImageView gamePlaceImageView = place.findViewById(R.id.gamePlaceImageView);
                row.add(gamePlaceImageView);
                gamePlacesGrid.addView(place);
            }
            placeList.add(row);
        }

        OseroAI ai = (OseroAI) getIntent().getSerializableExtra(EXTRA_NAME_AI);
        if (ai == null) {
            ai = new AINone();
        }
        presenter.onCreate(this, ai);

        Button passButton = findViewById(R.id.passButton);
        passButton.setOnClickListener(v -> presenter.onClickPass());
    }

    @Override
    public void putStone(Place place) {
        int imageRes;
        switch (place.getStone()) {
            case BLACK:
                imageRes = R.drawable.black_stone;
                break;
            case WHITE:
                imageRes = R.drawable.white_stone;
                break;
            case NONE:
            default:
                throw new IllegalArgumentException();
        }

        placeList.get(place.getX()).get(place.getY()).setImageResource(imageRes);

    }

    @Override
    public void setCurrentPlayerText(Stone player) {
        String color;
        switch (player) {
            case BLACK:
                color = "黒";
                break;
            case WHITE:
                color = "白";
                break;
            case NONE:
            default:
                throw new IllegalArgumentException();
        }
        TextView gameCurrentPlayerText = findViewById(R.id.gameCurrentPlayerText);

        gameCurrentPlayerText.setText(getString(R.string.textGameCurrentPlayer, color));


    }

    @Override
    public void showWinner(Stone player, int blackCount, int whiteCount) {
        Intent intent = new Intent(this, GameResultActivity.class);
        if (blackCount > whiteCount) {
            intent.putExtra(GameResultActivity.EXTRA_PLAYER, Stone.BLACK);
            intent.putExtra(GameResultActivity.EXTRA_BLACK_COUNT, blackCount);
            intent.putExtra(GameResultActivity.EXTRA_WHITE_COUNT, whiteCount);
        } else if (whiteCount > blackCount) {
            intent.putExtra(GameResultActivity.EXTRA_PLAYER, Stone.WHITE);
            intent.putExtra(GameResultActivity.EXTRA_BLACK_COUNT, blackCount);
            intent.putExtra(GameResultActivity.EXTRA_WHITE_COUNT, whiteCount);
        } else {
            intent.putExtra(GameResultActivity.EXTRA_PLAYER, Stone.NONE); // 引き分けの場合、Stone.NONEをセット
            intent.putExtra(GameResultActivity.EXTRA_BLACK_COUNT, blackCount);
            intent.putExtra(GameResultActivity.EXTRA_WHITE_COUNT, whiteCount);
        }
        startActivity(intent);
    }


    @Override
    public void finishGame() {
        finish();
    }

    @Override
    public void markCanPutPlaces(List<Place> places) {
        for (Place place : places) {
            placeList.get(place.getX()).get(place.getY()).setBackgroundColor(ContextCompat.getColor(this, R.color.green_light));
        }
    }

    @Override
    public void clearAllMarkPlaces() {
        for (List<ImageView> row : placeList) {
            for (ImageView imageView : row) {
                imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            }
        }
    }

    public static final String EXTRA_NAME_AI = "extra_ai";

    public static Intent createIntent(Context context, OseroAI ai) {
        if (ai == null) {
            ai = new AINone();
        }
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(EXTRA_NAME_AI, ai);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home :
                {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                }break;
            case R.id.action_user :
                {
                Intent intent = new Intent(this, Account.class);
                startActivity(intent);
                }break;
            case android.R.id.home:
                //画面を終了させる
                finish();
                break;
        }

        String message = "「" + item.getTitle() + "」が押されました。";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }


}
