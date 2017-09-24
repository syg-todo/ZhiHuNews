package com.example.lenove.zhihunews.comment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.entity.Comment;
import com.example.lenove.zhihunews.entity.CommentBean;
import com.example.lenove.zhihunews.entity.CommentItem;
import com.example.lenove.zhihunews.entity.ContentBean;
import com.example.lenove.zhihunews.network.NewsService;
import com.example.lenove.zhihunews.util.TransitionUtils;
import com.example.lenove.zhihunews.util.recyclerview.SlideInItemAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.lenove.zhihunews.util.AnimUtils.getFastOutSlowInInterpolator;

/**
 * Created by lenove on 2017/5/6.
 */

public class CommentFragment extends Fragment {

    private static final String COMMENT = "COMMENT";
    private int mId;
    private ArrayList<CommentItem> data;
    private CommentAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private Toolbar toolbar;
    private LinearLayout mLayoutNoLongComments;
    CommentAnimator commentAnimator;
    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_comment, container, false);
        initView(root);
        mId = getActivity().getIntent().getIntExtra(COMMENT, 0);
        Log.d("111", "comment" + mId);
        getComment();

        return root;
    }

    private void initView(View root) {
        mRecyclerView = (RecyclerView) root.findViewById(R.id.comment_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mLayoutNoLongComments = (LinearLayout) root.findViewById(R.id.comment_no_long);


        toolbar = (Toolbar) root.findViewById(R.id.comment_toolbar);
        initToolbar(toolbar);


    }

    private void initToolbar(Toolbar toolbar) {

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        toolbar.setTitle("11条点评");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.menu_comment_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("111","edit");
                return true;
            }
        });
    }

    private void getComment() {
        String baseUrl = "http://news-at.zhihu.com/api/4/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        NewsService newsService = retrofit.create(NewsService.class);
        Log.d("111", "comment" + mId);
        newsService.getComment("story", mId, "long-comments")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("111", "comment error");
                    }

                    @Override
                    public void onNext(CommentBean commentBean) {
                        initLongComments(commentBean);

                    }
                });


    }

    private void initLongComments(CommentBean commentBean) {
        Log.d("111", "try");
        List<CommentBean.EachBean> list = commentBean.getComments();
        int size = list.size();
        CommentItem commentItem;
        String name;
        String url;
        String content;
        int time;
        data = new ArrayList<CommentItem>();
        if (size == 0) {
            mRecyclerView.setVisibility(View.GONE);
//            mLayoutNoLongComments.setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < size; i++) {
            name = list.get(i).getAuthor();
            url = list.get(i).getAvatar();
            content = list.get(i).getContent();
            time = list.get(i).getTime();
            commentItem = new CommentItem();
            commentItem.setItemName(name);
            commentItem.setItemContent(content);
            commentItem.setItemIcon(url);
            commentItem.setTime(time);
            Log.d("111","comment"+name);
            data.add(commentItem);
        }
        mAdapter = new CommentAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initShortComments(CommentBean commentBean) {

    }

    View.OnTouchListener touchEater = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return true;
        }
    };

    class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int EXPAND = 0x1;
        private static final int COLLAPSE = 0x2;
        private static final int COMMENT_LIKE = 0x3;
        private static final int REPLY = 0x4;

        private final List<CommentBean.EachBean> comments = new ArrayList<>();
        final Transition expandCollapse;
        private View footer;

        private boolean loading;
        private boolean noComments;
        int expandedCommentPosition = RecyclerView.NO_POSITION;

        CommentsAdapter(
                @Nullable View footer,
                long commentCount,
                long expandDuration) {
            this.footer = footer;
            noComments = commentCount == 0L;
            loading = !noComments;
            expandCollapse = new AutoTransition();
            expandCollapse.setDuration(expandDuration);
            expandCollapse.setInterpolator(getFastOutSlowInInterpolator(getActivity()));
            expandCollapse.addListener(new TransitionUtils.TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(Transition transition) {
                    mRecyclerView.setOnTouchListener(touchEater);
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    commentAnimator.setAnimateMoves(true);
                    mRecyclerView.setOnTouchListener(null);
                }
            });
        }

//        @Override
//        public int getItemViewType(int position) {
//
//
//            if (footer != null) {
//                int footerPos =  comments.size() + 1;
//                if (position == footerPos) return R.layout.dribbble_enter_comment;
//            }
//            return R.layout.item_comment;
//        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (!comments.isEmpty()) {
                count += comments.size();
            }
            if (footer != null) count++;
            return count;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            switch (viewType) {
//                case R.layout.item_comment:
                    return createCommentHolder(parent, viewType);
//                case R.layout.loading:
//                case R.layout.dribbble_no_comments:
//                    return new SimpleViewHolder(
////                            getLayoutInflater().inflate(viewType, parent, false));
//                case R.layout.dribbble_enter_comment:
//                    return new SimpleViewHolder(footer);
//            }
//            throw new IllegalArgumentException();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case R.layout.item_comment:
                    bindComment((CommentViewHolder) holder, getComment(position));
                    break;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position,
                                     List<Object> partialChangePayloads) {
            if (holder instanceof CommentViewHolder) {
                bindPartialCommentChange(
                        (CommentViewHolder) holder, position, partialChangePayloads);
            } else {
                onBindViewHolder(holder, position);
            }
        }

        CommentBean.EachBean getComment(int adapterPosition) {
            return comments.get(adapterPosition - 1); // description
        }

        void addComments(List<CommentBean.EachBean> newComments) {
            hideLoadingIndicator();
            noComments = false;
            comments.addAll(newComments);
            notifyItemRangeInserted(1, newComments.size());
        }

        void removeCommentingFooter() {
            if (footer == null) return;
            int footerPos = getItemCount() - 1;
            footer = null;
            notifyItemRemoved(footerPos);
        }

        private CommentViewHolder createCommentHolder(ViewGroup parent, int viewType) {
            //comment
            final CommentViewHolder holder = new CommentViewHolder(

                    getActivity().getLayoutInflater().inflate(viewType, parent, false));

            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int position = holder.getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) return;

                    final CommentBean.EachBean comment = getComment(position);
                    TransitionManager.beginDelayedTransition(mRecyclerView, expandCollapse);
                    commentAnimator.setAnimateMoves(false);

                    // collapse any currently expanded items
                    if (RecyclerView.NO_POSITION != expandedCommentPosition) {
                        notifyItemChanged(expandedCommentPosition, COLLAPSE);
                    }

                    // expand this item (if it wasn't already)
                    if (expandedCommentPosition != position) {
                        expandedCommentPosition = position;
                        notifyItemChanged(position, EXPAND);
                        if (comment.getLikes() == 0) {
//                            final Call<Like> liked = dribbblePrefs.getApi()
//                                    .likedComment(shot.id, comment.id);
//                            liked.enqueue(new Callback<Like>() {
//                                @Override
//                                public void onResponse(Call<Like> call, Response<Like> response) {
//                                    comment.liked = response.isSuccessful();
//                                    holder.likeHeart.setChecked(comment.liked);
//                                    holder.likeHeart.jumpDrawablesToCurrentState();
//                                }
//
//                                @Override public void onFailure(Call<Like> call, Throwable t) { }
//                            });
                        }
//                        if (enterComment != null && enterComment.hasFocus()) {
//                            enterComment.clearFocus();
//                            ImeUtils.hideIme(enterComment);
//                        }
                        holder.itemView.requestFocus();
                    } else {
                        expandedCommentPosition = RecyclerView.NO_POSITION;
                    }
                }
            });

            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("111","avatar");
//                    final int position = holder.getAdapterPosition();
//                    if (position == RecyclerView.NO_POSITION) return;
//
//                    final CommentBean.EachBean comment = getComment(position);
//                    final Intent player = new Intent(CommentActivity.this, PlayerActivity.class);
//                    player.putExtra(PlayerActivity.EXTRA_PLAYER, comment.user);
//                    ActivityOptions options =
//                            ActivityOptions.makeSceneTransitionAnimation(DribbbleShot.this,
//                                    Pair.create(holder.itemView,
//                                            getString(R.string.transition_player_background)),
//                                    Pair.create((View) holder.avatar,
//                                            getString(R.string.transition_player_avatar)));
//                    startActivity(player, options.toBundle());
                }
            });

            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("111","reply");
//                    final int position = holder.getAdapterPosition();
//                    if (position == RecyclerView.NO_POSITION) return;
//
//                    final Comment comment = getComment(position);
//                    enterComment.setText("@" + comment.user.username + " ");
//                    enterComment.setSelection(enterComment.getText().length());
//
//                    // collapse the comment and scroll the reply box (in the footer) into view
//                    expandedCommentPosition = RecyclerView.NO_POSITION;
//                    notifyItemChanged(position, REPLY);
//                    holder.reply.jumpDrawablesToCurrentState();
//                    enterComment.requestFocus();
//                    mRecyclerView.smoothScrollToPosition(getItemCount() - 1);
                }
            });

            holder.likeHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("111","likeHeart");
//                    if (dribbblePrefs.isLoggedIn()) {
//                        final int position = holder.getAdapterPosition();
//                        if (position == RecyclerView.NO_POSITION) return;
//
//                        final Comment comment = getComment(position);
//                        if (comment.liked == null || !comment.liked) {
//                            comment.liked = true;
//                            comment.likes_count++;
//                            holder.likesCount.setText(String.valueOf(comment.likes_count));
//                            notifyItemChanged(position, COMMENT_LIKE);
//                            final Call<Like> likeCommentCall =
//                                    dribbblePrefs.getApi().likeComment(shot.id, comment.id);
//                            likeCommentCall.enqueue(new Callback<Like>() {
//                                @Override
//                                public void onResponse(Call<Like> call, Response<Like> response) { }
//
//                                @Override
//                                public void onFailure(Call<Like> call, Throwable t) { }
//                            });
//                        } else {
//                            comment.liked = false;
//                            comment.likes_count--;
//                            holder.likesCount.setText(String.valueOf(comment.likes_count));
//                            notifyItemChanged(position, COMMENT_LIKE);
//                            final Call<Void> unlikeCommentCall =
//                                    dribbblePrefs.getApi().unlikeComment(shot.id, comment.id);
//                            unlikeCommentCall.enqueue(new Callback<Void>() {
//                                @Override
//                                public void onResponse(Call<Void> call, Response<Void> response) { }
//
//                                @Override
//                                public void onFailure(Call<Void> call, Throwable t) { }
//                            });
//                        }
//                    } else {
//                        holder.likeHeart.setChecked(false);
//                        startActivityForResult(new Intent(DribbbleShot.this,
//                                DribbbleLogin.class), RC_LOGIN_LIKE);
//                    }
                }
            });

            holder.likesCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("111","likesCount");
//                    final int position = holder.getAdapterPosition();
//                    if (position == RecyclerView.NO_POSITION) return;
//
//                    final CommentBean.EachBean comment = getComment(position);
//                    final Call<List<Like>> commentLikesCall =
//                            dribbblePrefs.getApi().getCommentLikes(shot.id, comment.id);
//                    commentLikesCall.enqueue(new Callback<List<Like>>() {
//                        @Override
//                        public void onResponse(Call<List<Like>> call,
//                                               Response<List<Like>> response) {
//                            // TODO something better than this.
//                            StringBuilder sb = new StringBuilder("Liked by:\n\n");
//                            for (Like like : response.body()) {
//                                if (like.user != null) {
//                                    sb.append("@");
//                                    sb.append(like.user.username);
//                                    sb.append("\n");
//                                }
//                            }
//                            Toast.makeText(getApplicationContext(), sb.toString(), Toast
//                                    .LENGTH_SHORT).show();
//                        }

//                        @Override
//                        public void onFailure(Call<List<Like>> call, Throwable t) { }
//                    });
                }
            });

            return holder;
        }

        private void bindComment(CommentViewHolder holder, CommentBean.EachBean comment) {
            final int position = holder.getAdapterPosition();
            final boolean isExpanded = position == expandedCommentPosition;
            Glide.with(getContext())
                    .load(comment.getAvatar())
//                    .circleCrop()
                    .placeholder(R.drawable.avatar_placeholder)
//                    .override(largeAvatarSize, largeAvatarSize)
//                    .transition(withCrossFade())
                    .into(holder.avatar);
            holder.author.setText(comment.getAuthor());
//            holder.author.setOriginalPoster(isOP(comment.user.id));
//            holder.timeAgo.setText(comment.created_at == null ? "" :
//                    DateUtils.getRelativeTimeSpanString(comment.created_at.getTime(),
//                            System.currentTimeMillis(),
//                            DateUtils.SECOND_IN_MILLIS)
//                            .toString().toLowerCase());
//            HtmlUtils.setTextWithNiceLinks(holder.commentBody,
//                    comment.getParsedBody(holder.commentBody));
//            holder.likeHeart.setEnabled(comment.user.id != dribbblePrefs.getUserId());
            holder.likesCount.setText(String.valueOf(comment.getLikes()));
            setExpanded(holder, isExpanded);
        }

        private void setExpanded(CommentViewHolder holder, boolean isExpanded) {
            holder.itemView.setActivated(isExpanded);
            holder.reply.setVisibility((isExpanded ) ? View.VISIBLE : View.GONE);
            holder.likeHeart.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.likesCount.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

        private void bindPartialCommentChange(
                CommentViewHolder holder, int position, List<Object> partialChangePayloads) {
            // for certain changes we don't need to rebind data, just update some view state
            if ((partialChangePayloads.contains(EXPAND)
                    || partialChangePayloads.contains(COLLAPSE))
                    || partialChangePayloads.contains(REPLY)) {
                setExpanded(holder, position == expandedCommentPosition);
            } else if (partialChangePayloads.contains(COMMENT_LIKE)) {
                return; // nothing to do
            } else {
                onBindViewHolder(holder, position);
            }
        }

        private void hideLoadingIndicator() {
            if (!loading) return;
            loading = false;
            notifyItemRemoved(1);
        }
    }
    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
    static class CommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_item_icon)
        ImageView avatar;
        @BindView(R.id.comment_item_name)
        TextView author;
        @BindView(R.id.comment_item_time) TextView timeAgo;
        @BindView(R.id.comment_item_content) TextView commentBody;
        @BindView(R.id.comment_reply)
        ImageButton reply;
        @BindView(R.id.comment_like)
        ImageButton likeHeart;
        @BindView(R.id.comment_likes_count) TextView likesCount;

        CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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