-keepnames class com.altayyar.app.entity.DeletedStatus
-if class com.altayyar.app.entity.DeletedStatus
-keep class com.altayyar.app.entity.DeletedStatusJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.DeletedStatus
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.DeletedStatus {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,com.altayyar.app.entity.Status$Visibility,boolean,java.util.List,com.altayyar.app.entity.Poll,java.util.Date,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
