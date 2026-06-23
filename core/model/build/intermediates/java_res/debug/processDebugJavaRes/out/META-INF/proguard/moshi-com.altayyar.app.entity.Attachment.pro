-keepnames class com.altayyar.app.entity.Attachment
-if class com.altayyar.app.entity.Attachment
-keep class com.altayyar.app.entity.AttachmentJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Attachment
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Attachment {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,com.altayyar.app.entity.Attachment$MetaData,com.altayyar.app.entity.Attachment$Type,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
