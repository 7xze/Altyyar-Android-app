-keepnames class com.altayyar.app.entity.Marker
-if class com.altayyar.app.entity.Marker
-keep class com.altayyar.app.entity.MarkerJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
