package no.kantega.jg.awtest.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 16.10.13
 * Time: 12:52
 * To change this template use File | Settings | File Templates.
 */
public class Link {
    String rel;
    String href;

    public Link(JSONObject json) throws JSONException {
        setRel(json.getString("rel"));
        setHref(json.getString("href"));
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
