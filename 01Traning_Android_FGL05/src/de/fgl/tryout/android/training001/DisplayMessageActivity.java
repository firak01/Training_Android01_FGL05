package de.fgl.tryout.android.training001;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class DisplayMessageActivity extends ActionBarActivity { //Activity	
	private String sMessageCurrent;
	
	/**
	 * @param message
	 * 15.07.2016 08:26:09 Fritz Lindhauer
	 */
	private void setMessageCurrent(String message) {
		this.sMessageCurrent= message;
		Log.d("FGLSTATE", this.getClass().getSimpleName()+". setMessageCurrent() für '" + message + "'");
		
	}
	private String getMessageCurrent(){
		Log.d("FGLSTATE", this.getClass().getSimpleName()+". getMessageCurrent() für '" + this.sMessageCurrent + "'");
		return this.sMessageCurrent;		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//++++++++++++++++++++++++++++++++++++++++++++++
		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MyMessageHandler.EXTRA_MESSAGE);
		this.setMessageCurrent(message);
		//++++++++++++++++++++++++++++++++++++++++++++++
		int iColor;
		String alarmMessagePrefix = "Alarm";
		if(message.startsWith(alarmMessagePrefix)){
			iColor = Color.RED;
		}else{
			iColor = Color.GRAY;
		}
		
		//FGL: Check System Version at Runtime
		if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			//FGL: Aktiviere den Home / UP Button 
//			android.support.v7.app.ActionBar actionBar = getSupportActionBar();  //funktioniert nur in einer ActionBarActivity
//			actionBar.setDisplayHomeAsUpEnabled(true);
//			
//			//Style den Hintergrund		
//			actionBar.setBackgroundDrawable(new ColorDrawable(iColor)); // set your desired color
		}else{
			// If your minSdkVersion is 11 or higher, instead use:
			android.app.ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);//To allow Up navigation with the app icon in the action bar
			
			//Style den Hintergrund			
			actionBar.setBackgroundDrawable(new ColorDrawable(iColor)); // set your desired color		
		}
			
		
		// Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(message);

	    // Set the text view as the activity layout
	    setContentView(textView);

	    //FGL: Wenn man die View im Layout-Editor erstellt, kann man eine ID vergeben, die hier benutzt werden kann. 
	    //Initialize member TextView so we can manipulate it later
	    //mTextView = (TextView) findViewById(R.id.text_message);
		//setContentView(R.layout.activity_display_message);

		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	//Wird für diese App nicht benötigt
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.display_message, menu);
//		return true;
//	}

	//FGL: Training/Adding the Action Bar / Adding Action Buttons
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("FGLSTATE", "onOptionsItemSelected()");
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
		
		
		//### Fang den "Zurück" Button der ActivityActionBar ab. #####
		//Ziel: Übergib an die Ausgangsaktivität wieder den Wert. 
		//a) Wenn über den Zurück-Button des Geräts gearbeitet wird, ist es nicht notwendig. 
		//   Dann bleibt der Wert als Variable in der Activity vorhanden.
		//b) Wenn über den Zurück-Button in der ActionBar gearbeitet wird, sind die Werte in onResume() nur entgegenzunehmen,
		//   durch einen Intent, in dem man den Wert speichert.
		//c) IDEE: Versuche aus der Hauptaktivität mal die DisplayMessageActivity02 aufzurufen mit
		//         startActivityForResult(). Vielleicht geht es dann einfacher.
		//
		if(id==16908332){
			//If Abfrage, weil in der Switch-Case Anweisung der Vergleich nicht zu klappen scheint.
			Log.d("FGLSTATE", "onOptionsItemSelected() für speziell definierte actionBarId gefunden.");
			
			//Versuch X: Gib an die aufgerufene Funktion den Wert zurück
    		Bundle bundle = new Bundle();
            bundle.putString(MyMessageHandler.RESUME_MESSAGE_BUNDLE, this.getMessageCurrent());
            //natürlich nicht in den Intent Packen, der dieser Activity beim Start mitgegeben worden ist getIntent().putExtras(bundle);
            
            //Start an intent mit dem Ziel diesen in der onResume Methpde entgegenzunehmen.
    		Intent intent = new Intent(this, MainActivity.class);	    		
    		intent.putExtras(bundle);
      		startActivity(intent); //Merke: Nachteil ist, das jeder Activity-Start quasi in eine History kommt. 
      		                       //       Das bedeutet, dass der Zurück-Button des Geräts erst einmal alle Activities aus der Historie durchläuft,
      		                       //       wenn man ihn in der Hauptmaske betätigt.
    		
			
		}else{
			Log.d("FGLSTATE", "onOptionsItemSelected() für speziell definierte actionBarId NICHT gefunden.");
			
			// Handle presses on the action bar items
		    switch (id) {
		        case R.id.action_search:
		            openSearch();
		            return true;
		        case R.id.action_settings:
		            openSettings();
		            return true;
		        case R.id.home:
		        	Log.d("FGLSTATE", "onOptionsItemSelected() für HOME item.id= '" + id + "'");
		        case R.id.homeAsUp:
		        	Log.d("FGLSTATE", "onOptionsItemSelected() für HOMEASUP item.id= '" + id + "'");
		        case R.id.up:
		        	Log.d("FGLSTATE", "onOptionsItemSelected() für HUP item.id= '" + id + "'");
		        case 16908332:
		        	//DAS WIRD AUS iregendeinem Grund nicht ausgeführt. Darum in den if-Abfrage vorneweg verlagert.
		        	Log.d("FGLSTATE", "onOptionsItemSelected() für speziell definierte actionBarId ohne in R-Klasse vohranden zu sein: item.id= '" + id + "'");	        		        
		        default:
		        	Log.d("FGLSTATE", "onOptionsItemSelected() für default item.id= '" + id + "'");
		            return super.onOptionsItemSelected(item);
		    }
		}
		
		return true;
	}

	
	private void openSettings() {
		// TODO Auto-generated method stub
		
	}

	private void openSearch() {
		// TODO Auto-generated method stub
		
	}

	//Folgendes wird nicht benötigt:
	/**
	 * A placeholder fragment containing a simple view.
	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_display_message,
//					container, false);
//			return rootView;
//		}
//	}

	//FGL für neue Buttons in der Action Bar notwendig
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.display_message, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//FGL für die Reaktion auf neue Buttons in der ActionBar notwendig
	
}
