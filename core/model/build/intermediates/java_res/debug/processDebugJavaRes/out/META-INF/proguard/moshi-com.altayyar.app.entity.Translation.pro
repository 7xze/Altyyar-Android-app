-keepnames class com.altayyar.app.entity.Translation
-if class com.altayyar.app.entity.Translation
-keep class com.altayyar.app.entity.TranslationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Translation
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Translation {
    public synthetic <init>(java.lang.String,java.lang.String,com.altayyar.app.entity.TranslatedPoll,java.util.List,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
