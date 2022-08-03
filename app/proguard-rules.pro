#Google
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keepclassmembers, allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

#enums
-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}