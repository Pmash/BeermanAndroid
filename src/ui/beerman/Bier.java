package ui.beerman;

import java.util.ArrayList;

import org.json.JSONException;


import domainmodel.BierParser;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Bier extends Activity {
	
	
	private TextView titel;
	private TextView alcohol;
	protected String alc;
	protected TextView brouwerij;
	protected String brouw = "";
	private TextView soort;
	protected String soo;
	private TextView beschrijving;
	protected String bes;
	
	String value = "geen";
	BierParser bierP = new BierParser();
	String url = "";
	boolean data = false;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
         
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                 WindowManager.LayoutParams.FLAG_FULLSCREEN);
         setContentView(R.layout.activity_bier);
         titel = (TextView) findViewById(R.id.titel);
         alcohol = (TextView) findViewById(R.id.alcoholDes);
         brouwerij = (TextView) findViewById(R.id.brouwerijDes);
         soort = (TextView) findViewById(R.id.soortDes);
         beschrijving = (TextView) findViewById(R.id.beschrijvingDes);
         
         //De gegevens van de vorige activities worden hier opgehaald.
         Intent i = getIntent();
         Bundle bundle = i.getBundleExtra("scanner");
     	 value = bundle.getString("bier");
     	 Log.v("Titel", value);
     	 
     	 pasInhoudAan(value);
     	
         
         
         
    }
	
	//De inhoud van de activity wordt aangepast aan de gevonden gegevens.
  private void pasInhoudAan(String value2) {
	  String title = value2.replaceAll("_", " ");
	  titel.setText(title);
	   
	  
	  // Door middel van een Asynchrosische opdracht BierAsync() wordt er een HTTP request uitgevoerd.
	  Toast.makeText(getBaseContext(),"Please wait, connecting to server.",Toast.LENGTH_LONG).show();
		url = "http://feestinleuven.be/beerman/api/?action=search&toSearch="+title.replaceAll(" ", "%20");
		BierAsync logtje = new BierAsync();
		logtje.execute(url);
		
	}
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

private class BierAsync extends AsyncTask<String, Integer, String> {
	 String opl = "";
	
	 
		 
	 protected String doInBackground(String... urls) {
		 	int r;
		 	
		 	
		try {
			
			//De HTTP request wordt uitgevoerd door de klasse BierParser (MVC)
			opl = bierP.getJSONFromUrl(url);
			
			
			
			Log.v("uncleanString", opl);
			
			if(!opl.equals("[]")){
				Log.v("It's", "true");
				
				Log.v("oplength", Integer.toString(opl.length()));
				data = true;
			
			//De verkregen resultaat wordt hier geparsed.
			String[] fart = opl.split("[,|,]");
			
			
			
				for(r = 0; r < fart.length ; r++){
			//Log.v("BierParser result", fart[r]);				
			//String test = fart[r].substring(3,12 );
			Log.v("result", fart[r]);
			
				
				
			if (fart[r].substring(3,6).equals("bro")){
					
					brouw =  fart[r].substring(15,fart[r].length()-1);
					//Log.v("Result", brouw);
					 
				} 
			if (fart[r].substring(1,4).equals("soo")){
				
				soo =  fart[r].substring(9,fart[r].length()-3);
				//Log.v("Result", brouw);
				 
			} 
			
			if (fart[r].substring(1,4).equals("alc")){
				
				alc =  fart[r].substring(18,fart[r].length()-1);
				//Log.v("Result", brouw);
				 
			} 
			
			if (fart[r].substring(1,4).equals("bes")){
				Log.v("dit is de beschrijving", fart[r]);
				bes =  fart[r].substring(16,fart[r].length()-1);
				//Log.v("Result", brouw);
				 
			} 
			
			
			
				 }
			}
			else{
				Log.v("It's", "false");
				
			}
					 
			
		}
                
            
			 
		 catch (JSONException e) {
			Log.v("Gij: ", "Stinkt!");
			e.printStackTrace();
		}
		
		
	 
		
        
        return opl;
    }

    

	protected void onProgressUpdate(Integer... progress) {
        
    }

    protected void onPostExecute(String result) {
    	// Alle gevonden gegevens worden ingevuld op de juiste plaats.
    	if(data == true){
    	brouwerij.setText(brouw);
    	soort.setText(soo);
    	alcohol.setText(alc);
    	beschrijving.setText(bes);
    	}
    	
    	// Indien het bier niet herkend wordt, krijgt de gebruiker een foutmelding
    	else if(data == false){
    		titel.setText("ONBEKEND BIER");
    		brouwerij.setText("Niet gevonden");
        	soort.setText("Niet gevonden");
        	alcohol.setText("Niet gevonden");
        	beschrijving.setText("Het bier u zocht is niet gevonden, gelieve een andere zoekopdracht in te voeren");
    	}
    
    }
    
    
    
    
   




   }



}
