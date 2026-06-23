-keepnames class com.altayyar.app.entity.Account
-if class com.altayyar.app.entity.Account
-keep class com.altayyar.app.entity.AccountJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.altayyar.app.entity.Account
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.altayyar.app.entity.Account {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.Date,java.lang.String,java.lang.String,java.lang.String,java.lang.String,boolean,int,int,int,com.altayyar.app.entity.AccountSource,boolean,java.util.List,java.util.List,com.altayyar.app.entity.Account,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
