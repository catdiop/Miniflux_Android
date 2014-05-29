package enseirb.t2.miniflux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
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
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.projet.miniflux.R;

public class SubscriptionActivity extends Activity {

	private StringBuffer stringBuffer = new StringBuffer("");

	private static final String url = "http://cdiop.rmorpheus.enseirb.fr/Miniflux/rest/user/register";

	public static ProgressDialog progressDialog; // pour la gestion d'erreur

	private EditText UserEditText;
	private EditText PassEditText;
	private EditText ConfirmEditText;

	@SuppressWarnings("unused")
	private static final String LOG_TAG = "Log : ";


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

		//webView = (WebView) findViewById(R.id.website);

		// Récupération des éléments de la vue définis dans le xml
		UserEditText = (EditText)findViewById(R.id.username);
		PassEditText = (EditText)findViewById(R.id.password);
		ConfirmEditText = (EditText)findViewById(R.id.confirm);

		Button button =(Button)findViewById(R.id.subscription);

		button.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SetJavaScriptEnabled")
			public void onClick(View v) {

				int usersize = UserEditText.getText().length();
				int passsize = PassEditText.getText().length();
				int confirmsize = ConfirmEditText.getText().length();

				String login = UserEditText.getText().toString();
				String pass = PassEditText.getText().toString();
				String confirm = ConfirmEditText.getText().toString();

				//si les  champs sont remplis
				if( usersize>0 && passsize>0 && confirmsize>0 && pass.equals(confirm)==true )
				{
					progressDialog.show();

					//on appelle la fonction dologin qui va communiquer av le serveur
					try {
						doLogin(login,pass);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				else if(pass.equals(confirm)==false && passsize>0){
					createDialog("Erreur","Recommencez les deux mots de passe ne sont pas les mêmes");
				}
				else
					createDialog("Erreur","Entrez votre login et mot de passe");
			}
		}
				);


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

	@SuppressWarnings("unused")
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
					StatusLine sl=response.getStatusLine();
					int responseCode = sl.getStatusCode();
					if(responseCode==200) {
						progressDialog.dismiss();
						Intent i=new Intent(SubscriptionActivity.this, HomeActivity.class);
						startActivity(i);
					}
					else if(responseCode==403) {
						progressDialog.dismiss();
						createDialog("Erreur", "Le pseudo est déjà utilisé");
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
