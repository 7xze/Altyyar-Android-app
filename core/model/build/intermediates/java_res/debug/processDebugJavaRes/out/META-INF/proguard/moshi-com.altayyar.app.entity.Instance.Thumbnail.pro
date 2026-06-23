-keepnames class com.altayyar.app.entity.Instance$Thumbnail
-if class com.altayyar.app.entity.Instance$Thumbnail
-keep class com.altayyar.app.entity.Instance_ThumbnailJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Thumbnail
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Thumbnail {
    public synthetic <init>(java.lang.String,java.lang.String,com.altayyar.app.entity.Instance$Thumbnail$Versions,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
