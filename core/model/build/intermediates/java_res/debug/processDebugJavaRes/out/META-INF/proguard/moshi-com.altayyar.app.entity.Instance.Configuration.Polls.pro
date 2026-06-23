-keepnames class com.altayyar.app.entity.Instance$Configuration$Polls
-if class com.altayyar.app.entity.Instance$Configuration$Polls
-keep class com.altayyar.app.entity.Instance_Configuration_PollsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Configuration$Polls
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Configuration$Polls {
    public synthetic <init>(java.lang.Integer,java.lang.Integer,java.lang.Integer,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
