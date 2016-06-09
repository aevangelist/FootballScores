package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class myFetchService extends IntentService
{
    public static final String LOG_TAG = "myFetchService";
    public myFetchService()
    {
        super("myFetchService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        getData("n2");
        getData("p2");

        return;
    }

    private void getData (String timeFrame)
    {
        //Creating fetch URL
        //OLD: http://api.football-data.org/alpha/fixtures
        //NEW: http://api.football-data.org/v1/soccerseasons/424/fixtures
        final String BASE_URL = "http://api.football-data.org/alpha/fixtures"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        Log.v(LOG_TAG, "The url we are looking at is: " + fetch_build.toString()); //log spam

        String matchData = makeAPICall(fetch_build);

        try {
            if (matchData != null) {
                JSONArray matches = new JSONObject(matchData).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //processJSONdata(getString(R.string.dummy_data), getApplicationContext(), false);
                    return;
                }


                processJSONdata(matchData, getApplicationContext(), true);
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        }
        catch(Exception e)
        {
            Log.e(LOG_TAG,e.getMessage());
        }
    }


    private void processJSONdata (String JSONdata, Context mContext, boolean isReal)
    {
        //JSON data
        // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
        // be updated. Feel free to use the codes
        final String PREMIER_LEAGUE = "398";
        final String SERIE_A = "401";
        final String BUNDESLIGA1 = "394";
        final String BUNDESLIGA2 = "395";
        final String PRIMERA_DIVISION = "399";
        //Additional league codes for testing
        final String LIGUE1 = "396";
        final String LIGUE2 = "397";
        final String SEGUNDA_DIVISION = "400";
        final String PRIMERA_LIGA = "402";
        final String Bundesliga3 = "403";
        final String EREDIVISIE = "404";

        //
        final String SEASON_LINK = "http://api.football-data.org/alpha/soccerseasons/";
        final String MATCH_LINK = "http://api.football-data.org/alpha/fixtures/";

        final String FIXTURES = "fixtures";
        final String LINKS = "_links";
        final String SOCCER_SEASON = "soccerseason";
        final String SELF = "self";
        final String MATCH_DATE = "date";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String RESULT = "result";
        final String HOME_GOALS = "goalsHomeTeam";
        final String AWAY_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";

        final String HOME = "homeTeam";
        final String AWAY = "awayTeam";
        final String HREF = "href";


        //Match data
        String League = null;
        String HomeTeamUrl = null;
        String AwayTeamUrl = null;

        String mDate = null;
        String mTime = null;
        String Home = null;
        String Away = null;
        String Home_goals = null;
        String Away_goals = null;
        String match_id = null;
        String match_day = null;
        String home_team = null;
        String away_team = null;


        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES);


            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector <ContentValues> (matches.length());
            for(int i = 0;i < matches.length();i++)
            {

                JSONObject match_data = matches.getJSONObject(i);

                League = match_data.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).
                        getString("href");
                League = League.replace(SEASON_LINK,"");

                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                /*if(     League.equals(PREMIER_LEAGUE)      ||
                        League.equals(SERIE_A)             ||
                        League.equals(BUNDESLIGA1)         ||
                        League.equals(BUNDESLIGA2)         ||
                        League.equals(PRIMERA_DIVISION)   ||

                        League.equals(LIGUE1) ||
                        League.equals(LIGUE2) ||
                        League.equals(SEGUNDA_DIVISION) ||
                        League.equals(PRIMERA_LIGA) ||
                        League.equals(Bundesliga3) ||
                        League.equals(EREDIVISIE))
                {*/

                    match_id = match_data.getJSONObject(LINKS).getJSONObject(SELF).
                            getString(HREF);
                    match_id = match_id.replace(MATCH_LINK, "");
                    if(!isReal){
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        match_id=match_id+Integer.toString(i);
                    }

                    mDate = match_data.getString(MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                    mDate = mDate.substring(0,mDate.indexOf("T"));
                    SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parseddate = match_date.parse(mDate+mTime);
                        SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        new_date.setTimeZone(TimeZone.getDefault());
                        mDate = new_date.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0,mDate.indexOf(":"));

                        if(!isReal){
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis()+((i-2)*86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                            mDate=mformat.format(fragmentdate);
                        }
                    }
                    catch (Exception e)
                    {
                        Log.d(LOG_TAG, "error here!");
                        Log.e(LOG_TAG,e.getMessage());
                    }

                    Home = match_data.getString(HOME_TEAM);
                    Away = match_data.getString(AWAY_TEAM);
                    Home_goals = match_data.getJSONObject(RESULT).getString(HOME_GOALS);
                    Away_goals = match_data.getJSONObject(RESULT).getString(AWAY_GOALS);
                    match_day = match_data.getString(MATCH_DAY);


                    home_team = match_data.getJSONObject(LINKS).getJSONObject(HOME).
                            getString(HREF);
                    away_team = match_data.getJSONObject(LINKS).getJSONObject(AWAY).
                            getString(HREF);

                    String home_crest = getCrest(home_team);
                    String away_crest = getCrest(away_team);

                ContentValues match_values = new ContentValues();
                    match_values.put(DatabaseContract.scores_table.MATCH_ID,match_id);
                    match_values.put(DatabaseContract.scores_table.DATE_COL,mDate);
                    match_values.put(DatabaseContract.scores_table.TIME_COL,mTime);
                    match_values.put(DatabaseContract.scores_table.HOME_COL,Home);
                    match_values.put(DatabaseContract.scores_table.AWAY_COL,Away);
                    match_values.put(DatabaseContract.scores_table.HOME_GOALS_COL,Home_goals);
                    match_values.put(DatabaseContract.scores_table.AWAY_GOALS_COL,Away_goals);
                    match_values.put(DatabaseContract.scores_table.LEAGUE_COL,League);
                    match_values.put(DatabaseContract.scores_table.MATCH_DAY,match_day);
                    match_values.put(DatabaseContract.scores_table.HOME_IMG,home_crest);
                    match_values.put(DatabaseContract.scores_table.AWAY_IMG,away_crest);
                values.add(match_values);
                //}

            }

            int inserted_data = 0;
            ContentValues[] insert_data = new ContentValues[values.size()];
            values.toArray(insert_data);
            inserted_data = mContext.getContentResolver().bulkInsert(
                    DatabaseContract.BASE_CONTENT_URI,insert_data);

            Log.v(LOG_TAG,"Succesfully Inserted : " + String.valueOf(inserted_data));
        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG,e.getMessage());
        }

    }

    private String getCrest(String url){
        String crest = "";
        if(!url.isEmpty()){
            Uri url_uri = Uri.parse(url);
            String urlData = makeAPICall(url_uri);
            crest = processTeamURL(urlData);
            //crest = removeDiacritics(c);

            Log.d(LOG_TAG, "Crest URL: " + crest);
        }
        return crest;
    }

    public static String removeDiacritics(String input)
    {
        String nrml = Normalizer.normalize(input, Normalizer.Form.NFD);
        StringBuilder stripped = new StringBuilder();
        for (int i=0;i<nrml.length();++i)
        {
            if (Character.getType(nrml.charAt(i)) != Character.NON_SPACING_MARK)
            {
                stripped.append(nrml.charAt(i));
            }
        }
        return stripped.toString();
    }

    private String processTeamURL(String data){

        final String CREST_URL = "crestUrl";

        try {
            if (data != null) {
                JSONObject team = new JSONObject(data);
                String crest = team.getString(CREST_URL);
                return crest;
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        }
        catch(Exception e) {
            Log.e(LOG_TAG,e.getMessage());
        }

        return "";
    }

    private String makeAPICall(Uri url) {
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(url.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod("GET");
            m_connection.addRequestProperty("X-Auth-Token", getString(R.string.api_key));
            m_connection.connect();

            // Read the input stream into a String
            InputStream inputStream = m_connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return "";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return "";
            }
            JSON_data = buffer.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception here" + e.getMessage());
        } finally {
            if (m_connection != null) {
                m_connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream");
                }
            }
        }

        return JSON_data;
    }
}

