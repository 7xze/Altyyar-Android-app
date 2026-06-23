-keepnames class com.altayyar.app.entity.Instance$Usage
-if class com.altayyar.app.entity.Instance$Usage
-keep class com.altayyar.app.entity.Instance_UsageJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
