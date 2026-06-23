-keepnames class com.altayyar.app.entity.NewStatus
-if class com.altayyar.app.entity.NewStatus
-keep class com.altayyar.app.entity.NewStatusJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.NewStatus
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.NewStatus {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean,java.util.List,java.util.List,java.lang.String,com.altayyar.app.entity.NewPoll,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
