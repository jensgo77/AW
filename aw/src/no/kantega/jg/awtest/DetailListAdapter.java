package no.kantega.jg.awtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import no.kantega.jg.awtest.domain.Comment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 18.10.13
 * Time: 14:18
 */
public class DetailListAdapter extends ArrayAdapter<Comment> {

    private final Context context;
    private final List<Comment> values;

    public DetailListAdapter(Context context, List<Comment> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.detailitem, parent, false);
        TextView authorView = (TextView) rowView.findViewById(R.id.detailitem_author);
        TextView descView = (TextView) rowView.findViewById(R.id.detailitem_text);

        Comment val = values.get(position);
        authorView.setText(val.getAuthor());
        descView.setText(val.getText());

        // Change the icon for Windows and iPhone
/*        if (s.startsWith("iPhone")) {
                imageView.setImageResource(R.drawable.no);
        } else {
                imageView.setImageResource(R.drawable.ok);
        } */
        return rowView;
    }
}

