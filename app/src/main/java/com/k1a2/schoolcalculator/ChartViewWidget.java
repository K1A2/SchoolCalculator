package com.k1a2.schoolcalculator;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * 위젯 클래스 개발 중단됨
 */
public class ChartViewWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.chart_view_widget);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }
}

