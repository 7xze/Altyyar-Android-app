-keepnames class com.altayyar.app.entity.InstanceRules
-if class com.altayyar.app.entity.InstanceRules
-keep class com.altayyar.app.entity.InstanceRulesJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
