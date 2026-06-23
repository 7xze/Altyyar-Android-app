-keepnames class com.altayyar.app.entity.NotificationSubscribeResult
-if class com.altayyar.app.entity.NotificationSubscribeResult
-keep class com.altayyar.app.entity.NotificationSubscribeResultJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
