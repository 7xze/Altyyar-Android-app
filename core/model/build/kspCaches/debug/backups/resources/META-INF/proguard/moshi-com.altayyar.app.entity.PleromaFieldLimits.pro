-keepnames class com.altayyar.app.entity.PleromaFieldLimits
-if class com.altayyar.app.entity.PleromaFieldLimits
-keep class com.altayyar.app.entity.PleromaFieldLimitsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.PleromaFieldLimits
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.PleromaFieldLimits {
    public synthetic <init>(java.lang.Integer,java.lang.Integer,java.lang.Integer,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
