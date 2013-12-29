package ui.beerman;

import ui.beerman.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity {
	
	protected EditText search;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	         
	         requestWindowFeature(Window.FEATURE_NO_TITLE);
	         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	         
	         setContentView(R.layout.activity_home);
	         search = (EditText) findViewById(R.id.SText); 
	    }
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.login, menu);
	        return true;
	    }
	  // De juiste knoppen krijgen de juiste acties.
	  //bron: http://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android
	  public void onClick(View view){
		  switch(view.getId())
			{
			case R.id.thirsty:
				  Intent myIntent = new Intent(Home.this, Surroundings.class);
			    	Home.this.startActivity(myIntent);
			break;
			case R.id.BScan:
				  Intent myScan = new Intent(Home.this, Searcher.class);
			    	Home.this.startActivity(myScan);
			break;
			case R.id.BSearch:
				  Intent mySearch = new Intent(this, Bier.class);
				  Bundle b = new Bundle();
				  
				   // De ingegeven zoekgegevens worden gecheckt
				  if(!search.getText().toString().equals("Zoeken") && !search.getText().toString().equals("")){
				  b.putString("bier", search.getText().toString());
			      mySearch.putExtra("scanner", b);
			      startActivity(mySearch);
				  }
				  else{
					  Toast.makeText(getBaseContext(),"Geen zoekopdracht gegeven.",Toast.LENGTH_LONG).show();
				  }
			break;
			
			default:
			throw new RuntimeException("Unknow button ID");
			
	  }
		  
	  }
	  
	  
	

}
