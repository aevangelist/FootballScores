package barqsoft.footballscores.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.ScoresProvider;
import barqsoft.footballscores.Utilies;

/**
 * Created by aevangelista on 15-12-02.
 */
public class WidgetListProvider implements RemoteViewsFactory {

    private ScoresAdapter adapter;
    private ScoresProvider provider;
    private ArrayList<WidgetListItem> listItemList = new ArrayList();
    private Context context = null;
    private int appWidgetId;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

        provider = new ScoresProvider();

        int i = 2; //Set as 2 for today's date; 3 as tomorrow's date
        Date date = new Date(System.currentTimeMillis()+((i-2)*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

        String[] fragmentdate = new String[1];
        fragmentdate[0] = mformat.format(date);

        Cursor cursor = provider.query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, fragmentdate, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    WidgetListItem listItem = new WidgetListItem();

                    listItem.setHomeTeam(cursor.getString(adapter.COL_HOME));
                    listItem.setAwayTeam(cursor.getString(adapter.COL_AWAY));
                    listItem.setHomeScore(Utilies.getScore(cursor.getInt(adapter.COL_HOME_GOALS)));
                    listItem.setAwayScore(Utilies.getScore(cursor.getInt(adapter.COL_AWAY_GOALS)));

                    listItem.setHomeLogo(Utilies.getTeamCrestByTeamName(
                            cursor.getString(adapter.COL_HOME)));
                    listItem.setAwayLogo(Utilies.getTeamCrestByTeamName(
                            cursor.getString(adapter.COL_AWAY)));

                    listItemList.add(listItem);

                    Log.d("log", "Widget Data: " + cursor.getString(adapter.COL_HOME) +
                            " VS " + cursor.getString(adapter.COL_AWAY));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
    * Similar to getView of Adapter where instead of View
    * we return RemoteViews
    */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_item);

        WidgetListItem listItem = listItemList.get(position);

        //Set up UI on list item
        remoteView.setTextViewText(R.id.homeTeamName, listItem.getHomeTeam());
        remoteView.setTextViewText(R.id.awayTeamName, listItem.getAwayTeam());
        remoteView.setTextViewText(R.id.homeTeamScore, listItem.getHomeScore());
        remoteView.setTextViewText(R.id.awayTeamScore, listItem.getAwayScore());

        remoteView.setImageViewResource(R.id.homeTeamLogo, listItem.getHomeLogo());
        remoteView.setImageViewResource(R.id.awayTeamLogo, listItem.getAwayLogo());

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
