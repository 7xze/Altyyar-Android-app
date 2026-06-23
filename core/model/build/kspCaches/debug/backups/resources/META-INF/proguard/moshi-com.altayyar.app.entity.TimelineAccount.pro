-keepnames class com.altayyar.app.entity.TimelineAccount
-if class com.altayyar.app.entity.TimelineAccount
-keep class com.altayyar.app.entity.TimelineAccountJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.TimelineAccount
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.TimelineAccount {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
