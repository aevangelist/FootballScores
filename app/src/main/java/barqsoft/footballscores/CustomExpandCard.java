package barqsoft.footballscores;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by aevangelista on 15-11-18.
 */

public class CustomExpandCard extends CardExpand {

    //Use your resource ID for your inner layout
    public CustomExpandCard(Context context) {

        super(context, R.layout.scores_list_item_expand);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        //Retrieve TextView elements
        TextView tx1 = (TextView) view.findViewById(R.id.description);

        //Set value in text views
        if (tx1 != null) {
            tx1.setText("This is just test text for now...");
        }

    }
}