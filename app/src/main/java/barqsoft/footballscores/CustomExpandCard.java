package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by aevangelista on 15-11-18.
 */

public class CustomExpandCard extends CardExpand {

    private Context context;
    private String FOOTBALL_SCORES_HASHTAG = "#FootballScores";

    public String matchDay = "";
    public String league = "";

    public String homeTeam = "";
    public String awayTeam = "";

    public String score = "";


    //Use your resource ID for your inner layout
    public CustomExpandCard(Context context) {
        super(context, R.layout.scores_list_item_expand);
        this.context = context;
    }

    public void setMatchDay(String m){
        this.matchDay = m;
    }

    public void setLeague(String l){
        this.league = l;
    }

    public void setHomeTeam(String s){
        this.homeTeam = s;
    }

    public void setAwayTeam(String s){
        this.awayTeam = s;
    }

    public void setScore(String s){
        this.score = s;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        //Retrieve TextView elements
        TextView tx1 = (TextView) view.findViewById(R.id.description);

        //Build the text
        String s = this.matchDay + "\n" + "League: " + this.league + "\n";

        //Set value in text views
        if (tx1 != null) {
            tx1.setText(s);
        }

        //Retrieve the Share button
        LinearLayout sharePanel = (LinearLayout) view.findViewById(R.id.sharePanel);

        //Set the share text
        final String shareText = this.homeTeam + " vs " + this.awayTeam + " " + this.score;

        sharePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //add Share Action
                context.startActivity(createShareForecastIntent(shareText + " "));
            }
        });

    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }


}