package barqsoft.footballscores.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import barqsoft.footballscores.R;

/**
 * Created by aevangelista on 15-11-22.
 */
public class WidgetActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        sendBroadcast(new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME));
        setContentView(R.layout.widget_sample);

    }

}
