-keepnames class com.altayyar.app.entity.PollOption
-if class com.altayyar.app.entity.PollOption
-keep class com.altayyar.app.entity.PollOptionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.PollOption
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.PollOption {
    public synthetic <init>(java.lang.String,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
