-keepnames class com.altayyar.app.entity.Relationship
-if class com.altayyar.app.entity.Relationship
-keep class com.altayyar.app.entity.RelationshipJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Relationship
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Relationship {
    public synthetic <init>(java.lang.String,boolean,boolean,boolean,boolean,boolean,boolean,boolean,java.lang.Boolean,boolean,java.lang.String,java.lang.Boolean,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
