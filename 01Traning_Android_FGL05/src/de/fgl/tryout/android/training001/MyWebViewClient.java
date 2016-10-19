package de.fgl.tryout.android.training001;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**Mit dem eigenene WebViewClient sicherstellen:
 * - dass auch Links innerhalb der WebView ge�ffnet werden und nicht im Standardbrowser.
 * @author Fritz Lindhauer
 *
 */
public class MyWebViewClient extends WebViewClient{

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String sUrl) {
            	//Merke: WebView hat aus Sicherheitsgründen strenge Einschränkungen, was darin funktioniert. (z.B. kein Zugriff auf Cookies oder BrowserCache)
                view.loadUrl(sUrl);
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
}
