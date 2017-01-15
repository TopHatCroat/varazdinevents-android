package hr.foi.varazdinevents.ui.elements.list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import hr.foi.varazdinevents.util.ScreenUtils;

/**
 * Created by Antonio Martinović on 15.01.17.
 */

public class ItemAnimator extends DefaultItemAnimator {
    private int lastAddAnimatedItem = -2;

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                                               @NonNull RecyclerView.ViewHolder viewHolder,
                                                                               int changeFlags, @NonNull List<Object> payloads) {
//        if (changeFlags == FLAG_CHANGED) {
//            for (Object payload : payloads) {
//                if (payload instanceof String) {
//                    return new FeedItemHolderInfo((String) payload);
//                }
//            }
//        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getLayoutPosition() > lastAddAnimatedItem) {
            lastAddAnimatedItem++;
            runEnterAnimation((ItemViewHolder) viewHolder);
            return false;
        }

        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final ItemViewHolder holder) {
        final int screenHeight = ScreenUtils.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }


}
