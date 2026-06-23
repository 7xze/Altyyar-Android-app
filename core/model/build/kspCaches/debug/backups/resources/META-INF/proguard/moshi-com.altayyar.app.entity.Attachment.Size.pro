-keepnames class com.altayyar.app.entity.Attachment$Size
-if class com.altayyar.app.entity.Attachment$Size
-keep class com.altayyar.app.entity.Attachment_SizeJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Attachment$Size
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Attachment$Size {
    public synthetic <init>(int,int,double,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
