-keepnames class com.altayyar.app.entity.StatusEdit
-if class com.altayyar.app.entity.StatusEdit
-keep class com.altayyar.app.entity.StatusEditJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.StatusEdit
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.StatusEdit {
    public synthetic <init>(java.lang.String,java.lang.String,boolean,java.util.Date,com.altayyar.app.entity.TimelineAccount,com.altayyar.app.entity.Poll,java.util.List,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
