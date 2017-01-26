package ru.arnis.producthuntdemoapp.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.arnis.producthuntdemoapp.R;
import ru.arnis.producthuntdemoapp.model.Post;
import ru.arnis.producthuntdemoapp.view.CategoryFragment;

/**
 * Created by arnis on 26/01/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private CategoryFragment categoryFragment;
    private Context context;
    private ViewPropertyAnimation.Animator glideTransition;
    private int lastAnimated = -1;

    public RecyclerViewAdapter(Context context, CategoryFragment categoryFragment) {
        this.context = context;
        this.categoryFragment = categoryFragment;
        glideTransition = new ViewPropertyAnimation.Animator() {
            @Override
            public void animate(View view) {
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat( view, "alpha", 0f, 1f );
                fadeAnim.setDuration(300);
                fadeAnim.setInterpolator(new AccelerateInterpolator());
                fadeAnim.start();
            }
        };
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.background)
        ImageView background;
        @BindView(R.id.post_title)
        TextView postTitle;
        @BindView(R.id.post_description)
        TextView postDescription;
        @BindView(R.id.up_votes)
        Button upVotes;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void clearAnimation(){
            itemView.clearAnimation();
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = categoryFragment.getPosts().get(position);

        Glide.with(context).load(post.getThumbnailUrl()).asBitmap().animate(glideTransition).centerCrop().into(holder.background);
        holder.postTitle.setText(post.getName());
        holder.postDescription.setText(post.getDescription());
        holder.upVotes.setText(String.valueOf(post.getUpVotes()));

        setAnimation(holder.itemView,position);
    }

    private void setAnimation(View view, int position){
        if (position>lastAnimated) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotation_slide_in_from_bottom);
            view.startAnimation(animation);
            lastAnimated = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return categoryFragment.getPosts().size();
    }

    public void resetLastAnimated(){
        lastAnimated = -1;
    }
}
