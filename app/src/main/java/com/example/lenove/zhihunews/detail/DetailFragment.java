package com.example.lenove.zhihunews.detail;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.comment.CommentActivity;
import com.example.lenove.zhihunews.db.RealmHelper;
import com.example.lenove.zhihunews.entity.ContentBean;
import com.example.lenove.zhihunews.entity.ExtraBean;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.example.lenove.zhihunews.network.NewsService;
import com.example.lenove.zhihunews.photo.PhotoActivity;
import com.example.lenove.zhihunews.widget.NestedScrollWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by lenove on 2017/4/28.
 */

public class DetailFragment extends Fragment {
    private static final String TITLE = "TITLE";
    private static final String IMAGE = "IMAGE";
    private Toolbar toolbar;
    private NewsItem newsItem;
    private NestedScrollWebView webView;
    private ImageView imageView;
    private int numsLike;
    private static final String COMMENT = "COMMENT";
    private boolean isLike = false;
    private boolean isFavorite;
    private RealmHelper mRealmHelper;
    @BindView(R.id.detail_title)
    public TextView tvTitle;

    @BindView(R.id.detail_source)
    public TextView tvSource;


    @BindView(R.id.iv_toolbar_favorites)
    public ImageView ivToolFavorites;

    @BindView(R.id.iv_toolbar_like)
    public ImageView ivToolLike;

    @BindView(R.id.tv_toolbar_comments)
    public TextView tvToolComments;

    @BindView(R.id.tv_toolbar_like)
    public TextView tvToolLike;


    @OnClick(R.id.layout_toolbar_share)
    public void showShare(){
        Toast.makeText(getActivity(),"share",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_toolbar_favorites)
    public void changeFavorited(){
        if (isFavorite){
            isFavorite = false;
        }else {
            isFavorite = true;
        }
        setFavorited(isFavorite);
        showFavorited();
    }


    private void setFavorited(boolean isFavorite) {
        mRealmHelper.setIsFavorited(mId,isFavorite);
    }

    @OnClick(R.id.layout_toolbar_comments)
    public void showComments(){
        Intent CommentIntent = new Intent(getActivity(), CommentActivity.class);
        CommentIntent.putExtra(COMMENT,mId);
        startActivity(CommentIntent);
    }

    @OnClick(R.id.layout_toolbar_like)
    public void showLike(){
        if(!isLike){
            ivToolLike.setImageResource(R.drawable.ic_liked);
            tvToolLike.setText(numsLike+1+"");

            isLike = true;
        }else {
            ivToolLike.setImageResource(R.drawable.ic_like);
            tvToolLike.setText(numsLike+"");
            isLike = false;
        }
    }
    private static final String DETAIL = "DETAIL";
    private static final String PHOTO = "PHOTO";
    private String imageUrl;
    private String title;
    private String source;
    private int mId;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_detail,container,false);
//        newsItem = (NewsItem) getActivity().getIntent().getSerializableExtra(DETAIL);
        mId = getActivity().getIntent().getIntExtra(DETAIL,0);
        ButterKnife.bind( this , root ) ;
        imageView = (ImageView) root.findViewById(R.id.detail_img);
        mRealmHelper = new RealmHelper(getActivity());
        String url = getActivity().getIntent().getStringExtra(IMAGE);
        Glide.with(getActivity()).load(url).into(imageView);
        tvTitle.setText(getActivity().getIntent().getStringExtra(TITLE));
        isFavorite = mRealmHelper.getFavorited(mId);

        showFavorited();
        initWebView(root);

        toolbar = (Toolbar) root.findViewById(R.id.detail_toolbar);
        initToolbar(toolbar);
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        Log.d("111","id:"+mId);
        getContent(webView);


        return root;
    }

    private void showFavorited(){
        if(isFavorite){
            ivToolFavorites.setImageResource(R.drawable.ic_favorited);
        }else {
            ivToolFavorites.setImageResource(R.drawable.ic_favorites);
        }
    }


    private void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);

    }
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_detail,menu);
//        final MenuItem item = menu.findItem(R.id.detail_comment);
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        Log.d("111","menu:+++"+menu.getItem(2).getTitle()) ;
//        super.onPrepareOptionsMenu(menu);
//    }

    private void getContent(final WebView webView){
        String baseUrl = "http://news-at.zhihu.com/api/4/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        NewsService newsService = retrofit.create(NewsService.class);
        //TODO
        newsService.getContent("news",mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ContentBean contentBean) {
                        String str = contentBean.getBody();
//                        Document document = Jsoup.parse(str);

//                        Elements elements = document.getElementsByClass("bio");
//                        Log.d("111",elements.size()+"sss");
//                        Element src =  elements.get(0);
//                        src.attr("style","color:blue");
                        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
                        String html = "<html><head>" + css + "</head><body>" + str + "</body></html>";
                        html = html.replace("<div class=\"img-place-holder\">", "");
                        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);

                        imageUrl = contentBean.getImage();
                        title = contentBean.getTitle();
                        source = contentBean.getImage_source();

                        tvSource.setText(source);
                        Glide.with(getActivity()).load(imageUrl).into(imageView);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            // flags
//                            // FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
//                            // FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
//                            textView.setText(Html.fromHtml(str,Html.FROM_HTML_MODE_COMPACT));
//                        } else {
//                            textView.setText(Html.fromHtml(str));
//                        }


//                        Html.fromHtml(str);
//                        Log.d("111", Html.fromHtml(str)+"         000000000000title");
                    }
                });

        newsService.getExtra("story-extra",mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExtraBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ExtraBean extraBean) {
                        Log.d("111",extraBean.getPopularity()+"popularity");
                        numsLike = extraBean.getPopularity();
                        tvToolComments.setText(extraBean.getComments()+"");
                        tvToolLike.setText(extraBean.getPopularity()+"");

                        getActivity().invalidateOptionsMenu();
                    }
                });
    }

    private void initWebView(View root) {
        webView = (NestedScrollWebView) root.findViewById(R.id.tt);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        webView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:(" + readJS() + ")()");
            }
        });
        webView.addJavascriptInterface(new JavascriptInterface(), "mainActivity");
    }
    private String readJS() {
        try {
            InputStream inStream = getActivity().getAssets().open("js.txt");
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inStream.read(bytes)) > 0) {
                outStream.write(bytes, 0, len);
            }
            return outStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void startPhotoActivity(String imageUrl) {
            Intent intentPhotoActivity = new Intent(getActivity(),PhotoActivity.class);
            intentPhotoActivity.putExtra(PHOTO,imageUrl);
            startActivity(intentPhotoActivity);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.detail_share:
                Toast.makeText(getActivity(),"share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.detail_favorites:
                Toast.makeText(getActivity(),"favorites",Toast.LENGTH_SHORT).show();
                break;

            case R.id.detail_comment:
                Toast.makeText(getActivity(),"comment",Toast.LENGTH_SHORT).show();
                break;

            case R.id.detail_like:
                Toast.makeText(getActivity(),"like",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
