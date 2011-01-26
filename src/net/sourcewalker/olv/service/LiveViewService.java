package net.sourcewalker.olv.service;

import net.sourcewalker.olv.LiveViewPreferences;
import net.sourcewalker.olv.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This service hosts and controls the thread communicating with the LiveView
 * device.
 * 
 * @author Robert &lt;xperimental@solidproject.de&gt;
 */
public class LiveViewService extends Service {

    public static final String ACTION_START = "start";
    public static final String ACTION_STOP = "stop";

    private static final int SERVICE_NOTIFY = 100;

    private static final String TAG = "LiveViewService";

    private LiveViewThread workerThread = null;
    private Notification notification;

    /*
     * (non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        notification = new Notification(R.drawable.icon,
                "LiveView connected...", System.currentTimeMillis());
        Context context = getApplicationContext();
        CharSequence contentTitle = getString(R.string.app_name);
        CharSequence contentText = getString(R.string.notify_service_running);
        Intent notificationIntent = new Intent(this, LiveViewPreferences.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            startThread();
        } else if (ACTION_STOP.equals(action)) {
            stopThread();
        }
        return START_NOT_STICKY;
    }

    /**
     * Starts the worker thread if the thread is not already running. If there
     * is a thread running that has already been stopped then a new thread is
     * started.
     */
    private void startThread() {
        if (workerThread == null || !workerThread.isLooping()) {
            startForeground(SERVICE_NOTIFY, notification);

            workerThread = new LiveViewThread(this);
            workerThread.start();
        }
    }

    /**
     * Signals the current worker thread to stop itself. If no worker thread is
     * available then nothing is done.
     */
    private void stopThread() {
        if (workerThread != null && workerThread.isAlive()) {
            try {
                workerThread.stopLoop();
                workerThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Interrupted while waiting for worker.");
            } finally {
                stopForeground(true);
                stopSelf();
            }
        }
    }

}
