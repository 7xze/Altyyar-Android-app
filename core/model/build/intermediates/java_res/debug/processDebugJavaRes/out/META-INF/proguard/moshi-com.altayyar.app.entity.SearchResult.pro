-keepnames class com.altayyar.app.entity.SearchResult
-if class com.altayyar.app.entity.SearchResult
-keep class com.altayyar.app.entity.SearchResultJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
