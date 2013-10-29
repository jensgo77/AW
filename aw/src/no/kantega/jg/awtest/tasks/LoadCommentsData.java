package no.kantega.jg.awtest.tasks;

import android.os.AsyncTask;
import no.kantega.jg.awtest.DetailActivity;
import no.kantega.jg.awtest.domain.Comment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 18.10.13
 * Time: 13:55
 */
public class LoadCommentsData extends AsyncTask<String, Integer, List<Comment>> {

    private DetailActivity parent;

    public LoadCommentsData(DetailActivity parent) {
        this.parent = parent;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPostExecute(List<Comment> list) {
        parent.listDataFinished(list);
    }

    @Override
    protected List<Comment> doInBackground(String... params) {
        try {
            return readAll(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new ArrayList<Comment>();
    }

    public static List<Comment> readAll(String id) throws URISyntaxException, IOException, JSONException {
        String jsonResponse = LoadJsonString.readHttpString("http://preso.kantega.no/play/comments?name=" + id);
        List<Comment>  l = new ArrayList<Comment>();

        JSONArray arr = new JSONArray(jsonResponse);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject preso = arr.getJSONObject(i);

            Comment c = new Comment(preso);
            l.add(c);
        }
        return l;
    }
}
