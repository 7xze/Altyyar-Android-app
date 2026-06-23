-keepnames class com.altayyar.app.entity.TrendingTagHistory
-if class com.altayyar.app.entity.TrendingTagHistory
-keep class com.altayyar.app.entity.TrendingTagHistoryJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
