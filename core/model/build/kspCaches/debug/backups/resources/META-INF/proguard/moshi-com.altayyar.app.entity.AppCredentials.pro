-keepnames class com.altayyar.app.entity.AppCredentials
-if class com.altayyar.app.entity.AppCredentials
-keep class com.altayyar.app.entity.AppCredentialsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
