-keepnames class com.altayyar.app.entity.PreviewCard
-if class com.altayyar.app.entity.PreviewCard
-keep class com.altayyar.app.entity.PreviewCardJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.PreviewCard
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.PreviewCard {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.util.List,java.lang.String,java.lang.String,java.util.Date,java.lang.String,java.lang.String,int,int,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
