-keepnames class com.altayyar.app.entity.StringField
-if class com.altayyar.app.entity.StringField
-keep class com.altayyar.app.entity.StringFieldJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
