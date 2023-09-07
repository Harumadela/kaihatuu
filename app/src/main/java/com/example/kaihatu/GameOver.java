package com.example.kaihatu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;import com.example.kaihatu.StartUp;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        int points = getIntent().getExtras().getInt("points");

        RealmQuery<Data> query = realm.where(Data.class);
        query.equalTo("id",1);
        RealmResults<Data> result = query.findAll();

        if(result.get(0).getShooting_point() < points){
            realm.beginTransaction();
            result.get(0).setShooting_point(points);
            realm.commitTransaction();
        }


        tvPoints = findViewById(R.id.tvPoints);
        tvPoints.setText("" + points);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, StartUp.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }
}
