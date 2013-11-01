package no.kantega.jg.awtest.tasks;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import no.kantega.jg.awtest.DetailActivity;
import no.kantega.jg.awtest.MyActivity;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 31.10.13
 * Time: 09:47
 */
public class LoadHttpsPage extends AsyncTask<String, Integer, String> {

    private MyActivity parent;
    final private static DefaultHttpClient client = new DefaultHttpClient();
    // to keep session

    // bør lage baseklasse  med generel get og post
    // denne bør ha egen på og avlogging for formbasert

    // vider test, basic autentication + client sertificate.

    public LoadHttpsPage(MyActivity parent) {
        this.parent = parent;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPostExecute(String s) {
        parent.httpPageData(s);
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            //s = loginFormTestlink();
            //return readHttpString(params[0]);
            s = basicAuthentication("http://10.80.8.106:8080/dummyws.simulerpensjon/config.jsp", "plain", "plain");

        } /*catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
            */
        finally {

        }
        return s;
    }

    String readHttpString(String url) throws URISyntaxException, IOException {

        String httpString = "";
        try {
            URI uri = new URI(url);
            HttpGet get = new HttpGet();
            get.setURI(uri);

            HttpResponse responseGet = client.execute(get);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                // do something with the response
                httpString = EntityUtils.toString(resEntityGet);
            }

        } catch( Exception e) {
            Log.e("presco", "https error: " + e.toString());
        }
        finally {

        }
        Log.i("presco", "httpresult: " + httpString);
        return httpString;
    }

    private String basicAuthentication(String url, String user, String pwd) {

        String httpString = "";
        try {
            HttpGet request = new HttpGet(url);

            String basicAuth = "Basic " + new String(Base64.encode((user + ":" + pwd).getBytes(),Base64.NO_WRAP ));
            request.setHeader("Authorization", basicAuth);

            HttpResponse responseGet = client.execute(request);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                // do something with the response
                httpString = EntityUtils.toString(resEntityGet);
            }
        } catch (ClientProtocolException e) {
            Log.e("presco", "https error: " + e.toString());
        } catch (IOException e) {
            Log.e("presco", "https error: " + e.toString());
        } finally {

        }
        Log.i("presco", "httpresult: " + httpString);
        return httpString;
    }

    private String loginFormTestlink() {

        String httpString = "";

        try {
            HttpPost loginRequest = new HttpPost("https://localhost:8080/login.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("tl_login", "plain"));
            nameValuePairs.add(new BasicNameValuePair("tl_password", "plain"));
            loginRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(loginRequest);
            HttpEntity resEntityGet = response.getEntity();
            if (resEntityGet != null) {
                // do something with the response
                httpString = EntityUtils.toString(resEntityGet);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClientProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return httpString;
    }

}
