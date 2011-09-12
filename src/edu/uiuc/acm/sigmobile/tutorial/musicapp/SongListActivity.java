package edu.uiuc.acm.sigmobile.tutorial.musicapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SongListActivity extends Activity {
	
	ArrayAdapter<String> songs;
	ListView sListView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);
        
        
        songs = new ArrayAdapter<String>(this, R.layout.song_info);
        for(int i = 0; i < 100; i++){
        	songs.add(String.format("Song %d",i));
        }
        
        sListView = (ListView) findViewById(R.id.songs);
        sListView.setAdapter(songs);
        sListView.setOnItemClickListener(mSongClickListener);
        sListView.setStackFromBottom(false);
        
        
    }
    
    
    
    private OnItemClickListener mSongClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        	
        	String selected_message = String.format("%s selected!", songs.getItem(av.getPositionForView(v)));
        	Toast.makeText(getApplicationContext(), selected_message, Toast.LENGTH_SHORT).show();
        
        }
    };
    
    
    

}
