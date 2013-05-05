package com.idi.arau;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		getActionBar().setHomeButtonEnabled(true);
		
		((TextView)findViewById(R.id.help)).setText(R.string.help_text);
		TextViewJustify.justifyText(((TextView)findViewById(R.id.help)), 240f); 
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
        	case android.R.id.home:
                Intent intent = new Intent(this, Words.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
		}
	}
}
