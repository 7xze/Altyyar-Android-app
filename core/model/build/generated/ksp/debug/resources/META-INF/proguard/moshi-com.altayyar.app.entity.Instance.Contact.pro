-keepnames class com.altayyar.app.entity.Instance$Contact
-if class com.altayyar.app.entity.Instance$Contact
-keep class com.altayyar.app.entity.Instance_ContactJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
