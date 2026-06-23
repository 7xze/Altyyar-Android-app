-keepnames class com.altayyar.app.entity.HashTag
-if class com.altayyar.app.entity.HashTag
-keep class com.altayyar.app.entity.HashTagJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.HashTag
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.HashTag {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.Boolean,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
