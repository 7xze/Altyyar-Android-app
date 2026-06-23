# تيار - AlTayyar

<p align="center">
  <img src="app/src/main/res/drawable-nodpi/ic_launcher_foreground.png" alt="تيار" width="128"/>
</p>

<p align="center">
  <strong>منصة للمصممين العرب</strong><br/>
  <strong>فخر برمجة سورية</strong><br/>
  <strong>شركة 7X</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.1.21-7F52FF?logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android-API%2024--35-3DDC84?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Gradle-8.14-02303A?logo=gradle&logoColor=white"/>
  <img src="https://img.shields.io/badge/AGP-8.10.0-4285F4?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-FF6D00?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Target%20SDK-35-00C853?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/License-GPLv3-blue.svg"/>
</p>

---

## 📋 نظرة عامة | Overview

**تيار** هو تطبيق أندرويد عربي المنشأ، تم تطويره ليكون منصة متكاملة للمصممين العرب. يجمع التطبيق بين آخر التقنيات في عالم البرمجة وتجربة مستخدم سلسة وفريدة.

AlTayyar (تيار) is a feature-rich Android application built with modern architecture, providing a seamless platform for Arab designers.

---

## 🏗️ التقنيات المستخدمة | Tech Stack

### لغات البرمجة | Programming Languages

```
┌────────────────────────────────────────────────────┐
│                                                    │
│   ██  Kotlin          404 ملف     █████████████    │
│   ██  XML             383 ملف     ████████████     │
│   ██  Java              8 ملف     ██               │
│   ██  Kotlin Script      4 ملف     █               │
│   ██  JSON               9 ملف     ██              │
│   ██  TOML              1 ملف                      │
│   ██  ProGuard           1 ملف                     │
│                                                    │
└────────────────────────────────────────────────────┘
```

| اللغة | Language | الملفات | Files | الاستخدام |
|-------|----------|---------|-------|-----------|
| <img src="https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white" height="20"/> | **404** | لغة التطوير الأساسية |
| <img src="https://img.shields.io/badge/XML-FF6600?logo=xml&logoColor=white" height="20"/> | **383** | واجهات المستخدم والموارد |
| <img src="https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white" height="20"/> | **8** | كود مساعد وتوافق |
| <img src="https://img.shields.io/badge/Kotlin%20Script-7F52FF?logo=kotlin&logoColor=white" height="20"/> | **4** | ملفات بناء Gradle |
| <img src="https://img.shields.io/badge/JSON-000000?logo=json&logoColor=white" height="20"/> | **9** | إعدادات وبيانات |
| <img src="https://img.shields.io/badge/TOML-9C4221?logo=toml&logoColor=white" height="20"/> | **1** | كتالوج الإصدارات |

### 📦 المكتبات الأساسية | Core Libraries & Frameworks

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  📱 UI & UX                                                 │
│  ├── Jetpack Compose-like (AndroidX)                        │
│  ├── Material 3 Design (Material 1.12.0)                    │
│  ├── Glide 4.16.0 (تحميل وعرض الصور)                        │
│  ├── ExoPlayer / Media3 1.6.1 (تشغيل الوسائط)               │
│  └── SplashScreen API (شاشة البداية)                        │
│                                                             │
│  🏗️ Architecture (MVVM)                                    │
│  ├── Hilt 2.56.2 (حقن التبعيات)                             │
│  ├── ViewModel + LiveData/Flow                              │
│  ├── Room 2.7.1 (قاعدة بيانات محلية)                        │
│  ├── Paging 3.3.6 (تحميل تدريجي)                            │
│  └── Repository Pattern                                     │
│                                                             │
│  🌐 Networking                                              │
│  ├── Retrofit 3.0.0 (API calls)                             │
│  ├── OkHttp 4.12.0 (HTTP client)                            │
│  ├── Moshi 1.15.2 (JSON serialization)                      │
│  └── Okio 3.11.0 (I/O)                                      │
│                                                             │
│  🔐 Authentication                                          │
│  ├── Firebase Auth 23.2.0                                   │
│  ├── Google Sign-In 21.2.0                                  │
│  ├── Mastodon OAuth                                         │
│  └── Conscrypt 2.5.3 + Bouncy Castle 1.70                  │
│                                                             │
│  📬 Notifications                                           │
│  ├── UnifiedPush 2.4.0                                      │
│  ├── AndroidX Work 2.10.1                                   │
│  └── Background Services                                    │
│                                                             │
│  🧪 Testing                                                 │
│  ├── JUnit 4 / Robolectric 4.14.1                           │
│  ├── Mockito + Turbine                                      │
│  └── MockWebServer                                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🏛️ بنية المشروع | Project Architecture

```
AlTayyar/
├── app/                          # التطبيق الرئيسي
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/altayyar/app/
│   │   │   │   ├── components/       # شاشات التطبيق
│   │   │   │   │   ├── login/        # تسجيل الدخول
│   │   │   │   │   ├── compose/      # إنشاء المنشورات
│   │   │   │   │   ├── timeline/     # الجدول الزمني
│   │   │   │   │   ├── account/      # الحسابات
│   │   │   │   │   ├── search/       # البحث
│   │   │   │   │   ├── notifications/# الإشعارات
│   │   │   │   │   ├── preference/   # الإعدادات
│   │   │   │   │   └── ...
│   │   │   │   ├── db/              # قاعدة البيانات (Room)
│   │   │   │   ├── di/              # حقن التبعيات (Hilt)
│   │   │   │   ├── network/         # شبكة API
│   │   │   │   ├── util/            # أدوات مساعدة
│   │   │   │   ├── view/            # عناصر واجهة مخصصة
│   │   │   │   └── worker/          # عمال الخلفية
│   │   │   ├── res/                 # الموارد (XML, images)
│   │   │   └── AndroidManifest.xml
│   │   ├── blue/                    # flavor الإنتاج
│   │   └── green/                   # flavor الاختبار
│   └── build.gradle.kts
├── core/
│   └── model/                       # نماذج البيانات
├── gradle/
│   ├── libs.versions.toml           # كتالوج الإصدارات
│   └── wrapper/
├── build.gradle.kts                 # بناء الجذر
├── settings.gradle.kts
└── gradlew
```

---

## 📱 الميزات | Features

- ✅ **تسجيل الدخول** - Google Sign-In, Email/Password, Mastodon OAuth
- ✅ **التسجيل السوري** - دعم خاص للمستخدمين السوريين
- ✅ **الجدول الزمني** - عرض وتصفية المنشورات
- ✅ **إنشاء المنشورات** - نص، صور، فيديو، استطلاعات رأي
- ✅ **الإشعارات** - دعم UnifiedPush
- ✅ **البحث** - حسابات، هاشتاغ، منشورات
- ✅ **الرسائل الخاصة** - محادثات مباشرة
- ✅ **إدارة الحسابات** - تعديل الملف الشخصي
- ✅ **الوضع الليلي** - دعم كامل للثيم الداكن
- ✅ **النسخة العربية** - واجهة كاملة بالعربية
- ✅ **وضع المطور** - أدوات للمطورين
- ✅ **إصدارين** - Blue (إنتاج) / Green (اختبار)

---

## 🔧 متطلبات البناء | Build Requirements

| المتطلب | النسخة |
|----------|---------|
| Android Studio | Hedgehog 2024.1+ |
| JDK | 17+ |
| Android SDK | 35 |
| Gradle | 8.14 |
| Kotlin | 2.1.21 |
| Android Gradle Plugin | 8.10.0 |

### 🚀 بناء التطبيق | Build Instructions

```bash
# build الإنتاج (Blue)
./gradlew assembleBlueDebug

# build الاختبار (Green)
./gradlew assembleGreenDebug

# build جميع الإصدارات
./gradlew assembleDebug

# تنظيف وبناء
./gradlew clean assembleBlueDebug
```

---

## 🎨 flavors | النكهات

| flavor | المعرف | اسم التطبيق | الاستخدام |
|--------|--------|-------------|-----------|
| 🔵 **blue** | `com.altayyar.app` | تيار | الإنتاج |
| 🟢 **green** | `com.altayyar.app.test` | تيار Test | اختبار |

---

## 📄 الترخيص | License

```
GNU General Public License v3.0
```

هذا المشروع مرخص تحت رخصة GPL-3.0 - طالع ملف LICENSE للمزيد.

---

<p align="center">
  <strong>فخر برمجة سورية 🇸🇾</strong><br/>
  <strong>شركة 7X</strong><br/>
  <strong>© 2024-2025 AlTayyar. All rights reserved.</strong>
</p>
