package no.kantega.jg.awtest.tasks;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoadJsonString {

    public LoadJsonString() {
    }

    static String readHttpString(String url) throws URISyntaxException, IOException {

        String httpString = "";


        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        HttpResponse responseGet = client.execute(get, new BasicHttpContext());
        HttpEntity resEntityGet = responseGet.getEntity();
        if (resEntityGet != null) {
            // do something with the response
            httpString = EntityUtils.toString(resEntityGet);
        }
        return httpString;
    }
}