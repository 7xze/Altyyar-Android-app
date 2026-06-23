-keepnames class com.altayyar.app.entity.PleromaConfiguration
-if class com.altayyar.app.entity.PleromaConfiguration
-keep class com.altayyar.app.entity.PleromaConfigurationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.PleromaConfiguration
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.PleromaConfiguration {
    public synthetic <init>(com.altayyar.app.entity.PleromaMetadata,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
