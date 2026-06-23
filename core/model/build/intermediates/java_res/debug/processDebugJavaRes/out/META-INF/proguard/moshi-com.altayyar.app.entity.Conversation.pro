-keepnames class com.altayyar.app.entity.Conversation
-if class com.altayyar.app.entity.Conversation
-keep class com.altayyar.app.entity.ConversationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Conversation
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Conversation {
    public synthetic <init>(java.lang.String,java.util.List,com.altayyar.app.entity.Status,boolean,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
