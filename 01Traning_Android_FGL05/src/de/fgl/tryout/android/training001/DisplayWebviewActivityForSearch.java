package de.fgl.tryout.android.training001;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.os.Build;

//public class DisplaySearchWebActivity extends Activity {

//Damit eine Men�leiste angezeigt wird. Aber ActionBarActivity ist deprecated
//public class DisplaySearchWebActivity extends ActionBarActivity {

//AppCompatActivity wird wohl �ber die SupportBibiotheken eingebunden.
public class DisplayWebviewActivityForSearch extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_search_web);
		
		//SO WIRD DANN DAS FRAGMENT EINGEBUNDEN, WELCHES ALS INTERNE KLASSE HIER AUCH DEFINIERT IST.
		//WENN DAS FRAGMENT ENTSPRECHEND DEFINIERT IST tools:context="de.fgl.tryout.android.training001.DisplaySearchWebActivity$PlaceholderFragment"
		//MAN MAN MUSS DIE GEWUENSCHTEN LAYOUT-ELEMENTE AUS DER ACTIVITY IN DAS FRAGMENT VERSCHIEBEN UND DABEI BEACHTEN, DASS SIE IM FRAGEMENT in ANDEREN METHODEN DEFINIERT WERDEN.
		if (savedInstanceState == null) {
			//Merke: Das PlaceholderFragment ist hier ein interne Klasse. Code dafür siehe unten.
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		//++++++++++++++++++++++++++++++++++++++++++++++
		//Den Hintergrund der Menüleiste steuern
		// TODO: Das klappt noch nicht
		//++++++++++++++++++++++++++++++++++++++++++++++
				int iColor;
				//String alarmMessagePrefix = "Alarm";
				//if(message.startsWith(alarmMessagePrefix)){
					iColor = Color.RED;
				//}else{
				//	iColor = Color.GRAY;
				//}
				
		//FGL: Check System Version at Runtime
		if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
			//FGL: Aktiviere den Home / UP Button 
//					android.support.v7.app.ActionBar actionBar = getSupportActionBar();  //funktioniert nur in einer ActionBarActivity
//					actionBar.setDisplayHomeAsUpEnabled(true);
//					
//					//Style den Hintergrund		
//					actionBar.setBackgroundDrawable(new ColorDrawable(iColor)); // set your desired color
		}else{
Log.d("FGLTEST", "Methode sDisplayActivity.onCreate(..) - minSdkVersion is 11 or higher.");
			
			// If your minSdkVersion is 11 or higher, instead use:
			android.app.ActionBar actionBar = getActionBar();
			if(actionBar==null){
				//TODO GOO 20160818: Warum ist Action Bar NULL?
				Log.d("FGLTEST", "Methode sDisplayActivity.onCreate(..) - action bar IS NULL.");
				
			}else{
			Log.d("FGLTEST", "Methode DisplayActivity.onCreate(..) - action bar not null.");
			
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			//Style den Hintergrund			
			actionBar.setBackgroundDrawable(new ColorDrawable(iColor)); // set your desired color
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_search_web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_print){
			Log.d("FGLTEST", "Methode DisplayActivityWebViewActivityForSearch.onOptionsItemSelected(..) - PRINT.");
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing the SearchView
	 * FGL: Dieser Code basiert auf "DisplayWebviewActivityForVersion", der Lösung für Dialogbox & Webview,
	 *      welche Actity erweitert. Allerdings sind die im Fragment zu verwendenen Methoden andere, da das Fragement in einer Activity eingebunden ist.
	 */
	public static class PlaceholderFragment extends Fragment {
		private MyWebViewClient objWebViewClient=new MyWebViewClient();
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_display_search_web, container, false);
			Log.d("FGLTEST", "PlaceholderFrament.onCreateView() start.");
			
			//Hier, versuche die WebView zu füllen
			WebView wvSearch;
			wvSearch = (WebView) rootView.findViewById(R.id.webView1);
			
			if(wvSearch!=null){
				
				//Für Seiten mit JavaScript und CSS
				wvSearch.getSettings().setJavaScriptEnabled(true);
				
				 //Versuch die Scrollbar permanent zu machen.
			      wvSearch.setScrollbarFadingEnabled(false);
			      wvSearch.setScrollBarFadeDuration(0);
				
			      if(objWebViewClient!=null){
			        	wvSearch.setWebViewClient(this.objWebViewClient);
				    }else{
				    	
				    };
			      
			      
				initialisiereWebKit(wvSearch);
				wvSearch.bringToFront();
				
				 
			      
			      //TODO: Den Inhalt der Suchanfrage per Bundle übergeben.
			      //TODO: Dafür sorgen, dass dies in der gleichen WebView gestartet wird und nicht in einem neuen Browserfenster.
			      //Das passiert nun in "initalisiere Webkit"
			      //wvSearch.loadUrl("https://www.google.de");
			      //wvSearch.loadUrl("https://www.google.de/search?q=android");
				
			}	
			
			return rootView;
		}
		
		 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			//Das wird ausgeführt. 
			Log.d("FGLTEST", "PlaceholderFrament.onActivityCreated() start.");
	    }
		 
		 
		 private void initialisiereWebKit(WebView view){
				final String mimetype = "text/html";
				final String encoding = "UTF-8";
				String htmldata;
				
				//Anders als bei den Textdateien, kann so die WebViewer Datei nicht angezeigt werden.
				//int contextMenueId = R.raw.version_html_fgl;		
				//InputStream is = context.getResources().openRawResource(contextMenueId);
				
				 // Load the URL of the HTML file
		        //view.loadUrl("file:///android_asset/version_html_fgl.html");
				view.loadUrl("https://www.google.de/search?q=android");
				
				//try{
				//	if (is != null && is.available() > 0) {
				//		final byte[] bytes = new byte[is.available()];
				//		is.read(bytes);
				//		htmldata = new String(bytes);
						//anders als .loadData kann die Methode loadDataWithBaseURL auch statische Webseiten verwenden.
						//Merke: WebView hat aus Sicherheitsgr�nden strenge Einschr�nkungen, was darin funktioniert. (z.B. kein Zugriff auf Cookies oder BrowserCache)
						//       
				//		view.loadDataWithBaseURL(null, htmldata, mimetype, encoding, null);						
				//	}
				//} catch(IOException e){
					
					
				//}
				
			}
	}
}