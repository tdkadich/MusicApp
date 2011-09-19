package edu.uiuc.acm.sigmobile.tutorial.musicapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SongListActivity extends Activity {
	
	ArrayList<String> mSongs = null;
	ArrayAdapter<String> songs;
	ListView sListView;
	
	private String[] ARTISTS = new String[] {"white stripes"};
	
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
        
        ArrayAdapter<String> auto_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ARTISTS);
        final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.search_bar1);
        textView.setAdapter(auto_adapter);
        
        Button search_button = (Button) findViewById(R.id.search_button1);
        search_button.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v){
        		String query = textView.getText().toString();
        		new SearchTask().execute(query);
        	}
        });
        
        
    }
    
    private OnItemClickListener mSongClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
        	
        	String selected_message = String.format("%s selected!", songs.getItem(av.getPositionForView(v)));
        	Toast.makeText(getApplicationContext(), selected_message, Toast.LENGTH_SHORT).show();
        
        }
    };
    
    
    public void updateMyList(ArrayList<String> newSongs){
    	
    	songs.clear();
    	for(String cSong : newSongs){
    		songs.add(cSong);
    	}
    	songs.notifyDataSetChanged();
    	
    }
    
    public class SearchTask extends AsyncTask<String, Integer, ArrayList<String>> {
    	
    	DefaultHttpClient client = null;
    	public static final String SERVER_URL = "https://www-s.acm.uiuc.edu/acoustics";
    	public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    	
    	public static final String JSON_BASE = "https://www-s.acm.uiuc.edu/acoustics/json.pl?mode=";

    	@Override
    	protected ArrayList<String> doInBackground(String... params) {
    		// TODO Connect to acoustics and search
    		
    		if(client == null){
    			getHttpClient();
    		}
    		
    		HttpGet search = create_get_request(params[0]);
         	
    		JSONArray rlist = make_get_request_list(search);
         	
         	ArrayList<String> slist = getListOfSongs(rlist);
         	
         	return slist;
    		
    	}
    	
    	
    	@Override
    	protected void onPostExecute(ArrayList<String> result){
    		super.onPostExecute(result);
    		
    		updateMyList(result);
    		
    	}
    	
    	
    	private void getHttpClient(){
            if(client == null){
    	        client = new DefaultHttpClient();
    	        final HttpParams params = client.getParams();
    	        HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
    	        HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
    	        ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
            }
        }
    	
    	
    	private HttpGet create_get_request(String query){
    		
    		HttpGet search = null;
    		query = query.trim();
    		query = query.replaceAll(" ", "%20");
    		String uri = String.format("%ssearch;field=%s;value=%s",JSON_BASE,"any",query);
    		System.out.println(uri.toString());
    		try {
    			search = new HttpGet(new URI(uri));
    		} catch (URISyntaxException e1) {
    			System.out.println("ERROR!");
    			e1.printStackTrace();
    		}
    		
    		return search;
    		
    	}
    	
    	
    	private JSONArray make_get_request_list(HttpGet mGet){
        	
        	JSONArray object = null;
        	
        	BufferedReader in = null;
        	try{
        		HttpResponse resp = client.execute(mGet);
        		System.out.println(resp.getEntity().toString());
        		StringBuffer sb = new StringBuffer("");
        		String line = "";
        		in = new BufferedReader (new InputStreamReader(resp.getEntity().getContent()));
        		while ((line = in.readLine()) != null) {
        			sb.append(line + "\n");
        		}
        		in.close();
        		object = new JSONArray(sb.toString()); 
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	
        	return object;
        	
        }
    	
    	
    	private ArrayList<String> getListOfSongs(JSONArray rlist){
    		
    		ArrayList<String> song_titles = new ArrayList<String>();
    		for(int i = 0; i < rlist.length(); i++){
    			try {
    				JSONObject currSong = rlist.getJSONObject(i);
    				song_titles.add(currSong.getString("title"));
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			
    		}
    		
    		return song_titles;
    		
    	}

    }
    

}
