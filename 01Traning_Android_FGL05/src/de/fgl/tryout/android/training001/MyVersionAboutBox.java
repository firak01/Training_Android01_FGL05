/**
 * 
 */
package de.fgl.tryout.android.training001;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//import biz.tekeye.abouttest.AboutBox;
/**
 * @author Fritz Lindhauer
 *
 */
public class MyVersionAboutBox { //extends AboutBox{ aber: Diese AboutBox ist so einfach nicht anpassbar genug
	 private MyVersionHandler versionHandler=null;
	 
	private MyVersionHandler getVersionHandler() {
		return versionHandler;
	}

	private void setVersionHandler(MyVersionHandler versionHandler) {
		this.versionHandler = versionHandler;
	}

	private Activity callingActivity = null;
	 
	private Activity getCallingActivity() {
		return callingActivity;
	}

	private void setCallingActivity(Activity callingActivity) {
		this.callingActivity = callingActivity;
	}

	public MyVersionAboutBox(MyVersionHandler objVersionHandler){
		 this.setVersionHandler(objVersionHandler);
	 }
	 
	 static String VersionName(Context context) {
		    try {
		      return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
		    } catch (NameNotFoundException e) {
		      return "Unknown";
		    }
		  }
	      //nicht mehr static machen
		  public void Show(Activity callingActivity) {
			  this.callingActivity = callingActivity;
			  
			  String aboutText = this.computeVersionString();
			  Log.d("FGLTEST", "Methode Show() aboutText = " + aboutText);
		      //SpannableString aboutTextSpannable = this.computeSpannableVersionString();
			  
		    //Generate views to pass to AlertDialog.Builder and to set the text
		    View about;
		    TextView tvAbout;
		    try {
		      //Inflate the custom view
		      LayoutInflater inflater = callingActivity.getLayoutInflater();
		      about = inflater.inflate(R.layout.aboutbox, (ViewGroup) callingActivity.findViewById(R.id.aboutView));
		      tvAbout = (TextView) about.findViewById(R.id.aboutText);
		    } catch(InflateException e) {
		      //Inflater can throw exception, unlikely but default to TextView if it occurs
		      about = tvAbout = new TextView(callingActivity);
		    }
		    //Set the about text
		    tvAbout.setText(aboutText);
		    
		    // Now Linkify the text
		    // Das entfernt irgendwie den normalen Text, oder ?       Linkify.addLinks(tvAbout, Linkify.ALL);
		    
		    //Build and show the dialog
		    new AlertDialog.Builder(callingActivity)
		      .setTitle("About " + callingActivity.getString(R.string.app_name))
		      .setCancelable(true)
		      .setIcon(R.drawable.ic_launcher)
		      .setPositiveButton("OK", null)
		      .setView(about)
		      .show();    //Builder method returns allow for method chaining
		  }
		  
		  private String computeVersionString(){
			  String sReturn="";
			  main:{
				  Activity callingActivity = this.getCallingActivity();
				  String sText = new String("");
				  String sVersion = new String("");
				  if(callingActivity!=null){				  
					  sText = sText + "Version " + VersionName(callingActivity)+ "\n\n"
							  					  + callingActivity.getString(R.string.about);
							  					  
  					  
  					  MyVersionHandler objVersionHandler = this.getVersionHandler();  					  
  					  if(objVersionHandler!=null){
  						  sVersion = "\n" + sVersion + objVersionHandler.readFileVersionFromRawFGL(callingActivity);					
  					  }					  					    					 
				  }
				  sReturn = sText + sVersion;
				
			
			  }//end main:
			  return sReturn;			
		  }
		  
		  private SpannableString computeSpannableVersionString(){
			//Use a Spannable to allow for links highlighting
			  String sText = this.computeVersionString();
			  SpannableString objReturn=new SpannableString(sText);
			  return objReturn;
		  }

	
	
}
