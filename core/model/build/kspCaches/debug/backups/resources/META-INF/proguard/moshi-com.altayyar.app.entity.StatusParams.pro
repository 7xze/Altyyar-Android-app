-keepnames class com.altayyar.app.entity.StatusParams
-if class com.altayyar.app.entity.StatusParams
-keep class com.altayyar.app.entity.StatusParamsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.StatusParams
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.StatusParams {
    public synthetic <init>(java.lang.String,java.lang.Boolean,com.altayyar.app.entity.Status$Visibility,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
