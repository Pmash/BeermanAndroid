package ui.beerman;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import ui.beerman.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Login extends Activity {

	protected EditText pas;
	protected EditText login;
	protected Button btn;
	protected ProgressBar pb;
	protected String pasw;
	protected String email;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
         
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                 WindowManager.LayoutParams.FLAG_FULLSCREEN);
         
         setContentView(R.layout.activity_login);
         
        login=(EditText)findViewById(R.id.login);
        pas=(EditText)findViewById(R.id.paswoord);
 		btn=(Button)findViewById(R.id.Blog);
 		pb=(ProgressBar)findViewById(R.id.progressBar1);
 		 pb.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
    public void onClick(View view){
    	
    	if(pas.getText().toString().length()<1 ){
				
				//Check van ingevulde velden
				Toast.makeText(this, "please enter a password", Toast.LENGTH_LONG).show();
			}
    	if(login.getText().toString().equals("Login")){
			
		
			Toast.makeText(this, "please enter a login", Toast.LENGTH_LONG).show();
		}
    	 	
    	
    	else{
    		
    		// HTTP request naar Simon's API om de login te valideren.
    		try{
				pb.setVisibility(View.VISIBLE);
				String loginValue    = URLEncoder.encode(login.getText().toString(), "UTF-8");
		          String passValue    = URLEncoder.encode(pas.getText().toString(), "UTF-8");
		          Log.v("ingegeven e-mail", login.getText().toString());
		          Log.v("Ingegeven paswoord", pas.getText().toString());
		          pasw= URLEncoder.encode(pas.getText().toString(), "UTF-8");
		          email = URLEncoder.encode(login.getText().toString(), "UTF-8");

		         
				String url = "http://feestinleuven.be/beerman/api/?action=login&email="+loginValue+"&pas="+passValue;
				//HTTP request wordt uitgevoerd in Asynchronische opdracht LoginAsync hieronder.
				
				LoginAsync logtje = new LoginAsync();
				logtje.execute(url);
				
				
								
    		}
	          catch(UnsupportedEncodingException ex)
	          {
	        	  Toast.makeText(getBaseContext(),"Server tijdelijk onbereikbaar, probeer later nog eens",Toast.LENGTH_LONG).show();
					
	          }     
}
    }
    
    
    
    
    // Asynchronische opdracht met HTTP request (Subklasse van Login.java)
    public class LoginAsync extends AsyncTask<String, Void, String>{

    	String responseBody ="";
    	@Override
    	
    	
    	protected String doInBackground(String... urls) {
    	
           
            HttpClient httpclient = new DefaultHttpClient();
            //Dit deel verwerkt de request en verkrijgt het JSONObject. Dit geeft een string terug.
         
          try {
        	  
        	  
        	  for (String url : urls) {
        	
        		  
        		  HttpGet httpget = new HttpGet(url);

              System.out.println("executing request " + httpget.getURI());

              // Create a response handler
              ResponseHandler<String> responseHandler = new BasicResponseHandler();
              // Body contains your json stirng
              responseBody = httpclient.execute(httpget, responseHandler);
              
        	  }

          } catch (ClientProtocolException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} finally {
              // When HttpClient instance is no longer needed,
              // shut down the connection manager to ensure
              // immediate deallocation of all system resources
              httpclient.getConnectionManager().shutdown();
          }
      

    
      
    		return responseBody;
    	}
    	
    	
    	//Hier wordt de string van doInBackground() geparsed en de login al dan gevalideerd.
    	@Override
        protected void onPostExecute(String result) {
    		if(responseBody.contains("200")){
				pb.setVisibility(View.INVISIBLE);
				Intent myIntent = new Intent(Login.this, Home.class);
		    	Login.this.startActivity(myIntent);
			}
    		else{
    			pb.setVisibility(View.INVISIBLE);
    			  Toast.makeText(getBaseContext(),"Verkeerde gegevens.",Toast.LENGTH_LONG).show();
    			  
    			  //Registratie Dialoog wordt hier opgevraagd.
    			  newInstance(email, pasw);
    			  Toast.makeText(getBaseContext(),"Log u opnieuw in.",Toast.LENGTH_LONG).show();
    			  
    			 
    			    
    		}
        }
    	
    	//Registratie dialoog wordt opgemaakt.
    	public DialogFragment newInstance(String email, String pasw) {
    		DialogFragment f = new Signin();

		    // Geeft gegevens door van login.java naar dialoog door middel van een bundel.
		    Bundle args = new Bundle();
		    args.putString("email", email );
		    args.putString("pasw", pasw);
		    f.setArguments(args);
		    
		    f.show(getFragmentManager(), "Registreren");

		    return f;
		}
    }
    
    

}
			
    	
    

