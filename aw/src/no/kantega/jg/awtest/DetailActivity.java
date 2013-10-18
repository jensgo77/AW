package no.kantega.jg.awtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import no.kantega.jg.awtest.domain.Entry;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 18.10.13
 * Time: 12:25
 */
public class DetailActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        TextView titleView = (TextView)findViewById(R.id.detail_title);
        TextView descView = (TextView)findViewById(R.id.detail_desc);

        Entry entry = null;
        if( getIntent() != null ) {
            entry = (Entry) getIntent().getSerializableExtra("entry");
        }

        if( entry != null ) {
            titleView.setText(entry.getId());
            descView.setText(entry.getSummary());
        }
    }
}