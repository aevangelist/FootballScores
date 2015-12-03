package barqsoft.footballscores.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


import java.util.ArrayList;

import barqsoft.footballscores.R;

/**
 * Created by aevangelista on 15-12-02.
 */
public class WidgetListProvider implements RemoteViewsFactory {

    private ArrayList listItemList = new ArrayList();
    private Context context = null;
    private int appWidgetId;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        for (int i = 0; i < 10; i++) {
            WidgetListItem listItem = new WidgetListItem();
            listItem.homeTeam = "LALALA";
            listItem.awayTeam = "ALALLALA";
            listItem.homeScore = "5";
            listItem.awayScore = "5";

            listItemList.add(listItem);
        }

    }

    @Override
    public void onCreate() {

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

        WidgetListItem listItem = (WidgetListItem) listItemList.get(position);

        //Set up UI on list item
        remoteView.setTextViewText(R.id.homeTeamName, listItem.homeTeam);
        remoteView.setTextViewText(R.id.awayTeamName, listItem.awayTeam);
        remoteView.setTextViewText(R.id.homeTeamScore, listItem.homeScore);
        remoteView.setTextViewText(R.id.awayTeamScore, listItem.awayScore);


        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

}
