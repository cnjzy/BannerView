package com.jzy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mContainer;

    private String[] mItemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mItemArray = getResources().getStringArray(R.array.banner_style_array);
        initView();
    }

    private void initView() {
        mContainer = findViewById(R.id.container);
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            Button button = (Button) mContainer.getChildAt(i);
            button.setText(mItemArray[i]);
            final int position = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClassName(getPackageName(), BannerViewActivity.class.getName());
                    intent.putExtra(BannerViewActivity.EXTRA_TITLE, mItemArray[position]);
                    intent.putExtra(BannerViewActivity.EXTRA_POSITION, position);
                    startActivity(intent);
                }
            });
        }
    }
}
