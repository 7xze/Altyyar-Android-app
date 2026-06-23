-keepnames class com.altayyar.app.entity.ScheduledStatus
-if class com.altayyar.app.entity.ScheduledStatus
-keep class com.altayyar.app.entity.ScheduledStatusJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
