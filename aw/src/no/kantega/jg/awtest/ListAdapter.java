package no.kantega.jg.awtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import no.kantega.jg.awtest.domain.Entry;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jenork
 * Date: 17.10.13
 */
public class ListAdapter extends ArrayAdapter<Entry> {
    private final Context context;
    private final List<Entry> values;

    public ListAdapter(Context context, List<Entry> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listitem, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.listitem_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listitem_icon);
        TextView descView = (TextView) rowView.findViewById(R.id.listitem_desc);

        Entry val = values.get(position);
        titleView.setText(val.getTitle());
        descView.setText(val.getSummary());

        // Change the icon for Windows and iPhone
/*        if (s.startsWith("iPhone")) {
                imageView.setImageResource(R.drawable.no);
        } else {
                imageView.setImageResource(R.drawable.ok);
        } */
        return rowView;
    }
}

