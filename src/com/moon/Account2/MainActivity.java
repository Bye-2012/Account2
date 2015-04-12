package com.moon.Account2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void clickImage(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.imageView_write:
                intent.setClass(this,WriteActivity.class);
                break;
            case R.id.imageView_detail:
                intent.setClass(this,DetailActivity.class);
                break;
            case R.id.imageView_report:
                intent.setClass(this,ReportActivity.class);
                break;
            case R.id.imageView_exit:
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                break;
        }
        startActivity(intent);
    }
}
