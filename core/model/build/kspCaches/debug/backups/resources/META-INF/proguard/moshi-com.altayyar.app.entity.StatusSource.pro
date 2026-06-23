-keepnames class com.altayyar.app.entity.StatusSource
-if class com.altayyar.app.entity.StatusSource
-keep class com.altayyar.app.entity.StatusSourceJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
