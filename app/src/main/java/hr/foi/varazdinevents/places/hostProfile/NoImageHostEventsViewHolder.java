package hr.foi.varazdinevents.places.hostProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.auto.factory.AutoFactory;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.elements.ItemViewHolderFactory;
import hr.foi.varazdinevents.ui.elements.list.ItemViewHolder;

/**
 * Created by Valentin Magdić on 11.01.17..
 */

/**
 * View holder for the basic event item with no image to display
 */
@AutoFactory(implementing = ItemViewHolderFactory.class)
public class NoImageHostEventsViewHolder extends ItemViewHolder {
    private static final int ITEM_LAYOUT = R.layout.event_no_image_item;

    @BindView(R.id.basic_item_title)
    TextView title;

    @BindView(R.id.basic_item_datetime)
    TextView date;

    @BindView(R.id.basic_item_host)
    TextView host;

    private Event event;

    /**
     * Basic event view holder without image
     * @param parent
     */
    public NoImageHostEventsViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT, parent, false));
        unbinder = ButterKnife.bind(this, itemView);
    }

    /**
     * Gets and sets basic event details
     * @param item data object
     */
    @Override
    public void bind(Object item) {
        event = (Event) item;

        this.title.setText(event.getTitle());

        Date eventDate = new Date(event.getDate() - 3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        this.date.setText(dateFormat.format(eventDate));

        this.host.setText(event.getHost());
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public View getAnimationTarget() {
        return null;
    }

}
