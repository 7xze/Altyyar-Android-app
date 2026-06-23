-keepnames class com.altayyar.app.entity.PollConfiguration
-if class com.altayyar.app.entity.PollConfiguration
-keep class com.altayyar.app.entity.PollConfigurationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.PollConfiguration
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.PollConfiguration {
    public synthetic <init>(java.lang.Integer,java.lang.Integer,java.lang.Integer,java.lang.Integer,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
