-keepnames class com.altayyar.app.entity.MediaAttachmentConfiguration
-if class com.altayyar.app.entity.MediaAttachmentConfiguration
-keep class com.altayyar.app.entity.MediaAttachmentConfigurationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.MediaAttachmentConfiguration
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.MediaAttachmentConfiguration {
    public synthetic <init>(java.util.List,java.lang.Integer,java.lang.Integer,java.lang.Integer,java.lang.Integer,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
