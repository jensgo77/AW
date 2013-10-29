package no.kantega.jg.awtest.servic;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 21.10.13
 * Time: 13:27
 */
public class PresoService extends IntentService {

    public PresoService() {
        super("PresoService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {


    }
}
