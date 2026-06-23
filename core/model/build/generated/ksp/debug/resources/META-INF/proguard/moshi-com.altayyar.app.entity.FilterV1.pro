-keepnames class com.altayyar.app.entity.FilterV1
-if class com.altayyar.app.entity.FilterV1
-keep class com.altayyar.app.entity.FilterV1JsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.FilterV1
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.FilterV1 {
    public synthetic <init>(java.lang.String,java.lang.String,java.util.List,java.util.Date,boolean,boolean,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
