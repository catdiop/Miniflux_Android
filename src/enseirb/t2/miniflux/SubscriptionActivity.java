package enseirb.t2.miniflux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.projet.miniflux.R;

public class SubscriptionActivity extends Activity {
	
	private StringBuffer stringBuffer = new StringBuffer("");
	private BufferedReader bufferedReader  = null ;

	private static final String url = "http://cdiop.rmorpheus.enseirb.fr/Miniflux/rest/flux/toto";

	public static ProgressDialog progressDialog; // pour la gestion d'erreur

	private EditText UserEditText;
	private EditText PassEditText;
	private EditText ConfirmEditText;
	
	private static final String LOG_TAG = "Log : ";
	private final String mimeType = "text/html";
	private final String encoding = "utf-8";
	private String pageWeb; 
	private WebView webView;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscription);
		getActionBar().setDisplayHomeAsUpEnabled(true);

//		/*ADDED FOR AUTHENTIFICATION*/
		
				//initialisation de la progress bar
				progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Attendez...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(false);

				webView = (WebView) findViewById(R.id.website);

				// Récupération des éléments de la vue définis dans le xml
				UserEditText = (EditText)findViewById(R.id.username);
				PassEditText = (EditText)findViewById(R.id.password);
				ConfirmEditText = (EditText)findViewById(R.id.confirm);
				
				Button button =(Button)findViewById(R.id.subscription);
				
				button.setOnClickListener(new View.OnClickListener() {
					@SuppressLint("SetJavaScriptEnabled")
					public void onClick(View v) {
						
						Editable passtext = PassEditText.getText();
						Editable confirmtext = ConfirmEditText.getText();
						
						int usersize = UserEditText.getText().length();
						int passsize = PassEditText.getText().length();
						int confirmsize = ConfirmEditText.getText().length();
						
						//si les deux champs sont remplis
						if( usersize>0 && passsize>0 && confirmsize>0 && passtext.equals(confirmtext)==true )
						{
							progressDialog.show();

							String login = UserEditText.getText().toString();
							String pass = PassEditText.getText().toString();

							//on appelle la fonction dologin qui va communiquer av le serveur
							try {
								pageWeb = doLogin(login,pass);
							} catch (ClientProtocolException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							webView.getSettings().setJavaScriptEnabled(true);
							webView.loadDataWithBaseURL("fake://not/needed", pageWeb, mimeType, encoding, "");
							webView.getSettings().setJavaScriptEnabled(true);
						}
						
						else if(passtext.equals(confirmtext)== false && passsize>0){
							createDialog("Erreur","Entrez les deux mots de passe ne sont pas les mêmes");
						}
						else
							createDialog("Erreur","Entrez votre login et mot de passe");
					}
					//}.start();
				}//}
						);

				//le bouton logout pour sortir de l'application
//				Button button1 =(Button)findViewById(R.id.logout);
//				button1.setOnClickListener(new View.OnClickListener() {
//					public void onClick(View v) {
//						quit(false,null);	
//					}
//				}); 

		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		Intent i=null;
		if(v.getId()==R.id.subscription ) {
			i=new Intent(SubscriptionActivity.this, HomeActivity.class);
			startActivity(i);
		}
	}
	

	// Other methods add by Sandrine
	//---------------------------------------------------------------------------------------------------------------

	private void quit(boolean b, Intent i) {
		//on envoie un résultat qui va permettre de quitter l'application
		setResult((b)? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
		finish();
	}

	//---------------------------------------------------------------------------------------------------------------

	private void createDialog(String title, String text) {
		AlertDialog ad = new AlertDialog.Builder(this)
		.setPositiveButton("OK", null).setTitle(title).setMessage(text)
		.create();
		ad.show();
	}
	//---------------------------------------------------------------------------------------------------------------

	private String doLogin(final String login, final String pass)throws ClientProtocolException, IOException {

		Thread t = new Thread(){
			public void run(){
				Looper.prepare();					
				try{

					//on se connecte au serveur afin d'envoyer le login et le passwd
					HttpClient client = new DefaultHttpClient();
					HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);

					//la requete qui envoie les paramètres
					HttpPost post = new HttpPost(url);

					//on crée la liste qui contient les paramètres
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					
					//on rajoute les paramètres à la liste
					nvps.add(new BasicNameValuePair("username", login));
					nvps.add(new BasicNameValuePair("password", pass));

					post.setHeader("Content-Type", "application/x-www-form-urlencoded");

					//joindre les paramètre à la requete
					post.setEntity(new UrlEncodedFormEntity(nvps));

					//Execution du client HTTP avec le HttpPost
					HttpResponse response = client.execute(post);

					//On récupère la réponse dans un InputStream
					InputStream is = response.getEntity().getContent();
//					Log.d("myapp", "response " + response.getEntity().getContent() ); // crée des erreurs

					//On crée un bufferedReader pour pouvoir stocker le résultat dans un string
					bufferedReader  = new BufferedReader(new InputStreamReader(is));

					//On lit ligne à ligne le bufferedReader pour le stocker dans le stringBuffer
					String ligneCodeHTML = bufferedReader.readLine();
					while (ligneCodeHTML != null){
						stringBuffer.append(ligneCodeHTML);
						stringBuffer.append("\n");
						ligneCodeHTML = bufferedReader.readLine();
					}  
				}
				catch (Exception e) 
				{ 
					progressDialog.dismiss();
					createDialog("Erreur", "impossible d'établir la connexion");
				}
				Looper.loop();	
			}		
		};
		t.start();
		return stringBuffer.toString();
	}

}
