-keepnames class com.altayyar.app.entity.Status$Mention
-if class com.altayyar.app.entity.Status$Mention
-keep class com.altayyar.app.entity.Status_MentionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
