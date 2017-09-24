package com.example.lenove.zhihunews.comment;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.entity.CommentItem;
import com.example.lenove.zhihunews.util.TransitionUtils;
import com.example.lenove.zhihunews.util.recyclerview.SlideInItemAnimator;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

import static com.example.lenove.zhihunews.util.AnimUtils.getFastOutSlowInInterpolator;


/**
 * Created by lenove on 2017/5/8.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private ArrayList<CommentItem> commentList;
    private int numLong;
    private static final int TYPE_NUM_LONG = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_NUM_SHORT = 2;
    private static final int TYPE_SHORT = 3;
    private static final int EXPAND = 0x1;
    private static final int COLLAPSE = 0x2;
    private TextView mHeaderTitle;
    private View mLongView;
    private View mLongItemView;
    private View mShortView;
    private View mShortItemView;
  Transition expandCollapse;
    private CommentAnimator commentAnimator;
    int expandedCommentPosition = RecyclerView.NO_POSITION;
    private Activity context;
    private boolean expand = false;
    private ImageButton reply;
    private ImageButton likeHeart;
    private TextView likesCount;
    public class CommentHolder extends RecyclerView.ViewHolder {
        private ImageView ivLongIcon;
        private TextView tvLongName;
        private TextView tvLongContent;
        private TextView tvLongTime;

        private String mLongIcon;
        private String mLongName;
        private String mLongContent;
        private int mLongTime;

        private CommentItem mCommentItem;

        public CommentHolder(View itemView) {
            super(itemView);
            if (itemView == mLongView || itemView == mShortView) {
                mHeaderTitle = (TextView) itemView.findViewById(R.id.header);

                return;
            } else {
                ivLongIcon = (ImageView) itemView.findViewById(R.id.comment_item_icon);
                tvLongName = (TextView) itemView.findViewById(R.id.comment_item_name);
                tvLongContent = (TextView) itemView.findViewById(R.id.comment_item_content);
                tvLongTime = (TextView) itemView.findViewById(R.id.comment_item_time);
                likeHeart = (ImageButton) itemView.findViewById(R.id.comment_like);
                reply = (ImageButton) itemView.findViewById(R.id.comment_reply);
                likesCount = (TextView) itemView.findViewById(R.id.comment_likes_count);
                final Interpolator interp = getFastOutSlowInInterpolator(ivLongIcon.getContext());
                ivLongIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivLongIcon.animate()
                                .translationX(ivLongIcon.getWidth())
                                .alpha(0f)
                                .setDuration(200L)
                                .setInterpolator(interp);
                    }
                });

                return;


            }


        }

        public void bindHolder(CommentItem commentItem) {
            this.mCommentItem = commentItem;

            mLongIcon = commentItem.getItemIcon();
            mLongName = commentItem.getItemName();
            mLongContent = commentItem.getItemContent();
            mLongTime = commentItem.getTime();
            Log.d("111", "time+" + mLongTime);
            Picasso.with(ivLongIcon.getContext()).load(mLongIcon).into(ivLongIcon);
            tvLongName.setText(mLongName);
            tvLongContent.setText(mLongContent);
            tvLongTime.setText(mLongTime + "");
        }

        public void bindLong(CommentHolder holder, int size) {
            final int position = holder.getAdapterPosition();

            final boolean isExpanded = position == expandedCommentPosition;

            commentAnimator = new CommentAnimator();
//            commentsList.setItemAnimator(commentAnimator);
            mHeaderTitle.setText(size + "条长评");
            setExpanded(holder, isExpanded);

        }

        private void setExpanded(CommentHolder holder, boolean isExpanded) {
            Log.d("111","setExpanded");
            holder.itemView.setActivated(isExpanded);
//            holder.reply.setVisibility((isExpanded) ? View.VISIBLE : View.GONE);
//            holder.likeHeart.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//            holder.likesCount.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }


    }

    View.OnTouchListener touchEater = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };

    public CommentAdapter(ArrayList<CommentItem> data) {
        Log.d("111","CommentAdapter");
        commentList = data;
//        expandCollapse = new AutoTransition();
//        expandCollapse.setDuration(120);
//        expandCollapse.setInterpolator(getFastOutSlowInInterpolator(activity));
//        expandCollapse.addListener(new TransitionUtils.TransitionListenerAdapter() {
//            @Override
//            public void onTransitionStart(Transition transition) {
//                commentsList.setOnTouchListener(touchEater);
//            }
//
//            @Override
//            public void onTransitionEnd(Transition transition) {
//                commentAnimator.setAnimateMoves(true);
//                commentsList.setOnTouchListener(null);
//            }
//        });
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NUM_LONG:
                mLongView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                return new CommentHolder(mLongView);
            case TYPE_LONG:
                mLongItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                return createCommentHolder(mLongItemView,viewType);
            case TYPE_NUM_SHORT:
                mShortView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                return new CommentHolder(mShortView);
            case TYPE_SHORT:
                mShortItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                return new CommentHolder(mShortItemView);
            default:
                Log.d("error", "viewholder is null");
                return null;
        }
    }

    private CommentHolder createCommentHolder(View mLongItemView,int viewType) {
        Log.d("111","createCommentHolder");
        final CommentHolder holder = new CommentHolder(mLongItemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("111","click");
                setExpand();

//                final int position = holder.getAdapterPosition();
//                if (position == RecyclerView.NO_POSITION) return;
//
//              TransitionManager.beginDelayedTransition(commentsList, expandCollapse);
//                commentAnimator.setAnimateMoves(false);
//
//                // collapse any currently expanded items
//                if (RecyclerView.NO_POSITION != expandedCommentPosition) {
//                    notifyItemChanged(expandedCommentPosition, COLLAPSE);
//                }
//
//                // expand this item (if it wasn't already)
//                if (expandedCommentPosition != position) {
//                    expandedCommentPosition = position;
//                    notifyItemChanged(position, EXPAND);
//                    holder.itemView.requestFocus();
//                } else {
//                    expandedCommentPosition = RecyclerView.NO_POSITION;
//                }
            }
        });
        
        return holder;

    }

    private void setExpand() {

        if (!expand){
            reply.setVisibility(expand?View.VISIBLE:View.GONE);
            likesCount.setVisibility(expand?View.VISIBLE:View.GONE);
            likeHeart.setVisibility(expand?View.VISIBLE:View.GONE);
        }
        expand = !expand;
        Log.d("111","expand"+expand);
    }


    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NUM_LONG) {
            holder.bindLong(holder, numLong);
        } else {
            CommentItem commentItem = commentList.get(position - 1);
            holder.bindHolder(commentItem);
        }

    }


    @Override
    public int getItemViewType(int position) {
        numLong = commentList.size();

        if (position == 0) {
            return TYPE_NUM_LONG;
        } else if (position <= numLong) {
            return TYPE_LONG;
        } else if (position == numLong + 1) {
            return TYPE_NUM_SHORT;
        } else {
            return TYPE_SHORT;
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size() + 1;
    }

    static class CommentAnimator extends SlideInItemAnimator {

        private boolean animateMoves = false;

        CommentAnimator() {
            super();
        }

        void setAnimateMoves(boolean animateMoves) {
            this.animateMoves = animateMoves;
        }

        @Override
        public boolean animateMove(
                RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            if (!animateMoves) {
                dispatchMoveFinished(holder);
                return false;
            }
            return super.animateMove(holder, fromX, fromY, toX, toY);
        }
    }

}
