-keepnames class com.altayyar.app.entity.AccountSource
-if class com.altayyar.app.entity.AccountSource
-keep class com.altayyar.app.entity.AccountSourceJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.AccountSource
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.AccountSource {
    public synthetic <init>(com.altayyar.app.entity.Status$Visibility,java.lang.Boolean,java.lang.String,java.util.List,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
