-keepnames class com.altayyar.app.entity.StatusContext
-if class com.altayyar.app.entity.StatusContext
-keep class com.altayyar.app.entity.StatusContextJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
