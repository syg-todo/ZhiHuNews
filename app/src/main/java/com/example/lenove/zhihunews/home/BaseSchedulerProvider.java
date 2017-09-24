package com.example.lenove.zhihunews.home;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by lenove on 2017/7/10.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
