package ui.beerman;



import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Scanner;

import ui.beerman.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


//Deze activity is eigenlijk een laadklasse om de foto's die op onze database staan te kunnen synchroniseren
//Bron: https://moodstocks.com/documentation/getting-kastarted/android/
public class Searcher extends Activity implements Scanner.SyncListener{
	 // Moodstocks API key/secret pair
	  private static final String API_KEY = "gtixrdoeq1rcbhfdecne";
	  private static final String API_SECRET = "xdAmGq0m4KlDmnUU";

	  private boolean compatible = false;
	  private Scanner scanner;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_searcher);

	    Toast.makeText(getBaseContext(),"De gegevens worden opgeladen.",Toast.LENGTH_SHORT).show();
		
	    compatible = Scanner.isCompatible();
	    if (compatible) {
	      try {
	        this.scanner = Scanner.get();
	        scanner.open(this, API_KEY, API_SECRET);
	        scanner.sync(this);
	      } catch (MoodstocksError e) {
	        e.log();
	      }
	    }
	  }

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    if (compatible) {
	      try {
	        scanner.close();
	      } catch (MoodstocksError e) {
	        e.log();
	      }
	    }
	  }

	  @Override
	  public void onSyncStart() {
	    Log.d("Moodstocks SDK", "Sync will start.");
	  }

	  @Override
	  public void onSyncComplete() {
	    try {
	      Log.d("Moodstocks SDK", String.format("Sync succeeded (%d image(s))", scanner.count()));
	    } catch (MoodstocksError e) {
	      e.printStackTrace();
	    }
	  }

	  @Override
	  public void onSyncFailed(MoodstocksError e) {
	    Log.d("Moodstocks SDK", "Sync error: " + e.getErrorCode());
	  }

	  @Override
	  public void onSyncProgress(int total, int current) {
	    int percent = (int) ((float) current / (float) total * 100);
	    Log.d("Moodstocks SDK", String.format("Sync progressing: %d%%", percent));
	  }
	  
	  public void onScanButtonClicked(View view) {
		    startActivity(new Intent(this, ScanActivity.class));
		}
	
	}
	
