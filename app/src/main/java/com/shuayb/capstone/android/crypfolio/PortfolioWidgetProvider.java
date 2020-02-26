package com.shuayb.capstone.android.crypfolio;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.shuayb.capstone.android.crypfolio.DataUtils.RandomUtils;

/**
 * Implementation of App Widget functionality.
 */
public class PortfolioWidgetProvider extends AppWidgetProvider {

    public final static String ACTION_OPEN_PORTFOLIO = "KEY OPEN PORTFOLIO";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction("Default action");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.portfolio_widget);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static void updateWidgetText(Context context, AppWidgetManager appWidgetManager, int appWidgetId, double portfolioValue) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.portfolio_widget);
        String formattedVal = "$" + RandomUtils.getFormattedCurrencyAmount(portfolioValue);
        views.setTextViewText(R.id.appwidget_text, "Portfolio Value:\n" + formattedVal);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(ACTION_OPEN_PORTFOLIO);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}

