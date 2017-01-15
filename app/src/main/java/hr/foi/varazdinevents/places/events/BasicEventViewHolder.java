package hr.foi.varazdinevents.places.events;

import android.content.Intent;
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
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.places.hostProfile.HostProfileActivity;
import hr.foi.varazdinevents.ui.elements.list.ItemViewHolder;
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

    @BindView(R.id.basic_item_datetime)
    TextView date;

    //@BindView(R.id.basic_item_time)
    //TextView time;

    @BindView(R.id.basic_item_host)
    TextView host;

    private Event event;

    public BasicEventViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(ITEM_LAYOUT, parent, false));
        unbinder = ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Object item) {
        event = (Event) item;
        this.title.setText(event.getTitle());

        Date eventDate = new Date(event.getDate() - 3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        this.date.setText(dateFormat.format(eventDate));

        //this.time.setText(event.getTime());

        Picasso.with(itemView.getContext()).load(event.getImage()).into(image);
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);

        this.host.setText(event.getHost());
//        this.host.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent newIntent = new Intent(BasicEventViewHolder.this, HostProfileActivity.class);
//                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                BasicEventViewHolder.this.startActivity(newIntent);
//            }
//        });
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