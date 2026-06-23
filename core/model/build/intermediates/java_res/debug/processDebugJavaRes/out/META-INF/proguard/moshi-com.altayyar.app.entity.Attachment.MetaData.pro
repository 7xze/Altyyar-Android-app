-keepnames class com.altayyar.app.entity.Attachment$MetaData
-if class com.altayyar.app.entity.Attachment$MetaData
-keep class com.altayyar.app.entity.Attachment_MetaDataJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Attachment$MetaData
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Attachment$MetaData {
    public synthetic <init>(com.altayyar.app.entity.Attachment$Focus,java.lang.Float,com.altayyar.app.entity.Attachment$Size,com.altayyar.app.entity.Attachment$Size,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
