package hr.foi.varazdinevents.places.events;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.auto.factory.AutoFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.elements.ItemViewHolder;
import hr.foi.varazdinevents.ui.elements.ItemViewHolderFactory;

/**
 * Created by Antonio Martinović on 31.10.16.
 */

@AutoFactory(implementing = ItemViewHolderFactory.class)
public class BasicEventViewHolder extends ItemViewHolder {
    private static final int ITEM_LAYOUT = R.layout.event_basic_item;

    @BindView(R.id.basic_item_picture)
    ImageView image;

    @BindView(R.id.basic_item_title)
    TextView title;

    @BindView(R.id.basic_item_date)
    TextView date;

    private Event event;

    public BasicEventViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT, parent, false));
        unbinder = ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Object item) {
        event = (Event) item;
        this.title.setText(event.getTitle());
        this.date.setText(event.getDate().toString());
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public int getType() {
        return this.ITEM_LAYOUT;
    }
}