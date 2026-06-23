-keepnames class com.altayyar.app.entity.InstanceConfiguration
-if class com.altayyar.app.entity.InstanceConfiguration
-keep class com.altayyar.app.entity.InstanceConfigurationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.InstanceConfiguration
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.InstanceConfiguration {
    public synthetic <init>(com.altayyar.app.entity.StatusConfiguration,com.altayyar.app.entity.MediaAttachmentConfiguration,com.altayyar.app.entity.PollConfiguration,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
