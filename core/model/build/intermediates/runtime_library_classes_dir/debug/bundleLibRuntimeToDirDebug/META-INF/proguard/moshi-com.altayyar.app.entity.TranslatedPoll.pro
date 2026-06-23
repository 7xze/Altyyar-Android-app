-keepnames class com.altayyar.app.entity.TranslatedPoll
-if class com.altayyar.app.entity.TranslatedPoll
-keep class com.altayyar.app.entity.TranslatedPollJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
