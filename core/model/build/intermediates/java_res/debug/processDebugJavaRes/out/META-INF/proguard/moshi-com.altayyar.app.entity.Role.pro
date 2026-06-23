-keepnames class com.altayyar.app.entity.Role
-if class com.altayyar.app.entity.Role
-keep class com.altayyar.app.entity.RoleJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
