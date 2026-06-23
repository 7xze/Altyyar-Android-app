-keepnames class com.altayyar.app.entity.Status$Application
-if class com.altayyar.app.entity.Status$Application
-keep class com.altayyar.app.entity.Status_ApplicationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Status$Application
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Status$Application {
    public synthetic <init>(java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
