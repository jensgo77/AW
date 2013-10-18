package no.kantega.jg.awtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import no.kantega.jg.awtest.domain.Comment;
import no.kantega.jg.awtest.domain.Entry;
import no.kantega.jg.awtest.tasks.LoadCommentsData;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 18.10.13
 * Time: 12:25
 */
public class DetailActivity extends Activity {

    private Entry toShow;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        if( getIntent() != null ) {
            toShow = (Entry) getIntent().getSerializableExtra("entry");
        }

        if( toShow != null ) {
            loadComments(toShow.getId());
        }
    }


    private void loadComments(String id) {

      //  findViewById(R.id.detail_progress).setVisibility(View.VISIBLE);

        new LoadCommentsData(this).execute(id);
    }

    public void listDataFinished(List<Comment> comments) {
        final ArrayAdapter adapter = new DetailListAdapter(this, comments);

        final ListView listview = (ListView) findViewById(R.id.detailListView);

        listview.setAdapter(adapter);

        TextView titleView = (TextView)findViewById(R.id.detail_title);
        TextView descView = (TextView)findViewById(R.id.detail_desc);
        titleView.setText(toShow.getId());
        descView.setText(toShow.getSummary());

     //  findViewById(R.id.detail_progress).setVisibility(View.GONE);

    }


}