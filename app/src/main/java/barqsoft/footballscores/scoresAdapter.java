package barqsoft.footballscores;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.view.CardViewNative;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter
{
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;

    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    private CardViewNative cardview;

    public ScoresAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);

        //Create a Card and Expansion
        Card card = new Card(context);
        CustomExpandCard expansion = new CustomExpandCard(context);

        //Build the expansion description
        String matchDayText = Utilies.getMatchDay(cursor.getInt(COL_MATCHDAY),
                cursor.getInt(COL_LEAGUE));
        String leagueText = Utilies.getLeague(cursor.getInt(COL_LEAGUE));
        String homeTeamText = cursor.getString(COL_HOME);
        String awayTeamText = cursor.getString(COL_AWAY);
        String scoresText = Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));

        expansion.setMatchDay(matchDayText);
        expansion.setLeague(leagueText);
        expansion.setHomeTeam(homeTeamText);
        expansion.setAwayTeam(awayTeamText);
        expansion.setScore(scoresText);

        cardview = (CardViewNative) mItem.findViewById(R.id.cv);

        ViewToClickToExpand viewToClickToExpand =
                ViewToClickToExpand.builder()
                        .setupView(cardview);

        card.setViewToClickToExpand(viewToClickToExpand);
        card.addCardExpand(expansion);
        cardview.setCard(card);

        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.home_name.setText(cursor.getString(COL_HOME));
        mHolder.away_name.setText(cursor.getString(COL_AWAY));
        mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        mHolder.score.setText(Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        mHolder.match_id = cursor.getDouble(COL_ID);
        mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                cursor.getString(COL_HOME)));
        mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                cursor.getString(COL_AWAY)
        ));

        Log.v("ScoresAdapter", mHolder.home_name.getText() + " vs " + mHolder.away_name.getText() + " id " + String.valueOf(mHolder.match_id));

    }

}
