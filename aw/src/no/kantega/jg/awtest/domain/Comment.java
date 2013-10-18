package no.kantega.jg.awtest.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 18.10.13
 * Time: 13:56
 */
public class Comment implements Serializable {


    String author;
    String text;
    String videoTime;

    public Comment(JSONObject json) throws JSONException {
        author = json.getString("author");
        text = json.getString("text");
        videoTime = json.getString("videoTime");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }
}
