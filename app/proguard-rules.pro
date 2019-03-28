# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# -------------------------------------------------------------------------------------
# Android용 Account Kit SDK의 특정 이전 버전과 함께 ProGuard를 사용하는 경우,
# Account Kit에서 앱에 대한 Facebook 분석 데이터를 수집하지 못할 수도 있습니다.
# Account Kit 전환 분석 기능을 계속 사용하려면 다음과 같은 ProGuard 규칙을 포함해야 합니다.
# 출처: https://developers.facebook.com/docs/accountkit/android/configuration?locale=ko_KR
-keep class com.facebook.FacebookSdk {
   boolean isInitialized();
}
-keep class com.facebook.appevents.AppEventsLogger {
   com.facebook.appevents.AppEventsLogger newLogger(android.content.Context);
   void logSdkEvent(java.lang.String, java.lang.Double, android.os.Bundle);
}
