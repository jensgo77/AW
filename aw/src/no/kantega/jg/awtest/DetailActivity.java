package no.kantega.jg.awtest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import no.kantega.jg.awtest.domain.Comment;
import no.kantega.jg.awtest.domain.Entry;
import no.kantega.jg.awtest.service.PresoService;
import no.kantega.jg.awtest.tasks.LoadCommentsData;
import no.kantega.jg.awtest.tasks.LoadListData;
import no.kantega.jg.awtest.tasks.PutComment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 18.10.13
 * Time: 12:25
 */
public class DetailActivity extends ActionBarActivity implements View.OnClickListener {

    private Entry toShow;
    private GestureDetectorCompat mDetector;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);

        if( getIntent() != null ) {
            toShow = (Entry) getIntent().getSerializableExtra("entry");
        }

        if( toShow != null ) {
            loadComments(toShow.getId());
        }

        findViewById(R.id.detail_sendComment).setOnClickListener(this);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        // Set the gesture detector as the double tap
        // listener.

        Intent i = new Intent(this, PresoService.class);
        startService(i);

    }


    private void loadComments(String id) {

      //  findViewById(R.id.detail_progress).setVisibility(View.VISIBLE);

        new LoadCommentsData(this).execute(id);

    }


    public void httpPageData(String httpPage) {


    }

    public void listDataFinished(List<Comment> comments) {
        final ArrayAdapter adapter = new DetailListAdapter(this, comments);

        final ListView listview = (ListView) findViewById(R.id.detailListView);

        listview.setAdapter(adapter);

        TextView titleView = (TextView)findViewById(R.id.detail_title);
        TextView descView = (TextView)findViewById(R.id.detail_desc);
        titleView.setText(toShow.getId());
        descView.setText(toShow.getSummary());

        if( ! comments.isEmpty() ) {
            addNotification();
        }

    }

    private void addNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notify)
                        .setContentTitle("Preso")
                        .setContentText(toShow.getTitle());
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, DetailActivity.class);
        resultIntent.putExtra("entry", this.toShow);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(DetailActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }


    private void onSendButton(View v) {
        TextView authorView = (TextView) findViewById(R.id.detail_editAuthor);
        TextView commentView = (TextView) findViewById(R.id.detail_editComment);

        String author = authorView.getText().toString();
        String comment = commentView.getText().toString();
        Comment c = new Comment(author, comment, "");

        new PutComment(this).execute(new PutComment.PutCommentData(toShow, c));
    }

    @Override
    public void onClick(View v) {

        if( v.getId() == R.id.detail_sendComment ) {
            onSendButton(v);
        } else {
            Log.e("aw", "OnClick event without implementation!!!");
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(MyActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastview", this.toShow.getId());

        // Commit the edits!
        editor.commit();
    }

    private void showNext() {
        List<Entry> l = LoadListData.doWork();
        String id = this.toShow.getId();
        Entry found = null;
        boolean bFound = false;

        for(Entry e : l ) {
            if( bFound ) {
                found = e;
                break;
            }
            if( e.getId().equals(id)) {
                bFound = true;
            }
        }

        if( found != null ) {
            toShow = found;
            loadComments(toShow.getId());
        }
    }

    private void showPrev() {
        List<Entry> l = LoadListData.doWork();
        String id = this.toShow.getId();
        Entry found = null;
        Entry prev = null;

        for(Entry e : l ) {
            if( e.getId().equals(id)) {
                found = prev;
                break;
            }
            prev = e;
        }

        if( found != null ) {
            toShow = found;
            loadComments(toShow.getId());
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
            Log.d(DEBUG_TAG, "MotionX: " + Float.toString(velocityX) + "  MotionY: " + Float.toString(velocityY));

            if(  velocityX < 0 ) {
                showNext();
            } else {
                showPrev();
            }

            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }
}