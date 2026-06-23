-keepnames class com.altayyar.app.entity.Instance$Thumbnail$Versions
-if class com.altayyar.app.entity.Instance$Thumbnail$Versions
-keep class com.altayyar.app.entity.Instance_Thumbnail_VersionsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Thumbnail$Versions
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Thumbnail$Versions {
    public synthetic <init>(java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
