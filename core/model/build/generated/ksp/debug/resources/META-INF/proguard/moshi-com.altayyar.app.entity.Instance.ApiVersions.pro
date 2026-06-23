-keepnames class com.altayyar.app.entity.Instance$ApiVersions
-if class com.altayyar.app.entity.Instance$ApiVersions
-keep class com.altayyar.app.entity.Instance_ApiVersionsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$ApiVersions
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$ApiVersions {
    public synthetic <init>(java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
