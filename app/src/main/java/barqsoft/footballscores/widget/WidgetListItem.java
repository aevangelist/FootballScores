package barqsoft.footballscores.widget;

/**
 * Created by aevangelista on 15-12-02.
 */
public class WidgetListItem {
    private int homeLogo;
    private int awayLogo;
    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;

    public WidgetListItem(){
    }

    public WidgetListItem(int hl, int al, String ht, String at, String hs, String as){
        this.homeLogo = hl;
        this.awayLogo = al;
        this.homeTeam = ht;
        this.awayTeam = at;
        this.homeScore = hs;
        this.awayScore = as;
    }

    public int getHomeLogo(){
        return homeLogo;
    }

    public int getAwayLogo(){
        return awayLogo;
    }

    public String getHomeTeam(){
        return homeTeam;
    }

    public String getAwayTeam(){
        return awayTeam;
    }

    public String getHomeScore(){
        return homeScore;
    }

    public String getAwayScore(){
        return awayScore;
    }

    public void setHomeLogo(int s){
        this.homeLogo = s;
    }

    public void setAwayLogo(int s){
        this.awayLogo = s;
    }

    public void setHomeTeam(String s){
        this.homeTeam = s;
    }

    public void setAwayTeam(String s){
        this.awayTeam = s;
    }

    public void setHomeScore(String s){
        this.homeScore = s;
    }

    public void setAwayScore(String s){
        this.awayScore = s;
    }

}
