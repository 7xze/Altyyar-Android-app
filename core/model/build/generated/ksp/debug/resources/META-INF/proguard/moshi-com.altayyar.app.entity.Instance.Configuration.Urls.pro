-keepnames class com.altayyar.app.entity.Instance$Configuration$Urls
-if class com.altayyar.app.entity.Instance$Configuration$Urls
-keep class com.altayyar.app.entity.Instance_Configuration_UrlsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Configuration$Urls
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Configuration$Urls {
    public synthetic <init>(java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
