package edu.uiuc.acm.sigmobile.tutorial.musicapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MusicAppActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button bShowSongs = (Button) findViewById(R.id.show_songs);
        bShowSongs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent Intentx = new Intent(getApplicationContext(), SongListActivity.class);
                startActivityForResult(Intentx, 0);
				
			}
		});
    }
    
    
    
}