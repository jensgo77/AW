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
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import no.kantega.jg.awtest.domain.Entry;
import no.kantega.jg.awtest.tasks.LoadListData;

import java.util.List;


public class MyActivity extends Activity implements PopupMenu.OnMenuItemClickListener{
    /**
     * Called when the activity is first created.
     */

    private List<Entry> dataList;
    private Entry selectedItem;

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
        final MyActivity mainView = this;

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Entry item = (Entry) parent.getItemAtPosition(position);
                selectedItem = item;
                Toast.makeText(parent.getContext(), item.toString(), 2000).show();

                // menu?
                PopupMenu popup = new PopupMenu(parent.getContext(), view);

                MenuInflater inflater = popup.getMenuInflater();
                popup.setOnMenuItemClickListener(mainView);
                inflater.inflate(R.menu.listpopupmenu, popup.getMenu());
                popup.show();


            }
        });

        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch( menuItem.getItemId()  ) {
            case R.id.menu_webside:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://preso.kantega.no/play/?" + selectedItem.getId()));
                startActivity(i);
                break;

            case R.id.menu_detaljer:
                Intent i2 = new Intent(this, DetailActivity.class);
                i2.putExtra("entry_id", selectedItem.getId());
                i2.putExtra("entry_title", selectedItem.getTitle());
                i2.putExtra("entry_desc", selectedItem.getSummary());
                i2.putExtra("entry", selectedItem);
                startActivity(i2);
                break;

            default:
                Log.e("testApp", "Missing implementation for: " + menuItem.getTitle());
                return false;
        }
        return true;
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
