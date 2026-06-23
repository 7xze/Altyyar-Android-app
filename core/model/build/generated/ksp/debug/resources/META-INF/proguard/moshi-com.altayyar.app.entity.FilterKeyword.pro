-keepnames class com.altayyar.app.entity.FilterKeyword
-if class com.altayyar.app.entity.FilterKeyword
-keep class com.altayyar.app.entity.FilterKeywordJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
