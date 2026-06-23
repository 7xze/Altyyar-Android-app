-keepnames class com.altayyar.app.entity.Announcement
-if class com.altayyar.app.entity.Announcement
-keep class com.altayyar.app.entity.AnnouncementJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Announcement
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Announcement {
    public synthetic <init>(java.lang.String,java.lang.String,java.util.Date,java.util.Date,boolean,java.util.Date,java.util.Date,boolean,java.util.List,java.util.List,java.util.List,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
