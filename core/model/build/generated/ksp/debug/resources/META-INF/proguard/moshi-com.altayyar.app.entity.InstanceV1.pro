-keepnames class com.altayyar.app.entity.InstanceV1
-if class com.altayyar.app.entity.InstanceV1
-keep class com.altayyar.app.entity.InstanceV1JsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.InstanceV1
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.InstanceV1 {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.Integer,com.altayyar.app.entity.PollConfiguration,com.altayyar.app.entity.InstanceConfiguration,java.lang.Integer,com.altayyar.app.entity.PleromaConfiguration,java.lang.Integer,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
