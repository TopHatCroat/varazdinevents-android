package hr.foi.varazdinevents.ui.elements;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Antonio Martinović on 13.10.16.
 */

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.foi.varazdinevents.R;

public abstract class ItemViewHolder extends RecyclerView.ViewHolder implements Listable {
    protected Unbinder unbinder;

    public ItemViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(Object item);

    public abstract void unbind();

}

