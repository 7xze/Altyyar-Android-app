-keepnames class com.altayyar.app.entity.Instance$Configuration$Statuses
-if class com.altayyar.app.entity.Instance$Configuration$Statuses
-keep class com.altayyar.app.entity.Instance_Configuration_StatusesJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Configuration$Statuses
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Configuration$Statuses {
    public synthetic <init>(java.lang.Integer,java.lang.Integer,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
