-keepnames class com.altayyar.app.entity.MediaTranslation
-if class com.altayyar.app.entity.MediaTranslation
-keep class com.altayyar.app.entity.MediaTranslationJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
