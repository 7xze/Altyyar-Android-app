-keepnames class com.altayyar.app.entity.MastoList
-if class com.altayyar.app.entity.MastoList
-keep class com.altayyar.app.entity.MastoListJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.MastoList
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.MastoList {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.Boolean,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
