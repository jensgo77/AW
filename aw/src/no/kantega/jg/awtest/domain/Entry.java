package no.kantega.jg.awtest.domain;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 16.10.13
 * Time: 12:49
 */
public class Entry implements Serializable {

    String id;
    String summary;
    String title;
    String updated;
    List<Link> links;

    public Entry(JSONObject preso) throws JSONException {

        setId(preso.getString("id"));
        setSummary(preso.getString("summary"));
        setTitle(preso.getString("title"));
        setUpdated(preso.getString("updated"));
        setLinks(new ArrayList<Link>());

        JSONArray jLinks = preso.getJSONArray("links");
        for (int i = 0; i < jLinks.length(); i++) {
            JSONObject jLink = jLinks.getJSONObject(i);
            Link l = new Link(jLink);
            addLink(l);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public void addLink(Link link) {
        if( links == null) {
            links = new ArrayList<Link>();
        }
        links.add(link);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append("id: ");
        b.append(getId());
        b.append(", title: ");
        b.append(getTitle());

        return b.toString();
    }
}
