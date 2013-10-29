package no.kantega.jg.awtest.tasks;

import android.os.AsyncTask;
import no.kantega.jg.awtest.DetailActivity;
import no.kantega.jg.awtest.domain.Comment;
import no.kantega.jg.awtest.domain.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 22.10.13
 * Time: 12:58
 */
public class PutComment extends AsyncTask<PutComment.PutCommentData, Integer, Boolean> {

    private DetailActivity parent;

    public static class PutCommentData {
        public Entry e;
        public Comment newComment;

        public PutCommentData(Entry e, Comment c) {
            this.e = e;
            this.newComment = c;
        }
    }

    public PutComment(DetailActivity parent) {
        this.parent = parent;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onPostExecute(Boolean b) {
    }

    @Override
    protected Boolean doInBackground(PutCommentData... params) {
        try {
            return saveComment(params[0].e.getId(), params[0].newComment);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    private Boolean saveComment(String id, Comment newComment) throws IOException, JSONException {
        String url = "http://preso.kantega.no/play/comments";

        String jsonResponse = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("name", id));
        nameValuePairs.add(new BasicNameValuePair("author", newComment.getAuthor()));
        nameValuePairs.add(new BasicNameValuePair("comment", newComment.getText()));
        nameValuePairs.add(new BasicNameValuePair("videoTime", newComment.getVideoTime()));
        nameValuePairs.add(new BasicNameValuePair("systemTime", Long.toString(System.currentTimeMillis())));
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

        HttpResponse responseGet = client.execute(post);
        HttpEntity resEntityGet = responseGet.getEntity();
        if (resEntityGet != null) {
            // do something with the response
            jsonResponse = EntityUtils.toString(resEntityGet);
        }



        List<Comment>  l = new ArrayList<Comment>();

        JSONArray arr = new JSONArray(jsonResponse);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject preso = arr.getJSONObject(i);

            Comment c = new Comment(preso);
            l.add(c);
        }

        return true;
    }
}

