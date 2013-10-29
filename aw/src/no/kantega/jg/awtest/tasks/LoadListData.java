package no.kantega.jg.awtest.tasks;

import android.os.AsyncTask;
import no.kantega.jg.awtest.MyActivity;
import no.kantega.jg.awtest.domain.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class LoadListData extends AsyncTask<String, Integer, List<Entry>> {
    MyActivity parent;

    static List<Entry> globalData = null;
    static final Integer lock = new Integer(0);

    public LoadListData(MyActivity a) {
        parent = a;
    }
    protected List<Entry> doInBackground(String... noparam) {

        return doWork();
    }

    static public List<Entry> doWork() {

        List<Entry> toReturn = null;
        synchronized (lock) {
            if( globalData == null ) {
                try {
                    globalData = toReturn = readAll();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch( Exception e) {
                    e.printStackTrace();
                }
            } else {
                toReturn = globalData;
            }
        }
        return toReturn != null ? toReturn : new ArrayList<Entry>();
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(List<Entry> list) {
        parent.listDataFinished(list);
    }

    static private List<Entry> readAll() throws URISyntaxException, IOException, JSONException {

        String jsonResponse = LoadJsonString.readHttpString("http://preso.kantega.no/feed.json");
        List<Entry>  l = new ArrayList<Entry>();


            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray entries = jsonObject.getJSONArray("entries");
            for (int i = 0; i < entries.length(); i++) {
                JSONObject preso = entries.getJSONObject(i);

                Entry e = new Entry(preso);
                l.add(e);

                //LoadCommentsData.readAll(e.getId());

            }

        return l;
    }

}