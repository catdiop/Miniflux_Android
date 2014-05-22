package enseirb.t2.miniflux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projet.miniflux.R;

//Classe qui gére la première activité pour se connecter au serveur 
// Maintenant ici il faut envoyer les données du formulaire au serveur

@SuppressWarnings("unused")

public class MainActivity extends Activity {

//	private StringBuffer stringBuffer = new StringBuffer("");
//	private BufferedReader bufferedReader  = null ;
//
//	private static final String url = "http://ssoubnguemtchueng.rmorpheus.enseirb.fr/Miniflux/rest/flux/toto";
//
//	public static ProgressDialog progressDialog; // pour la gestion d'erreur
//
//	private EditText UserEditText;
//	private EditText PassEditText;
//
//	private static final String LOG_TAG = "Log : ";
//	private final String mimeType = "text/html";
//	private final String encoding = "utf-8";
//	private String pageWeb; 
//	private WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

//		/*ADDED FOR AUTHENTIFICATION*/
//
//		//initialisation de la progress bar
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setMessage("Attendez...");
//		progressDialog.setIndeterminate(true);
//		progressDialog.setCancelable(false);
//
//		webView = (WebView) findViewById(R.id.website);
//
//		// Récupération des éléments de la vue définis dans le xml
//		UserEditText = (EditText)findViewById(R.id.username);
//		PassEditText = (EditText)findViewById(R.id.password);
//
//		Button button =(Button)findViewById(R.id.sign_in);
//		button.setOnClickListener(new View.OnClickListener() {
//			@SuppressLint("SetJavaScriptEnabled")
//			public void onClick(View v) {
//
//				//				new Thread(){
//				//					public void run(){
//				int usersize = UserEditText.getText().length();
//				int passsize = PassEditText.getText().length();
//
//				//si les deux champs sont remplis
//				if(usersize>0 && passsize>0)
//				{
//					progressDialog.show();
//
//					String login = UserEditText.getText().toString();
//					String pass = PassEditText.getText().toString();
//
//					//on appelle la fonction dologin qui va communiquer av le serveur
//					try {
//						pageWeb = doLogin(login,pass);
//					} catch (ClientProtocolException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					webView.getSettings().setJavaScriptEnabled(true);
//					webView.loadDataWithBaseURL("fake://not/needed", pageWeb, mimeType, encoding, "");
//					webView.getSettings().setJavaScriptEnabled(true);
//				}
//				else
//					createDialog("Erreur","Entrez votre login et mot de passe");
//			}
//			//}.start();
//		}//}
//				);
//
//		//le bouton logout pour sortir de l'application
////		Button button1 =(Button)findViewById(R.id.logout);
////		button1.setOnClickListener(new View.OnClickListener() {
////			public void onClick(View v) {
////				quit(false,null);	
////			}
////		}); 
//

	}

	public void onClick(View v) {
		Intent i=null;
		if(v.getId()==R.id.sign_in ) {
			i=new Intent(MainActivity.this, HomeActivity.class);
			startActivity(i);
		}

		if(v.getId()==R.id.sign_up) {
			Intent intent = new Intent(MainActivity.this, SubscriptionActivity.class);
			startActivity(intent);


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

//	// Other methods add by Sandrine
//	//---------------------------------------------------------------------------------------------------------------
//
//	private void quit(boolean b, Intent i) {
//		//on envoie un résultat qui va permettre de quitter l'application
//		setResult((b)? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
//		finish();
//	}
//
//	//---------------------------------------------------------------------------------------------------------------
//
//	private void createDialog(String title, String text) {
//		AlertDialog ad = new AlertDialog.Builder(this)
//		.setPositiveButton("OK", null).setTitle(title).setMessage(text)
//		.create();
//		ad.show();
//	}
//	//---------------------------------------------------------------------------------------------------------------
//
//	private String doLogin(final String login, final String pass)throws ClientProtocolException, IOException {
//
//		Thread t = new Thread(){
//			public void run(){
//				Looper.prepare();					
//				try{
//
//					//on se connecte au serveur afin d'envoyer le login et le passwd
//					HttpClient client = new DefaultHttpClient();
//					HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
//
//					//la requete qui envoie les paramètres
//					HttpPost post = new HttpPost(url);
//
//					//on crée la liste qui contient les paramètres
//					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//					//on rajoute les paramètres à la liste
//					nvps.add(new BasicNameValuePair("username", login));
//					nvps.add(new BasicNameValuePair("password", pass));
//
//					post.setHeader("Content-Type", "application/x-www-form-urlencoded");
//
//					//joindre les paramètre à la requete
//					post.setEntity(new UrlEncodedFormEntity(nvps));
//
//					//Execution du client HTTP avec le HttpPost
//					HttpResponse response = client.execute(post);
//
//					//On récupère la réponse dans un InputStream
//					InputStream is = response.getEntity().getContent();
//					Log.d("myapp", "response " + response.getEntity().getContent() ); // crée des erreurs
//
//					//On crée un bufferedReader pour pouvoir stocker le résultat dans un string
//					bufferedReader  = new BufferedReader(new InputStreamReader(is));
//
//					//On lit ligne à ligne le bufferedReader pour le stocker dans le stringBuffer
//					String ligneCodeHTML = bufferedReader.readLine();
//					while (ligneCodeHTML != null){
//						stringBuffer.append(ligneCodeHTML);
//						stringBuffer.append("\n");
//						ligneCodeHTML = bufferedReader.readLine();
//					}  
//				}
//				catch (Exception e) 
//				{ 
//					progressDialog.dismiss();
//					createDialog("Erreur", "impossible d'établir la connexion");
//				}
//				Looper.loop();	
//			}		
//		};
//		t.start();
//		return stringBuffer.toString();
//	}

}