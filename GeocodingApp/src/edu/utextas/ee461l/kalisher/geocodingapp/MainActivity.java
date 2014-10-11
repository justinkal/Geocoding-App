package edu.utextas.ee461l.kalisher.geocodingapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void submitHandler(View view)
	{	
		String request = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		request += ((EditText) findViewById(R.id.mainInput)).getText();
		request = request.replaceAll(" ", "+");
		request += "&key=AIzaSyBzxnfobS-3cq3b4uArYJyfr5Xmvli8X6w";
		
		class MapGetter extends AsyncTask<String, Void, String>
		{
            @Override
            protected String doInBackground(String... request)
            {
            	Log.i(MainActivity.class.toString(), request[0]);
            	String location = "";
				try
				{
					URL url = new URL(request[0]);
					Log.i(MainActivity.class.toString(), "created URL");
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					Log.i(MainActivity.class.toString(), "created HttpConnection");
					InputStream in = new BufferedInputStream(urlConnection.getInputStream());
					Log.i(MainActivity.class.toString(), "created InputStream");
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					Log.i(MainActivity.class.toString(), "created BufferedReader");
					String line;
					StringBuilder builder = new StringBuilder();
					while((line = reader.readLine()) != null)
					{
						builder.append(line + "\n");
					}
					Log.i(MainActivity.class.toString(), "finished while loop");
					urlConnection.disconnect();
					Log.i(MainActivity.class.toString(), "disconnected urlConnection");
					location = builder.toString();
				}
				catch (Exception e)
				{
					Log.e(MainActivity.class.toString(), e.getMessage());
				}
				return location;
            }
		}
		String location = "";
		try
		{
			MapGetter get = new MapGetter();
			AsyncTask<String, Void, String>  mapGet= get.execute(request);
			location = mapGet.get();
			//Toast.makeText(this, location, Toast.LENGTH_LONG).show();
			
			Intent nextScreen = new Intent(getApplicationContext(), MapActivity.class);
			nextScreen.putExtra("location", location);
			startActivity(nextScreen);
		}
		catch (Exception e)
		{
			Log.e(MainActivity.class.toString(), e.getMessage());
		}
		
	}
}