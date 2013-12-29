package ui.beerman;

import com.moodstocks.android.MoodstocksError;
import com.moodstocks.android.Result;
import com.moodstocks.android.ScannerSession;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;


//Dit is de eigenlijke scan klasse. Hier wordt het bierflesje gescant.
public class ScanActivity extends Activity implements ScannerSession.Listener {

	  private int ScanOptions = Result.Type.IMAGE | Result.Type.EAN8
	      | Result.Type.EAN13 | Result.Type.QRCODE | Result.Type.DATAMATRIX;
	  private TextView resultTextView;

	  private ScannerSession session;

	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_scan);

	    // get the camera preview surface & result text view
	    SurfaceView preview = (SurfaceView) findViewById(R.id.preview);
	    resultTextView = (TextView) findViewById(R.id.scan_result);
	    resultTextView.setText("Scan result: N/A");

	    // Create a scanner session
	    try {
	      session = new ScannerSession(this, this, preview);
	    } catch (MoodstocksError e) {
	      e.log();
	    }

	    // set session options
	    session.setOptions(ScanOptions);
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();

	    // start the scanner session
	    session.resume();
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();

	    // pause the scanner session
	    session.pause();
	  }

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();

	    // close the scanner session
	    session.close();
	  }

	  @Override
	  public void onApiSearchStart() {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onApiSearchComplete(Result result) {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onApiSearchFailed(MoodstocksError e) {
	    // TODO Auto-generated method stub

	  }

	  @Override
	  public void onScanComplete(Result result) {
		  
	    if (result != null) {
	      resultTextView.setText(String.format("Scan result: %s", result.getValue()));
	      String res = result.getValue();
	      
	      //Met het resultaat wordt een activity gestart.
	      Intent intent = new Intent(this, Bier.class);
	      Bundle b = new Bundle();
	      intent.putExtra("scanner", b);
	      b.putString("bier", res);
	      startActivity(intent);
	    }
	  }

	  @Override
	  public void onScanFailed(MoodstocksError error) {
	    resultTextView.setText(String.format("Scan failed: %d", error.getErrorCode()));
	  }

	}