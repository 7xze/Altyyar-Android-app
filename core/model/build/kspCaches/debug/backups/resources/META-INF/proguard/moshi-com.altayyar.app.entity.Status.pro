-keepnames class com.altayyar.app.entity.Status
-if class com.altayyar.app.entity.Status
-keep class com.altayyar.app.entity.StatusJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Status
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Status {
    public synthetic <init>(java.lang.String,java.lang.String,com.altayyar.app.entity.TimelineAccount,java.lang.String,java.lang.String,com.altayyar.app.entity.Status,java.lang.String,java.util.Date,java.util.Date,java.util.List,int,int,int,boolean,boolean,boolean,boolean,java.lang.String,com.altayyar.app.entity.Status$Visibility,java.util.List,java.util.List,java.util.List,com.altayyar.app.entity.Status$Application,boolean,boolean,com.altayyar.app.entity.Poll,com.altayyar.app.entity.PreviewCard,java.lang.String,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
