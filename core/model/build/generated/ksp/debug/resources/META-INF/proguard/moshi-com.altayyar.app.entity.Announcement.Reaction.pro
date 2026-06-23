-keepnames class com.altayyar.app.entity.Announcement$Reaction
-if class com.altayyar.app.entity.Announcement$Reaction
-keep class com.altayyar.app.entity.Announcement_ReactionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Announcement$Reaction
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Announcement$Reaction {
    public synthetic <init>(java.lang.String,int,boolean,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
