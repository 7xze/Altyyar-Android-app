-keepnames class com.altayyar.app.entity.ScheduledStatusReply
-if class com.altayyar.app.entity.ScheduledStatusReply
-keep class com.altayyar.app.entity.ScheduledStatusReplyJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
