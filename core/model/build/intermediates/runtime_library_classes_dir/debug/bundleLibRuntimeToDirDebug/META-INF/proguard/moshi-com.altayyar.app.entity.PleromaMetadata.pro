-keepnames class com.altayyar.app.entity.PleromaMetadata
-if class com.altayyar.app.entity.PleromaMetadata
-keep class com.altayyar.app.entity.PleromaMetadataJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
