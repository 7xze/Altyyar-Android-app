-keepnames class com.altayyar.app.entity.NotificationPolicy
-if class com.altayyar.app.entity.NotificationPolicy
-keep class com.altayyar.app.entity.NotificationPolicyJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
