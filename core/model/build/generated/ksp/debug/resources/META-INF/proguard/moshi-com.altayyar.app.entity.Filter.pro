-keepnames class com.altayyar.app.entity.Filter
-if class com.altayyar.app.entity.Filter
-keep class com.altayyar.app.entity.FilterJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Filter
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Filter {
    public synthetic <init>(java.lang.String,java.lang.String,java.util.List,java.util.Date,com.altayyar.app.entity.Filter$Action,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
