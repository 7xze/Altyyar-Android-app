-keepnames class com.altayyar.app.entity.FilterResult
-if class com.altayyar.app.entity.FilterResult
-keep class com.altayyar.app.entity.FilterResultJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
