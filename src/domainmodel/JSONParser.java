package domainmodel;

	import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
	
	//Bron:http://www.learn2crack.com/2013/10/android-asynctask-json-parsing-example.html
	
	
	public class JSONParser {
	    static InputStream is = null;
	    static JSONObject jObj = null;
	    static String json = "";
	    protected ArrayList <String> namen = new ArrayList<String>();
	    protected ArrayList <Venue> cafes = new ArrayList<Venue>();
	    
	    // constructor
	    public JSONParser() {
	    }
	    public String getJSONFromUrl(String url) throws JSONException {
	    	String result = "";
	        // Making HTTP request
	        try {
	            // defaultHttpClient
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            json = sb.toString();
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	        // try parse the string to a JSON object
	        try {
	            jObj = new JSONObject(json);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	        // return JSON String
	        
	       result =  parser(jObj);
	        return result;
	    }
	    
	    public String parser(JSONObject jObj) throws JSONException{
	    	String result = "";
	    	String name ="";
	    	jObj = (JSONObject) new JSONTokener(json).nextValue();

	        JSONObject jsonResponse = jObj.getJSONObject("response");
	        JSONArray venues = jsonResponse.getJSONArray("venues");
	        
	        for (int i = 0; i < venues.length(); i++)
	        {       
	            JSONObject c = venues.getJSONObject(i);
	            String id = c.getString("id");
	            String naam = c.getString("name");
	            String lat = c.getString("location");
	            //String lng = c.getString("lng");
	           	            
	            
	            String list = "Naam: "+naam;
	            result += "plaats: " +lat;
	            
	            Venue ven = new Venue (id, naam);
	           
	            cafes.add(ven);
	            namen.add(naam);
	            
	            
	            
	            
	        }

	   	    for(Venue e: cafes){    
	        System.out.println(e.getNaam() + " " + e.getID());
	   	    }
	    	return result;
	    	
	    }
	    
	    public ArrayList<String> geefNamen(){
	    	return namen;
	    	//getter met een speciale naam.
	    }
	
	    public ArrayList<Venue> geefCafes(){
	    	return cafes;
	    	//getter met een speciale naam.
	    }

}
