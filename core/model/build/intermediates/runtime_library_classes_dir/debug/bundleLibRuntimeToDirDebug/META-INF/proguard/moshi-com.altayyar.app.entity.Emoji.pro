-keepnames class com.altayyar.app.entity.Emoji
-if class com.altayyar.app.entity.Emoji
-keep class com.altayyar.app.entity.EmojiJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Emoji
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Emoji {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,boolean,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
