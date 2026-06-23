-keepnames class com.altayyar.app.entity.NewPoll
-if class com.altayyar.app.entity.NewPoll
-keep class com.altayyar.app.entity.NewPollJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
