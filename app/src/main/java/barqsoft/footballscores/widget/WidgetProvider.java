package barqsoft.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;

/**
 * Created by aevangelista on 15-11-22.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";
    public ScoresAdapter adapter;

    /**
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     **/

    @Override
    public void onUpdate(Context context, AppWidgetManager
            appWidgetManager,int[] appWidgetIds) {

            /*int[] appWidgetIds holds ids of multiple instance
             * of your widget
             * meaning you are placing more than one widgets on
             * your homescreen*/
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);

            appWidgetManager.updateAppWidget(appWidgetIds[i],
                    remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {

        //Layout for widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),R.layout.widget);

        remoteViews.setOnClickPendingIntent(R.id.refreshIcon, getPendingSelfIntent(context, SYNC_CLICKED));

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.widgetListView, svcIntent);
        remoteViews.setEmptyView(R.id.widgetListView, R.id.emptyView);


        return remoteViews;
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            watchWidget = new ComponentName(context, WidgetActivity.class);

            //DO SOMETHING
            Toast.makeText(context, "Widget Refreshed",
                    Toast.LENGTH_LONG).show();

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
    }


}
