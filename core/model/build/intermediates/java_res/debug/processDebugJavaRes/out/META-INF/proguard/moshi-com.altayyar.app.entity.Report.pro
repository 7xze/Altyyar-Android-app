-keepnames class com.altayyar.app.entity.Report
-if class com.altayyar.app.entity.Report
-keep class com.altayyar.app.entity.ReportJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Report
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Report {
    public synthetic <init>(java.lang.String,java.lang.String,java.util.List,java.util.Date,com.altayyar.app.entity.TimelineAccount,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
