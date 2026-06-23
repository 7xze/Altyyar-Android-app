-keepnames class com.altayyar.app.entity.TrendingTag
-if class com.altayyar.app.entity.TrendingTag
-keep class com.altayyar.app.entity.TrendingTagJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
