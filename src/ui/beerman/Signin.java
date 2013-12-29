package ui.beerman;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import ui.beerman.Login.LoginAsync;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


@SuppressLint({ "NewApi", "ValidFragment" })


public class Signin extends DialogFragment {
	
	protected String email;
	protected String pasw;
	
   
	    
    
    
    
	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public String getPasw() {
		return pasw;
	}





	public void setPasw(String pasw) {
		this.pasw = pasw;
	}





	@SuppressLint("NewApi")
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    email = getArguments().getString("email");
	    pasw = getArguments().getString("pasw");
	    
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.register)
               .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   String url = "http://feestinleuven.be/beerman/api/?action=register&email="+email+"&pas="+pasw;
       				LoginAsync logtje = new LoginAsync();
       				logtje.execute(url);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
    public class LoginAsync extends AsyncTask<String, Void, String>{

    	String responseBody ="";
    	@Override
    	
    	
    	protected String doInBackground(String... urls) {
    		//ALERT MESSAGE
           
            HttpClient httpclient = new DefaultHttpClient();
         
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
    	
    	@Override
        protected void onPostExecute(String result) {
    		
    		if(responseBody.contains("200")){
    			Log.v("test", "gelukt");
			}
    		else{
    			
    			Log.v("test", "gefaald");
    			  
    		}
        }
    }
    
}
