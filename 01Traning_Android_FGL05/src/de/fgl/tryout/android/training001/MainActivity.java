package de.fgl.tryout.android.training001;


import basic.zBasic.util.datatype.string.StringZZZ;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Aus developer.android.com Training/Building Your First App
 * @Date   2014-05-06
 * @author Fritz Lindhauer
 *
 */
public class MainActivity extends ActionBarActivity {
	//public final static String EXTRA_MESSAGE = "de.fgl.tryout.android.training001.MainAcitvity.MESSAGE";
	//public final static String RESUME_MESSAGE = "de.fgl.tryout.android.training001.MainAcitvity.RESUMEMESSAGE";
	//public final static String RESUME_MESSAGE_BUNDLE = "de.fgl.tryout.android.training001.MainAcitvity.RESUMEMESSAGEBUNDLE";
	//private final String KEY_MESSAGE_CURRENT="currentMessage";
	
	//Folgende Texte werden beim Zurückkehren aus der "DisplayMessageActivity" an den Sende String gehängt. Je nachdem welcher Weg gewählt wurde.
	//private final String MESSAGE_ADDITION_VARIABLE="(wiederhergestellt per Variable)";
	//private final String MESSAGE_ADDITION_BUNDLE="(wiederhergestellt per Intent und Bundle)";
	//private final String MESSAGE_ADDITION_INTENT="(wiederhergestellt per Intent)";
	//private final String MESSAGE_ADDITION_RESULT="(als Result)";
	private String sMessageCurrent;
 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the user interface layout for this Activity
	    // The layout file is defined in the project res/layout/main_activity.xml file
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		 } else {
			//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
			//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
			//            und Eclipse neu starten muss.
			Log.d("FGLSTATE", "onCreate() wurde aktiviert. MIT SAVEDINSTANCESTATE vorhanden");
			 
        	//Notwendiger Zweig um Persistierung zurückzuholen. Siehe auch onResume().
        	String sMessageCurrent = (String) savedInstanceState.getSerializable(MyMessageHandler.KEY_MESSAGE_CURRENT);
        	Log.d("FGLSTATE", "onCreate(): sMessageCurrent = " + sMessageCurrent);
			
        	if(sMessageCurrent!=null){
	        	this.setMessageCurrent(sMessageCurrent);
	        	
	        	//Sollte man nun irgendwie den String zurück-/einsetzen?
	        	EditText editText = (EditText) findViewById(R.id.edit_message);
	    		editText.setText(sMessageCurrent + " (wiederhergestellt)");
        	}
        	
        }
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		return super.onOptionsItemSelected(item);
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
	    //Start an intent
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		
		//Besser als das Standard String.replace und Pattern zu verwenden ist hier die JAZKernel-Hilfsklasse
		message = StringZZZ.replace(message, MyMessageHandler.MESSAGE_ADDITION_VARIABLE, "");
		message = StringZZZ.replace(message, MyMessageHandler.MESSAGE_ADDITION_INTENT, "");
		message = StringZZZ.replace(message, MyMessageHandler.MESSAGE_ADDITION_BUNDLE, "");	
		message = StringZZZ.replace(message, MyMessageHandler.MESSAGE_ADDITION_RESULT,"");
		Log.d("FGLSTATE", "sendessage(): message nach der Normierung = " + message);
		
		//Speichere die message in eine lokale Variable. Grund: So kann man sie dann wegsichern wenn sich der State des Geräts ändert.
		this.setMessageCurrent(message);
				
		intent.putExtra(MyMessageHandler.EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	
	/** Called when the user clicks the SendForResult button */
	public void sendMessageForResult(View view) {
	    //Start an intent
		Intent intent = new Intent(this, DisplayMessageActivityForResult.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		
		message = MyMessageHandler.createNormedMessage(message);
		
		//Besser als das Standard String.replace und Pattern zu verwenden ist hier die JAZKernel-Hilfsklasse		
		Log.d("FGLSTATE", "sendessageForResult(): message nach der Normierung = " + message);
		
		//Speichere die message in eine lokale Variable. Grund: So kann man sie dann wegsichern wenn sich der State des Geräts ändert.
		this.setMessageCurrent(message);
				
		intent.putExtra(MyMessageHandler.EXTRA_MESSAGE, message);
		startActivityForResult(intent,1);
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * 27.07.2016 09:09:40 Fritz Lindhauer
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("FGLSTATE", "onActivityResult(): START mit requestCode='" + requestCode + "' | resultCode='"+resultCode+"'");
	    super.onActivityResult(requestCode, resultCode, data);	    
	    if (requestCode == 1) {
	    		String stredittext=data.getStringExtra(MyMessageHandler.EXTRA_MESSAGE);
	         if(resultCode == RESULT_OK){	             
	             Log.d("FGLSTATE", "onActivityResult(): result OK. message  empfangen = " + stredittext);
	             this.setMessageCurrent(stredittext);
	         }else{	        	
	        	 Log.d("FGLSTATE", "onActivityResult(): result nicht OK. message  empfangen = " + stredittext);
	         }
	    }
	} 
	
	/**
	 * @param message
	 * 15.07.2016 08:26:09 Fritz Lindhauer
	 */
	private void setMessageCurrent(String message) {
		this.sMessageCurrent= message;
		Log.d("FGLSTATE", "setMessageCurrent() für '" + message + "'");
		
	}
	private String getMessageCurrent(){
		Log.d("FGLSTATE", "getMessageCurrent() für '" + this.sMessageCurrent + "'");
		return this.sMessageCurrent;		
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 * 
	 * SHIFT+ALT+J ist die Tastenkombination für automatische Kommentare.
	 * Nun muss das entsprechene Template angepasst werden.
	 *  
	 */
	@Override
	public void onPause(){
		super.onPause();
		//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
		
		//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
		//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
		//            und Eclipse neu starten muss.
		Log.d("FGLSTATE", "onPause() wurde aktiviert");
		
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent)
	 * 18.07.2016 08:46:20 Fritz Lindhauer
	 * Versuch dadurch den neuen Intent zu verwenden und nicht einen vorher an die activity iergendwie erzeugten Intent
	 * http://stackoverflow.com/questions/6352281/getintent-extras-always-null
	 */
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    Log.d("FGLSTATE", "onNewIntent() wurde aktiviert");
	    setIntent(intent);
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 * 14.07.2016 08:06:14 Fritz Lindhauer
	 */
	public void onResume(){
		//super.onResume();
		//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
		
		//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
		//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
		//            und Eclipse neu starten muss.
		Log.d("FGLSTATE", "onResume() wurde aktiviert");
		
		
		//Versuche einen gespeicherten Text wiederherszustellen.
		//Merke: Beim einfachen Wechseln zurück wird dann nicht onCreate() aufgerufen, sondern onResume(), 
		//       darum gehört der Code hierher, ABER: savedInstanceState ist hier nicht vorhanden.
    	//String sMessageCurrent = (String) savedInstanceState.getSerializable(KEY_MESSAGE_CURRENT);
    	//this.setMessageCurrent(sMessageCurrent);
    	
		//Damit das funktioniert muss onRestoreInstanceState() ausgeführt werden und es muss die lokale Property wieder gefüllt worden sein.
		String sMessageCurrent = this.getMessageCurrent();
		Log.d("FGLSTATE", "onResume(): Wert per Variable sMessageCurrent = " + sMessageCurrent);
		
		if(sMessageCurrent!=null){
	    	//Sollte man nun irgendwie den String zurück-/einsetzen?
	    	EditText editText = (EditText) findViewById(R.id.edit_message);
			editText.setText(sMessageCurrent + MyMessageHandler.MESSAGE_ADDITION_VARIABLE);
		}else{
			//Das ist der Normalefall: Die Variable ist nämlich weg.
			//Nun Versuch sie in inStop() über einen Intent.getExtras zu sichern und hier wiederherzustellen
			sMessageCurrent=getIntent().getStringExtra(MyMessageHandler.RESUME_MESSAGE);
			Log.d("FGLSTATE", "onResume(): Wert per intent sMessageCurrent = " + sMessageCurrent);
			
			//DAS FUNKTIONIERT AUCH NICHT!!!
			if(sMessageCurrent!=null){
				EditText editText = (EditText) findViewById(R.id.edit_message);
				editText.setText(sMessageCurrent + MyMessageHandler.MESSAGE_ADDITION_INTENT);
			}else{
				Bundle bundle = getIntent().getExtras();
				if(bundle!=null){
					sMessageCurrent = bundle.getString(MyMessageHandler.RESUME_MESSAGE_BUNDLE);
					Log.d("FGLSTATE", "onResume(): Wert per intent und bundle sMessageCurrent = " + sMessageCurrent);
					
					EditText editText = (EditText) findViewById(R.id.edit_message);
					editText.setText(sMessageCurrent + MyMessageHandler.MESSAGE_ADDITION_BUNDLE);
				}else{
					Log.d("FGLSTATE", "onResume(): Bundle ist auch im neuen intent leer");
					
				}
			}
		}
		super.onResume();
	}
	
	public void onStop(){
		//super.onStop();
		//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
		
		//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
				//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
				//            und Eclipse neu starten muss.
				Log.d("FGLSTATE", "onStop() wurde aktiviert");
				
				
		//Versuche für onResume() den Stringwert zu retten, der dann in onResume() ausgelesen werden soll.
				//ABER TODO GOON 20160715: Das klappt nicht!!!
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();			
		Log.d("FGLSTATE", "onStop() - Sicher Message in intent weg: " + message);
		getIntent().putExtra(MyMessageHandler.RESUME_MESSAGE, message);
		
		//ABER: So kann der Wert nicht in der gleichen Activity wiedergeholte werden.

		//Versuch 2:
		Bundle bundle = new Bundle();
        bundle.putString(MyMessageHandler.RESUME_MESSAGE_BUNDLE, message);
        getIntent().putExtras(bundle);
        
        //Versuch 3: Mit neuem Intent UND der überschriebenenen Methode onNewIntent()
        Log.d("FGLSTATE", "onStop() - Sicher Message in NEUEM intent weg: " + message);
        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle02 = new Bundle();
        bundle02.putString(MyMessageHandler.RESUME_MESSAGE_BUNDLE, message);
        intent.putExtras(bundle02);
		
        super.onStop();
	}
	
	public void onRestart(){
		super.onRestart();
		//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
		
		//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
				//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
				//            und Eclipse neu starten muss.
				Log.d("FGLSTATE", "onRestart() wurde aktiviert");
	}
	
	public void onStart(){
		super.onStart();
		//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
		
		//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
				//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
				//            und Eclipse neu starten muss.
				Log.d("FGLSTATE", "onStart() wurde aktiviert");
	}
	
	public void onSaveInstanceState(Bundle outState){
		
		//NOTWENDIG ZUM PERSISTIERN DER DATEN IM BUNDLE
				//super.onSaveInstanceState(outState);
				//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
				
				//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
						//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
						//            und Eclipse neu starten muss.
						Log.d("FGLSTATE", "onSaveInstanceState() wurde aktiviert");
				
				
				//GRUND: SERIALIZIERUNG geht nur mit expliziter Klasse, ggf. reicht auch hier ein "dreckiger" Typecast
				String sMessage = this.getMessageCurrent();
				outState.putSerializable(MyMessageHandler.KEY_MESSAGE_CURRENT, sMessage); //liste darf nicht das Interface sein, sondern muss explizit die Klasse ArrayList sein.
				super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle inState){
		//NOTWENDIG ZUM ZURUECKHOLEN DER PERSISTIERTEN DATEN AUS DEM BUNDEL. 
		//WIRD ABER NUR BEI onCreate() aufgerufen
		
		//Merke onResume() oder onStart() haben kein Bundle als Parameter
		//Bei onResume() ... WIE KANN MAN DA DIE DATEN ZURUECKHOLEN?????
		//super.onRestoreInstanceState(inState);
		//FGL: Rufe beim Überschreiben dieser Event-Methoden IMMER die Methode der Elternklasse auf.
		
		//FGL: Versuch etwas in LogCat auszugeben. Dazu muss der Emulator/das Gerät verbunden sein.
				//     Merke: Hatte man ggfs. mehrere Emulatoren am Laufen, kann es sein, dass man alle beenden muss
				//            und Eclipse neu starten muss.
				Log.d("FGLSTATE", "onRestoreInstanceState() wurde aktiviert");
		
		
		//GRUND: SERIALIZIERUNG geht nur mit expliziter Klasse, ggf. reicht auch hier ein "dreckiger" Typecast		
		String sMessage = (String) inState.get(MyMessageHandler.KEY_MESSAGE_CURRENT);
		Log.d("FGLSTATE", "onRestoreInstanceState() mit sMessage="+sMessage);
		this.setMessageCurrent(sMessage);	
		
		//klappst so nicht, versuche die Methode der Elternklasse danach aufzurufen.
		super.onRestoreInstanceState(inState);
	}
	
	
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
