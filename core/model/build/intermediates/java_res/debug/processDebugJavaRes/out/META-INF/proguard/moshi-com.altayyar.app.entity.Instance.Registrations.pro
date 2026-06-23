-keepnames class com.altayyar.app.entity.Instance$Registrations
-if class com.altayyar.app.entity.Instance$Registrations
-keep class com.altayyar.app.entity.Instance_RegistrationsJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Instance$Registrations
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Instance$Registrations {
    public synthetic <init>(boolean,boolean,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
