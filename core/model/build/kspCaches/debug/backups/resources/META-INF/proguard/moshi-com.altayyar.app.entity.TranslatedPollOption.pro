-keepnames class com.altayyar.app.entity.TranslatedPollOption
-if class com.altayyar.app.entity.TranslatedPollOption
-keep class com.altayyar.app.entity.TranslatedPollOptionJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
