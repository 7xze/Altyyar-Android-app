-keepnames class com.altayyar.app.entity.Instance$Configuration$MediaAttachments
-if class com.altayyar.app.entity.Instance$Configuration$MediaAttachments
-keep class com.altayyar.app.entity.Instance_Configuration_MediaAttachmentsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Configuration$MediaAttachments
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Configuration$MediaAttachments {
    public synthetic <init>(java.lang.Long,java.lang.Long,java.lang.Long,java.lang.Long,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
