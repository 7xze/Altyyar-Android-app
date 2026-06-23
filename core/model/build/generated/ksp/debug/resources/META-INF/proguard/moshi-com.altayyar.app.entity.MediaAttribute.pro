-keepnames class com.altayyar.app.entity.MediaAttribute
-if class com.altayyar.app.entity.MediaAttribute
-keep class com.altayyar.app.entity.MediaAttributeJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.MediaAttribute
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.MediaAttribute {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
