-keepnames class com.altayyar.app.entity.MediaUploadResult
-if class com.altayyar.app.entity.MediaUploadResult
-keep class com.altayyar.app.entity.MediaUploadResultJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
