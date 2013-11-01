package no.kantega.jg.awtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import no.kantega.jg.awtest.domain.Entry;
import no.kantega.jg.awtest.service.PresoService;
import no.kantega.jg.awtest.tasks.LoadHttpsPage;
import no.kantega.jg.awtest.tasks.LoadListData;

import java.util.List;


public class MyActivity extends Activity implements PopupMenu.OnMenuItemClickListener {
    /**
     * Called when the activity is first created.
     */

    public static final String PREFS_NAME = "no.kantega.jg.awtest.savedprops";

    private List<Entry> dataList;
    private Entry selectedItem;
    private String lastView;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        boolean isOnline = false;
        boolean isMobile = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isOnline = (networkInfo != null) && networkInfo.isConnected();
            isMobile = ConnectivityManager.TYPE_WIFI != networkInfo.getType();
        } catch( Exception e) {
            isOnline = false;
        }
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        lastView = settings.getString("lastview", "");

        if( isMobile ) {
            errorBox("No network!");
        } else {
            loadListData();
        }


        Intent i = new Intent(this, PresoService.class);
        startService(i);


    }

    private void loadListData() {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        new LoadListData(this).execute("");

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if( status == BatteryManager.BATTERY_PLUGGED_AC || status == BatteryManager.BATTERY_PLUGGED_USB) {

            new LoadHttpsPage(this).execute("https://testlink.kantega.no/index.php");

        } else {
            Toast.makeText(this, "Unplugged!!!!", 3000).show();
        }
    }


    public void httpPageData(String httpPage) {
    }

    public void listDataFinished(List<Entry> list) {

        this.dataList = list;

        final ArrayAdapter adapter = new ListAdapter(this, dataList);

        final ListView listview = (ListView) findViewById(R.id.listView);
        final MyActivity mainView = this;


        registerForContextMenu(listview);


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

        if( lastView != null ) {
            Entry lastViewEntry = null;
            for(Entry e : list) {
                if( e.getId().equals(lastView)) {
                    lastViewEntry = e;
                    break;
                }
            }
            if( lastViewEntry != null ) {
                selectedItem = lastViewEntry;
                startDetailView();
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch( menuItem.getItemId()  ) {
            case R.id.menu_webside:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://preso.kantega.no/play/?" + selectedItem.getId()));
                startActivity(i);
                break;

            case R.id.menu_detaljer:
                startDetailView();
                break;

            default:
                Log.e("testApp", "Missing implementation for: " + menuItem.getTitle());
                return false;
        }
        return true;
    }

    private void startDetailView() {
        Intent i2 = new Intent(this, DetailActivity.class);
        i2.putExtra("entry_id", selectedItem.getId());
        i2.putExtra("entry_title", selectedItem.getTitle());
        i2.putExtra("entry_desc", selectedItem.getSummary());
        i2.putExtra("entry", selectedItem);
        startActivity(i2);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listpopupmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Entry entry = dataList.get((int)info.id);

        switch (item.getItemId()) {
            case R.id.menu_webside:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://preso.kantega.no/play/?" + entry.getId()));
                startActivity(i);
                return true;
            case R.id.menu_detaljer:
                Intent i2 = new Intent(this, DetailActivity.class);
                i2.putExtra("entry_id", entry.getId());
                i2.putExtra("entry_title", entry.getTitle());
                i2.putExtra("entry_desc", entry.getSummary());
                i2.putExtra("entry", entry);
                startActivity(i2);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
