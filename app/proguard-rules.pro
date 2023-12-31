#SQLCipher for Room Database
-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }

 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response
 -keep class retrofit2.** { *; }
 -keepattributes *Annotation*
 -keep class com.squareup.okhttp.** { *; }
 -keep interface com.squareup.okhttp.** { *; }
 -keep class okhttp3.** { *; }
 -keep interface okhttp3.** { *; }

 # Model Class
 -keep class com.luther.github.data.model.model.** { *; }
 -keep class com.luther.github.data.network.model.** { *; }

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 -dontwarn kotlin.reflect.jvm.internal.**

 -keep class kotlin.reflect.jvm.internal.** { *; }

 -keep interface javax.annotation.Nullable

 # Uncomment this to preserve the line number information for
 # debugging stack traces.
 #-keepattributes SourceFile,LineNumberTable

 # If you keep the line number information, uncomment this to
 # hide the original source file name.
 #-renamesourcefileattribute SourceFile

 # Glide
 #-keep public class * implements com.bumptech.glide.module.GlideModule
 #-keep public class * extends com.bumptech.glide.module.AppGlideModule

 -keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

 -keep class com.google.android.gms.** { *; }
 -dontwarn com.google.android.gms.*
 -keep class com.google.api.client.** {*;}

 # JSR 305 annotations are for embedding nullability information.
 -dontwarn javax.annotation.**

 #### OkHttp, Retrofit and Moshi
 -dontwarn okhttp3.**
 -dontwarn retrofit2.Platform.Java8
 -dontwarn okio.**
 -dontwarn javax.annotation.**
 -keepclasseswithmembers class * {
     @retrofit2.http.* <methods>;
 }

 -keepclasseswithmembers class * {
     @com.squareup.moshi.* <methods>;
 }

 -keep @com.squareup.moshi.JsonQualifier interface *

 # Enum field names are used by the integrated EnumJsonAdapter.
 # values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
 # Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
 -keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
     <fields>;
     **[] values();
 }

 # Keep helper method to avoid R8 optimisation that would keep all Kotlin Metadata when unwanted
 -keepclassmembers class com.squareup.moshi.internal.Util {
     private static java.lang.String getKotlinMetadataClassName();
 }

 -keep class kotlin.Metadata { *; }
 -keepclassmembers class kotlin.Metadata {
     public <methods>;
 }

 -keepclassmembers class * {
     @com.squareup.moshi.FromJson <methods>;
     @com.squareup.moshi.ToJson <methods>;
 }

 -keepnames @kotlin.Metadata class com.yourpackage.model.**
 -keep class com.yourpackage.model.** { *; }
 -keepclassmembers class com.yourpackage.model.** { *; }

 # Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
 # EnclosingMethod is required to use InnerClasses.
 -keepattributes Signature, InnerClasses, EnclosingMethod

 # Retrofit does reflection on method and parameter annotations.
 -keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

 # Keep annotation default values (e.g., retrofit2.http.Field.encoded).
 -keepattributes AnnotationDefault

 # Retain service method parameters when optimizing.
 -keepclassmembers,allowshrinking,allowobfuscation interface * {
     @retrofit2.http.* <methods>;
 }

 # Ignore annotation used for build tooling.
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

 # Ignore JSR 305 annotations for embedding nullability information.
 -dontwarn javax.annotation.**

 # Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
 -dontwarn kotlin.Unit

 # Top-level functions that can only be used by Kotlin.
 -dontwarn retrofit2.KotlinExtensions
 -dontwarn retrofit2.KotlinExtensions.*

 # With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
 # and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
 -if interface * { @retrofit2.http.* <methods>; }
 -keep,allowobfuscation interface <1>

 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation