package enseirb.t2.miniflux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.projet.miniflux.R;

//Classe qui gére la première activité pour se connecter au serveur 
@SuppressWarnings("unused")
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

	}
	
	public void onClick(View v) {
			Intent i=null;
		if(v.getId()==R.id.sign_in) {
			i=new Intent(MainActivity.this, HomeActivity.class);
        	startActivity(i);
		}
		
		if(v.getId()==R.id.sign_up) {
			  HttpCall task=new HttpCall();
			  task.execute(new String[] { "http://www.france3.com" });
			  try{
			  String s=task.get();
			  }
			  catch(Exception e) {
				  System.out.println("Error: "+e.getMessage());
			  }
		}
	}
	
	 private String readStream(InputStream in) {
	    	BufferedReader reader = null;
	    	try {
	    		reader = new BufferedReader(new InputStreamReader(in));
	    		String line = "";
	    		String s="";
	    		while ((line = reader.readLine()) != null) {
	    			s=s.concat(line);
	    		}
	    		return s;
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	} finally {
	    		if (reader != null) {
	    			try {
	    				reader.close();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}
	    	return null;
	    } 
	 
	 class GetFluxName extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
	        	  URL url = new URL("http://www.france3.fr");
	        	  HttpURLConnection con = (HttpURLConnection) url
	        	    .openConnection();
	        	  String s=readStream(con.getInputStream());
	        	  return s;
	        	  } catch (Exception e) {
	        	  e.printStackTrace();
	        	}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
			System.out.println("echo je marche");
		}
		 
	 }
	    
}
