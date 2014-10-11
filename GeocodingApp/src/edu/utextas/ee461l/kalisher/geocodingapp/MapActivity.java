package edu.utextas.ee461l.kalisher.geocodingapp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity
{
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen2);
		Intent i = getIntent();
		String locationData = i.getStringExtra("location");
		//map = MapFragment.newInstance().getMap();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		HashMap<String, String> keyValueMap = new HashMap<String, String>();
		String[] lines = locationData.split("\n");
		if (lines == null)
		{
		  //Compensate for strange JDK semantics
		  lines = new String[] { locationData };
		}
		for (String line : lines)
		{
		  if (!line.contains(":"))
		  {
		    //Skip lines that don't contain key-value pairs
		    continue;
		  }
		  String[] parts = line.split(":");
		  if (!keyValueMap.containsKey(parts[0].trim()))
		  {
			  keyValueMap.put(parts[0].trim(), parts[1].trim());
		  }
		}
		
		double lat = Double.parseDouble(keyValueMap.get("\"lat\"").replace(",", ""));
		double lng = Double.parseDouble(keyValueMap.get("\"lng\""));
		String address = keyValueMap.get("\"formatted_address\"");
		final LatLng LOC = new LatLng(lat, lng);
		Log.i("location", LOC.toString());
		Log.i("address", address);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOC, 15));
		Marker location = map.addMarker(new MarkerOptions().position(LOC).title(address.substring(1, address.length() - 2)));
		location.setVisible(true);
		location.showInfoWindow();
	}	
}