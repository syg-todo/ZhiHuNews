package com.example.lenove.zhihunews.home;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.data.NewsDataSource;
import com.example.lenove.zhihunews.data.NewsRepository;
import com.example.lenove.zhihunews.theme.ThemeActivity;
import com.example.lenove.zhihunews.theme.ThemeFragment;
import com.example.lenove.zhihunews.util.ActivityUtils;

public class MainActivity extends AppCompatActivity {
    private static final String THEME = "THEME";
    private DrawerLayout mDrawerLayout;
    private HomePresenter mHomePresenter;
    private ThemeFragment themeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        final FavoriteFragment favoriteFragment = FavoriteFragment.newInstance();


        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, R.id.contentFrame);
        }
        mHomePresenter = new HomePresenter(NewsRepository.getInstance(this),homeFragment);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        GridLayout layout = (GridLayout) navigationView.getHeaderView(0);
        layout.findViewById(R.id.nav_header_favorites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("111","shoucang");
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.contentFrame,favoriteFragment);
                ft.addToBackStack(null);
                ft.commit();

                mDrawerLayout.closeDrawers();
                Log.d("111","click0");
            }
        });
        if ((navigationView != null)) {
            setupDrawerContent(navigationView);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        Intent intentTheme = new Intent(MainActivity.this, ThemeActivity.class);
//                        intentTheme.putExtra(THEME, menuItem.getOrder());
//                        startActivity(intentTheme);
//                        Log.d("111", menuItem.getOrder() + "id");
                        //2开始游戏
                        //3 电影日报
                        //4 设计日报
                        //5 大公司日报
                        //6 财经日报
                        //7 音乐日报
                        //8 体育日报
                        //9 动漫日报
                        //10 互联网安全
                        //11 不许无聊
                        //12 用户推荐日报
                        //13 日常心理学

//                        switch (menuItem.getItemId()){
//                                case R.id.nav_list_daily_psy:
//                                    Log.d("111","daily");
//                                    break; 
//                                default:
//                                break;
//                        }
                        themeFragment = ThemeFragment.newInstance();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        if (menuItem.getOrder() == 0){
                            ft.replace(R.id.contentFrame,HomeFragment.newInstance());
                        }else {

                        }
                        Log.d("111","order"+menuItem.getOrder());

                        ft.addToBackStack(null);
                        ft.commit();


                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_messages:
//                break;
//            case R.id.action_mode:
//                if (item.getTitle().equals("夜间模式")) {
//                    item.setTitle("日间模式");
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }else {
//                    item.setTitle("夜间模式");
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
