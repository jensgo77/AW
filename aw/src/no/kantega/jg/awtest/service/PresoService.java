package no.kantega.jg.awtest.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import no.kantega.jg.awtest.domain.Entry;
import no.kantega.jg.awtest.tasks.LoadListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 21.10.13
 * Time: 13:27
 */
public class PresoService extends IntentService {

    private EntryDBHelper dbHelp;
    private ServiceScheduler scheduler;

    public PresoService() {
        super("PresoService");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        dbHelp = new EntryDBHelper(this);
        scheduler = new ServiceScheduler();

        Log.d("PresoService", "onCreate");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("PresoService", "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("PresoService", "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("PresoService", "Handeling intent start");
        // "work"
        List<Entry> list = LoadListData.doWork();
        List<Entry> toAdd = new ArrayList<Entry>();
        List<Entry> toUpdate = new ArrayList<Entry>();

        boolean test = false;
        if( test == true ) {
            dbHelp.addEntries(list);
            dbHelp.deleteAllEnties();
            dbHelp.addEntries2(list);
            dbHelp.deleteAllEnties();
            for(Entry e : list) {
                dbHelp.addEntry(e);
            }
        }

        long start = System.currentTimeMillis();
        dbHelp.prepareBatchUpdate();
        for(Entry e : list) {
            Entry old = dbHelp.getEntryByRealId(e.getId());
            if( old == null ) {
                toAdd.add(e);
            } else {  // all need update (batchupdate flag
                toUpdate.add(e);
            }
        }

        dbHelp.addEntries2(toAdd);
        for( Entry e : toUpdate ){
            dbHelp.updateEntry(e);
        }
        dbHelp.cleanupBatchUpdate();

        long end = System.currentTimeMillis();
        Log.d("PresoService", "Handeling intent end. dbhandling: " + Long.toString(end - start ));

        scheduler.beepForAnHour();
    }
}
