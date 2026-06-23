-keepnames class com.altayyar.app.entity.AccessToken
-if class com.altayyar.app.entity.AccessToken
-keep class com.altayyar.app.entity.AccessTokenJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
