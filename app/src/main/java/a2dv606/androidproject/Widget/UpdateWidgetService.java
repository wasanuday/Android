package a2dv606.androidproject.Widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import a2dv606.androidproject.Database.DrinkDataSource;
import a2dv606.androidproject.MainWindow.MainActivity;
import a2dv606.androidproject.Settings.PrefsHelper;
import a2dv606.androidproject.R;

public class UpdateWidgetService extends Service {
    public static String SERVICE_INTENT = "service_intent";
    public static final int WIDGET_INIT = 0;
    public static final int WIDGET_UPDATE = 1;
    private int glassSize;
    private int bottleSize;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("on start command");

        int command = intent.getIntExtra(SERVICE_INTENT, -1);
        if (intent == null) {
            return START_STICKY;
        }

        switch (command) {
            case WIDGET_INIT: {
                int widget_id = intent.getIntExtra("widget_id", -1);
                int[] widgetIds = {widget_id};
                Intent widget_intent = new Intent(getApplicationContext(), WidgetProvider.class);
                widget_intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                widget_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
                sendBroadcast(widget_intent);
                break;
            }

            case WIDGET_UPDATE: {
                System.out.println("on update");
                int appWidgetId = intent.getIntExtra("widget_id", -1);

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout1);
                setupViews(appWidgetId,views);
                setupOnWidgetClick(appWidgetId,views );
            }
        }
        stopSelf();
        return START_STICKY;
    }

    private void setupOnWidgetClick(int widgetId, RemoteViews views) {

        System.out.println("on click");
        Intent intent = new Intent(UpdateWidgetService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateWidgetService.this, widgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateWidgetService.this);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setupViews(int id,RemoteViews views) {
        System.out.println("on setup");
        DrinkDataSource db = new DrinkDataSource(getApplicationContext());
        db.open();
    //    loadContainerSizePrefs();
  //      db.createTimeLog(glassSize,"glass", DateHandler.getCurrentDate(),DateHandler.getCurrentTime());
  //      db.updateConsumedWaterForTodayDateLog(glassSize);
        int perValue=  db.getConsumedPercentage();
        int consumed= db.geConsumedWaterForToadyDateLog();
        views.setTextViewText(R.id.drank_per_textview,String.valueOf(perValue)+"%");
        views.setTextViewText(R.id.drank_textview,String.valueOf(consumed)+"/"
                +PrefsHelper.getWaterNeedPrefs(getApplicationContext())+" ml");

    }

    private void loadContainerSizePrefs() {
        String glassSizeStr= PrefsHelper.getGlassSizePrefs(getApplicationContext());
        String bottleSizeStr =PrefsHelper.getBottleSizePrefs(getApplicationContext());
        this.glassSize = Integer.valueOf(glassSizeStr);
        this.bottleSize = Integer.valueOf(bottleSizeStr);

    }
}
