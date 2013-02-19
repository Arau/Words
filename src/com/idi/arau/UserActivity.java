package com.idi.arau;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_form);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user, menu);
        return true;
    }
}
