-keepnames class com.altayyar.app.entity.NotificationRequest
-if class com.altayyar.app.entity.NotificationRequest
-keep class com.altayyar.app.entity.NotificationRequestJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
