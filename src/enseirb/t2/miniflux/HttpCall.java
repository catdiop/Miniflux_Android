package enseirb.t2.miniflux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

//Classe qui fait une requete Http et renvoie la réponse
public class HttpCall extends AsyncTask<String, Void, String> {
	
	@Override
	protected String doInBackground(String... urls) {
		// TODO Auto-generated method stub
		try {
      	  URL url = new URL(urls[0]);
      	  HttpURLConnection con = (HttpURLConnection) url
      	    .openConnection();
      	  String s=readStream(con.getInputStream());
      	  return s;
      	  } catch (Exception e) {
      	  e.printStackTrace();
      	}
		return null;
	}

	// Et l'affiche au niveau de l'appli
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
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
}
