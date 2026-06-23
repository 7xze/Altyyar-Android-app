-keepnames class com.altayyar.app.entity.Notification
-if class com.altayyar.app.entity.Notification
-keep class com.altayyar.app.entity.NotificationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Notification
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Notification {
    public synthetic <init>(com.altayyar.app.entity.Notification$Type,java.lang.String,com.altayyar.app.entity.TimelineAccount,com.altayyar.app.entity.Status,com.altayyar.app.entity.Report,boolean,com.altayyar.app.entity.RelationshipSeveranceEvent,com.altayyar.app.entity.AccountWarning,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
