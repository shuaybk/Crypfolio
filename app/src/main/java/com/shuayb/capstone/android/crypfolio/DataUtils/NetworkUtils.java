package com.shuayb.capstone.android.crypfolio.DataUtils;

import android.net.Uri;

public class NetworkUtils {
    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";
    private static final String COINS_PATH = "coins/";
    private static final String MARKETS_PATH = "markets";
    private static final String CHART_PATH = "/market_chart";

    private static final String CURRENCY_KEY = "vs_currency";
    private static final String ORDER_KEY = "order";
    private static final String PER_PAGE_KEY = "per_page";
    private static final String PAGE_KEY = "page";
    private static final String SPARKLINE_KEY = "sparkline";
    private static final String PRICE_CHANGE_PERCENTAGE_KEY = "price_change_percentage";
    private static final String DAYS_KEY = "days";

    private static final String CURRENCY_USD = "usd";
    private static final String ORDER_MARKET_CAP = "market_cap_desc";
    private static final String ITEMS_PER_PAGE = "100";
    private static final String PAGE = "1";
    private static final String SPARKLINE = "false";
    private static final String PRICE_CHANGE_PERCENTAGE = "24h";
    private static final String DAYS_1YEAR = "365";

    public static String getUrlForMarketviewData() {
        String baseUrl = BASE_URL + COINS_PATH + MARKETS_PATH;

        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(CURRENCY_KEY, CURRENCY_USD)
                .appendQueryParameter(ORDER_KEY, ORDER_MARKET_CAP)
                .appendQueryParameter(PER_PAGE_KEY, ITEMS_PER_PAGE)
                .appendQueryParameter(PAGE_KEY, PAGE)
                .appendQueryParameter(SPARKLINE_KEY, SPARKLINE)
                .appendQueryParameter(PRICE_CHANGE_PERCENTAGE_KEY, PRICE_CHANGE_PERCENTAGE)
                .build();

        return builtUri.toString();
    }

    public static String getUrlForChartData(String id) {
        String baseUrl = BASE_URL + COINS_PATH + id + CHART_PATH;

        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(CURRENCY_KEY, CURRENCY_USD)
                .appendQueryParameter(DAYS_KEY, DAYS_1YEAR)
                .build();

        return  builtUri.toString();
    }
}
