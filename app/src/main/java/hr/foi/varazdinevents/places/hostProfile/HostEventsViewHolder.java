package hr.foi.varazdinevents.places.hostProfile;

import android.support.v7.view.menu.MenuView;
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
 * Contains basic view holder for host's events
 */
@AutoFactory(implementing = ItemViewHolderFactory.class)
public class HostEventsViewHolder extends ItemViewHolder {
    private static final int ITEM_LAYOUT = R.layout.event_basic_item;

    @BindView(R.id.basic_item_picture)
    ImageView image;

    @BindView(R.id.basic_item_title)
    TextView title;
    @BindView(R.id.basic_item_datetime)
    TextView date;
    //@BindView(R.id.basic_item_time)
    //TextView time;
    @BindView(R.id.basic_item_host)
    TextView host;

    private Event event;

    /**
     * Basic view holder for events of a specific host
     * @param parent
     */
    public HostEventsViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT, parent, false));
        unbinder = ButterKnife.bind(this, itemView);
    }

    /**
     * Gets and sets basic event information
     * @param item data object
     */
    @Override
    public void bind(Object item) {
        event = (Event) item;

        this.title.setText(event.getTitle());

        Date eventDate = new Date(event.getDate() - 3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        this.date.setText(dateFormat.format(eventDate));

        Picasso.with(itemView.getContext()).load(event.getImage()).into(image);

        this.host.setText(event.getHost());
    }

    @Override
    public void unbind() {
        unbinder.unbind();
    }

    @Override
    public View getAnimationTarget() {
        return image;
    }
}
