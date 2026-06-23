-keepnames class com.altayyar.app.entity.Poll
-if class com.altayyar.app.entity.Poll
-keep class com.altayyar.app.entity.PollJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Poll
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Poll {
    public synthetic <init>(java.lang.String,java.util.Date,boolean,boolean,int,java.lang.Integer,java.util.List,boolean,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
