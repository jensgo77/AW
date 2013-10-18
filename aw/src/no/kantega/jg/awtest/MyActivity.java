package no.kantega.jg.awtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import no.kantega.jg.awtest.domain.Entry;
import no.kantega.jg.awtest.tasks.LoadListData;

import java.util.List;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private List<Entry> dataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        boolean isOnline = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isOnline = (networkInfo != null) && networkInfo.isConnected();
        } catch( Exception e) {
            isOnline = false;
        }

        if( !isOnline) {
            errorBox("No network!");
        }

        loadListData();
    }

    private void loadListData() {

        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        new LoadListData(this).execute("");
    }

    public void listDataFinished(List<Entry> list) {

        this.dataList = list;

        final ArrayAdapter adapter = new ListAdapter(this, dataList);

        final ListView listview = (ListView) findViewById(R.id.listView);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Entry item = (Entry) parent.getItemAtPosition(position);

                Toast.makeText(parent.getContext(), item.toString(), 2000).show();

                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://preso.kantega.no/play/?" + item.getId()));
                startActivity(i);
            }
        });



        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    public void errorBox(String s) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Melding fra testapp");

        // set dialog message
        alertDialogBuilder
                .setMessage(s)
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
