package com.emasjid;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.Adapters.GelleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagesGellery extends AppCompatActivity {

    ViewPager viewPager;

    GelleryAdapter adapter;

    List<String> paths = new ArrayList<>();

    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_gellery);

        viewPager = findViewById(R.id.viewPager);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (getIntent().hasExtra("bean") && getIntent().hasExtra("position")) {

            paths = getIntent().getStringArrayListExtra("bean");

            Toast.makeText(this, paths.size()+  "", Toast.LENGTH_SHORT).show();

            adapter = new GelleryAdapter(ImagesGellery.this, paths);


            for (int i = 0; i < paths.size(); i++) {
                Log.e("paths===", paths.get(i));
            }

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem((getIntent().getIntExtra("position", 1)));


        }


    }
}
