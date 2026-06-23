-keepnames class com.altayyar.app.entity.StatusConfiguration
-if class com.altayyar.app.entity.StatusConfiguration
-keep class com.altayyar.app.entity.StatusConfigurationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.StatusConfiguration
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.StatusConfiguration {
    public synthetic <init>(java.lang.Integer,java.lang.Integer,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
