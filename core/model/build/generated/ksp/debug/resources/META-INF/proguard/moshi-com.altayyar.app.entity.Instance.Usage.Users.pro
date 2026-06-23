-keepnames class com.altayyar.app.entity.Instance$Usage$Users
-if class com.altayyar.app.entity.Instance$Usage$Users
-keep class com.altayyar.app.entity.Instance_Usage_UsersJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
