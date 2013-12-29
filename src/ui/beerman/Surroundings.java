package ui.beerman;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import domainmodel.JSONParser;
import domainmodel.Venue;

import ui.beerman.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class Surroundings extends Activity implements LocationListener{
	
	private GoogleMap googleMap;
	double latitude;
	double longitude;
	 //URL to get JSON Array
    private String url = " ";
    JSONParser jsontje = new JSONParser();
    String best ="";
   
	 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_surroundings);
		
		 try {
	            // Loading map
			 	initilizeMap();
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 
		 //De locatie van het toestel wordt bepaald.
		 LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 Criteria criteria = new Criteria();
	     //best = lm.getBestProvider(criteria, true); 
	     
	     String provider = lm.getBestProvider(criteria, true);
	    
	  
	 
	     
	 
		Location gpswerk = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 10, this);
		 Log.v("Samsung doet lastig", longitude + ":" + latitude);
		// location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 resetLocation(gpswerk); 
	  
		
	  
		 
	}
		 
		 @SuppressLint("NewApi")
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void initilizeMap() {
		        if (googleMap == null) {
		            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
		                    R.id.map)).getMap();
		 
		            // check if map is created successfully or not
		            if (googleMap == null) {
		                Toast.makeText(getApplicationContext(),
		                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
		                        .show();
		            }
		        }
		        
		        
		     
		    }
		
		
		 @Override
		    protected void onResume() {
		        super.onResume();
		        initilizeMap();
		    }
		
		
	

	
	public void getLocation(){
		Log.v("waar", Double.toString(latitude)+" "+Double.toString(longitude));
		
		 // Create a LatLng object for the current location
	    LatLng latLng = new LatLng(latitude, longitude);   
		// Show the current location in Google Map        
	    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

	    // Zoom in the Google Map
	    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	    googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		
	    try{ 
	    String latValue    = URLEncoder.encode(Double.toString(latitude), "UTF-8");
        String longValue    = URLEncoder.encode(Double.toString(longitude), "UTF-8");
       

        Toast.makeText(getBaseContext(),"Please wait, connecting to server.",Toast.LENGTH_LONG).show();
		url = "http://feestinleuven.be/beerman/api/?action=nearby&long="+longValue+"&lat="+latValue;
		LocAsync logtje = new LocAsync();
		logtje.execute(url);
				
	    }
        catch(UnsupportedEncodingException ex)
        {
             // content.setText("Fail");
        } 
	}
	
	@Override
	public void onLocationChanged(Location location) {
		resetLocation(location);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void resetLocation(Location location) throws NullPointerException{
		// Hier kijkt mijn na of de GPS beschikbaar is.
		
		try{
		longitude = location.getLongitude();
		 latitude = location.getLatitude();
		 getLocation();
		}
		catch(NullPointerException e){
			  
				locatieIsNull();
		}
		
		
	}
	
	
	public void locatieIsNull(){
		Toast.makeText(getBaseContext(),"Gelieve eerst uw GPS aan te zetten",Toast.LENGTH_LONG).show();
   	
	}
	
	
	 private class LocAsync extends AsyncTask<String, Integer, String> {
		 String opl = "";
		 
		 ArrayList <String> longe = new ArrayList <String>(100);
			ArrayList <String> lati = new ArrayList <String>(100);
			ArrayList <String> namen = jsontje.geefNamen();
			ArrayList <String> adres = new ArrayList <String>(100);
			 ArrayList <Venue> cafes = jsontje.geefCafes();
		 
		 protected String doInBackground(String... urls) {
			 	
			try {
				opl = jsontje.getJSONFromUrl(url);
				
				String[] result = opl.split("plaats: ");
				
				
				 for ( int x=0; x<result.length; x++) {
					 
					 String part= result[x];
					 String[] fart = part.split("[,|,]");
					 
					 
					 for(int d = 1; d<fart.length; d++){
						   
						 
						 ArrayList <String> lijstje = new ArrayList<String>();
						 
						 lijstje.add(fart[d]);
						 String adresje = "";
						 
					
						 for(String lijst : lijstje){
							 
							 adres.add(adresje);
							 
							 
							 if (lijst.substring(1,4 ).equals("lng")){
								 String lng = lijst.substring(6);
								 longe.add(lng);
								 cafes.get(d).setLongitude(Double.parseDouble(lng));
									 
							 }
							 
							 if (lijst.substring(1,4 ).equals("lat")){
								 String lng = lijst.substring(6);
								 lati.add(lng);
								 cafes.get(d).setLatitude(Double.parseDouble(lng));
								 							 
							 }
							 
							 
						
							 
							
							 
							 
							
							 
						
							 
							// cafes.get(d).setAdres(lng);
							 
							 
							  }
						
						 
						 
						}
					 
					 
						 
					 }
	                 
	             
				 
			} catch (JSONException e) {
				Log.v("Gij: ", "Stinkt!");
				e.printStackTrace();
			}
	         
	         return opl;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	         
	     }

	     protected void onPostExecute(String result) {
	        makeMarkers();
	     }
	     
	     protected void makeMarkers(){
	    	 Double latit = 0.0;
	    	 Double longet = 0.0;
	    	 String naam = "";
	    	 String adresse = "None";
	    	 
	    	 for(int i = 0; i<lati.size() && i<longe.size() && i<adres.size() ; i++){
	    		 //naam = namen.get(i);
	    		 naam = cafes.get(i).getNaam();
	    		 
	    		 latit = Double.parseDouble(lati.get(i));
	    		 //latit = cafes.get(i).getLatitude();
    			
	    	 
    			
    			 longet = Double.parseDouble(longe.get(i));
    			 //longet = cafes.get(i).getLongitude();
    			 
    			 
    			 
    			if(adres.get(i)!=null){
    			adresse = adres.get(i);
    			}
    			else{
    				adresse = "geen";
    			}
    			 
    			 
    		 
	    	 googleMap.addMarker(new MarkerOptions().position(new LatLng(latit, longet)).title(naam).snippet(adresse));
	    	 
	     }
	    	 for(String e: adres){    
			        System.out.println(adres);
			   	    }
	     
	    
	 }
	 
	
	
	
	    }

	
}

	 
	 

	
	

