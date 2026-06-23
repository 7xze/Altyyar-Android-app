-keepnames class com.altayyar.app.entity.Instance
-if class com.altayyar.app.entity.Instance
-keep class com.altayyar.app.entity.InstanceJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance {
    public synthetic <init>(java.lang.String,java.lang.String,com.altayyar.app.entity.Instance$Configuration,java.util.List,com.altayyar.app.entity.PleromaConfiguration,com.altayyar.app.entity.Instance$ApiVersions,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
