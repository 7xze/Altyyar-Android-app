-keepnames class com.altayyar.app.entity.Field
-if class com.altayyar.app.entity.Field
-keep class com.altayyar.app.entity.FieldJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Field
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Field {
    public synthetic <init>(java.lang.String,java.lang.String,java.util.Date,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
