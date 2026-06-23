-keepnames class com.altayyar.app.entity.Instance$Configuration
-if class com.altayyar.app.entity.Instance$Configuration
-keep class com.altayyar.app.entity.Instance_ConfigurationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Configuration
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Configuration {
    public synthetic <init>(com.altayyar.app.entity.Instance$Configuration$Urls,com.altayyar.app.entity.Instance$Configuration$Accounts,com.altayyar.app.entity.Instance$Configuration$Statuses,com.altayyar.app.entity.Instance$Configuration$MediaAttachments,com.altayyar.app.entity.Instance$Configuration$Polls,com.altayyar.app.entity.Instance$Configuration$Translation,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
