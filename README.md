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

## 🆕 آخر التحديثات | Recent Changes

### الإصدار الحالي
- **إصلاح عطل الإقلاع**: تم تصحيح مسارات الكلاسات في `AndroidManifest.xml` بعد إعادة الهيكلة إلى Clean Architecture
  - جميع الأنشطة كانت قد نُقلت إلى `presentation.ui.*` لكن manifest كان لا يزال يشير إلى المسارات القديمة
  - تم حذف `TrendingActivity` و `DraftsActivity` (بدون ملفات مصدرية)
- **إضافة سوق الخدمات**: تصفح وشراء خدمات التصميم، سلة تسوق، طلبات، لوحة بائع
- **إزالة ملفات البناء من Git**: تم حذف artifacts البناء من tracking لتقليل حجم المستودع
- **ترقيات المكتبات**: Kotlin 2.1.21, AGP 8.10.0, Gradle 8.14

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
├── app/                                      # التطبيق الرئيسي
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/altayyar/app/
│   │   │   │   ├── data/                      # طبقة البيانات
│   │   │   │   │   ├── local/                 #   قاعدة البيانات (Room)
│   │   │   │   │   │   ├── dao/               #     7 DAOs
│   │   │   │   │   │   ├── entity/            #     8 كيانات
│   │   │   │   │   │   ├── AppDatabase.java   #     40+ ترحيلات
│   │   │   │   │   │   └── Converters.kt
│   │   │   │   │   ├── remote/                #   شبكة API (Retrofit)
│   │   │   │   │   ├── repository/            #   تنفيذ المستودعات
│   │   │   │   │   └── di/                    #   وحدات Hilt
│   │   │   │   ├── domain/                    # طبقة المجال
│   │   │   │   │   ├── entity/                #   كيانات المجال
│   │   │   │   │   ├── repository/            #   واجهات المستودعات
│   │   │   │   │   └── usecase/               #   حالات الاستخدام
│   │   │   │   ├── presentation/              # طبقة العرض
│   │   │   │   │   ├── ui/                    #   واجهات المستخدم
│   │   │   │   │   │   ├── activity/          #     الأنشطة
│   │   │   │   │   │   ├── adapter/           #     المحولات
│   │   │   │   │   │   ├── feature/           #     الميزات
│   │   │   │   │   │   │   ├── marketplace/   #       السوق
│   │   │   │   │   │   │   ├── cart/          #       السلة
│   │   │   │   │   │   │   ├── orders/        #       الطلبات
│   │   │   │   │   │   │   ├── sellerdashboard/#       لوحة البائع
│   │   │   │   │   │   │   ├── compose/       #       إنشاء منشورات
│   │   │   │   │   │   │   ├── timeline/      #       الجدول الزمني
│   │   │   │   │   │   │   ├── account/       #       الحسابات
│   │   │   │   │   │   │   ├── search/        #       البحث
│   │   │   │   │   │   │   ├── notifications/ #       الإشعارات
│   │   │   │   │   │   │   ├── login/         #       تسجيل الدخول
│   │   │   │   │   │   │   ├── filters/       #       التصفية
│   │   │   │   │   │   │   ├── announcements/ #       الإعلانات
│   │   │   │   │   │   │   ├── scheduled/     #       المنشورات المجدولة
│   │   │   │   │   │   │   ├── report/        #       الإبلاغ
│   │   │   │   │   │   │   ├── domainblocks/  #       حظر النطاقات
│   │   │   │   │   │   │   ├── conversations/ #       المحادثات
│   │   │   │   │   │   │   ├── viewthread/    #       عرض الخيط
│   │   │   │   │   │   │   └── followedtags/  #       الوسوم المتابعة
│   │   │   │   │   │   ├── fragment/          #     الشظايا
│   │   │   │   │   │   └── view/              #     عناصر مخصصة
│   │   │   │   │   ├── viewmodel/             #   نماذج العرض
│   │   │   │   │   ├── state/                 #   حالات الواجهة
│   │   │   │   │   └── events/                #   نظام الأحداث
│   │   │   │   ├── di/                        # وحدات Hilt العامة
│   │   │   │   ├── receiver/                  # مستقبلات البث
│   │   │   │   ├── service/                   # خدمات الخلفية
│   │   │   │   ├── util/                      # أدوات مساعدة
│   │   │   │   └── worker/                    # عمال الخلفية
│   │   │   ├── res/                           # الموارد (XML, images)
│   │   │   └── AndroidManifest.xml
│   │   ├── blue/                              # flavor الإنتاج
│   │   └── green/                             # flavor الاختبار
│   └── build.gradle.kts
├── core/
│   └── model/                                 # نماذج البيانات (45+ كيان)
│       └── src/main/java/com/altayyar/app/entity/
├── gradle/
│   ├── libs.versions.toml                     # كتالوج الإصدارات
│   └── wrapper/
├── build.gradle.kts                           # بناء الجذر
├── settings.gradle.kts
└── gradlew
```

---

## 📱 الميزات | Features

| القسم | الميزة | الحالة |
|-------|--------|--------|
| 🔐 **تسجيل الدخول** | Google Sign-In, Email/Password, Mastodon OAuth | ✅ |
| 🇸🇾 **التسجيل السوري** | دعم خاص للمستخدمين السوريين | ✅ |
| 📰 **الجدول الزمني** | عرض وتصفية المنشورات مع تحميل تدريجي | ✅ |
| ✏️ **إنشاء المنشورات** | نص، صور، فيديو، استطلاعات رأي، جدولة | ✅ |
| ✏️ **تعديل المنشورات** | تعديل المنشورات بعد النشر عبر SendStatusService | ✅ |
| 🔔 **الإشعارات** | دعم UnifiedPush مع إشعارات دفع وسحب | ✅ |
| 🔔 **سياسات الإشعارات** | إدارة تصفية الإشعارات حسب النوع | ✅ |
| 🔔 **طلبات الإشعارات** | مراجعة وإدارة طلبات الإشعارات | ✅ |
| 🔍 **البحث** | حسابات، هاشتاغ، منشورات مع تحميل تدريجي | ✅ |
| 💬 **الرسائل الخاصة** | محادثات مباشرة مع تحميل تدريجي | ✅ |
| 👤 **إدارة الحسابات** | تعديل الملف الشخصي، إضافة/تبديل حسابات متعددة | ✅ |
| 🌙 **الوضع الليلي** | دعم كامل للثيم الداكن | ✅ |
| 🇸🇦 **النسخة العربية** | واجهة كاملة بالعربية مع دعم RTL | ✅ |
| 🔧 **وضع المطور** | أدوات للمطورين (محاكاة أخطاء التحميل) | ✅ |
| 📦 **سوق الخدمات** | تصفح وشراء خدمات التصميم (شعارات، تصاميم، تصوير) | ✅ |
| 🛒 **سلة التسوق** | إضافة وإزالة الخدمات وإتمام الطلبات | ✅ |
| 📋 **الطلبات** | عرض حالة الطلبات (قيد الانتظار، قيد التنفيذ، مكتمل) | ✅ |
| 👨‍💼 **لوحة البائع** | إدارة الخدمات، المحفظة، وإضافة خدمات جديدة | ✅ |
| 📢 **الإعلانات** | عرض إعلانات الخادم مع التفاعلات (إيموجي) | ✅ |
| 🚫 **التصفية** | إنشاء وتحرير وإدارة فلاتر المحتوى | ✅ |
| #️⃣ **الوسوم المتابعة** | متابعة وإلغاء متابعة الهاشتاغات | ✅ |
| 📅 **المنشورات المجدولة** | عرض وجدولة وإدارة المنشورات المؤجلة | ✅ |
| 🚨 **الإبلاغ** | الإبلاغ عن المنشورات والحسابات المسيئة | ✅ |
| 🚫 **حظر النطاقات** | حظر وإلغاء حظر نطاقات الخوادم | ✅ |
| ⚙️ **إعدادات التبويبات** | تخصيص علامات التبويب الظاهرة | ✅ |
| 📋 **القوائم** | إنشاء وإدارة قوائم Mastodon | ✅ |
| 🔗 **مشاركة سريعة** | مشاركة النصوص والصور ومقاطع الفيديو من أي تطبيق | ✅ |
| 🎚️ **زر الإعدادات السريعة** | إنشاء منشور من لوحة الإعدادات السريعة | ✅ |
| 🖼️ **عرض الوسائط** | عرض الصور والفيديوهات بملء الشاشة | ✅ |
| ℹ️ **حول وتراخيص** | شاشة حول التطبيق وتراخيص المصادر المفتوحة | ✅ |
| 🎨 **إصدارين** | Blue (إنتاج) / Green (اختبار) | ✅ |

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

## 🧩 وحدات حقن التبعيات | DI Modules (Hilt)

| الوحدة | الوظيفة |
|--------|---------|
| `NetworkModule` | توفير Moshi, OkHttpClient, Retrofit, MastodonApi, MediaUploadApi |
| `StorageModule` | توفير SharedPreferences, Room AppDatabase مع 40+ ترحيل |
| `MarketplaceModule` | ربط MarketplaceRepository بالتنفيذ الافتراضي |
| `PlayerModule` | توفير ExoPlayer مع دعم MP4, WebM, MP3, FLAC, WAV, Ogg, ترجمات |
| `CoroutineScopeModule` | توفير CoroutineScope @ApplicationScope للمهام الطويلة |
| `NotificationManagerModule` | توفير NotificationManager |
| `PreferencesEntryPoint` | EntryPoint للوصول إلى SharedPreferences خارج Hilt |

---

## 📡 نظام الأحداث | Event System

نظام مركزي للتواصل بين المكونات عبر `EventsHub` (SharedFlow):

| الحدث | المعنى |
|-------|--------|
| `StatusChanged` | تغيير منشور (تعديل/حذف) |
| `Unfollow` / `Block` / `Mute` | تفاعلات الحساب |
| `StatusDeleted` | حذف منشور |
| `StatusComposed` / `StatusScheduled` | إنشاء/جدولة منشور |
| `ProfileEdited` | تعديل الملف الشخصي |
| `PreferenceChanged` | تغيير الإعدادات |
| `PollVote` / `PollShowResults` | تفاعلات الاستطلاع |
| `NewNotifications` | وصول إشعارات جديدة |
| `FilterUpdated` | تحديث الفلاتر |
| `ConversationsLoading` / `NotificationsLoading` | حالة التحميل |

---

## ⚙️ خدمات وخلفيات | Services, Receivers & Workers

### خدمات الخلفية
| الخدمة | الوظيفة |
|--------|---------|
| `SendStatusService` | خدمة أمامية لإرسال/تعديل المنشورات مع إعادة المحاولة ورفع الوسائط |
| `TayyarTileService` | زر الإعدادات السريعة لإنشاء منشور جديد |

### مستقبلات البث
| المستقبل | الوظيفة |
|----------|---------|
| `UnifiedPushBroadcastReceiver` | استقبال رسائل UnifiedPush |
| `SendStatusBroadcastReceiver` | رد سريع على الإشعارات عبر RemoteInput |
| `NotificationBlockStateBroadcastReceiver` | التفاعل مع تغيير حالة حظر قنوات الإشعارات |

### عمال الخلفية
| العامل | الوظيفة |
|--------|---------|
| `NotificationWorker` | جلب وعرض الإشعارات في الخلفية |
| `PruneCacheWorker` | تنظيف دوري لقاعدة البيانات والوسائط المخبأة |

---

## 🗄️ قاعدة البيانات | Room Database

- **قاعدة البيانات**: `AppDatabase.java` مع 40+ ترحيل
- **DAOs**: AccountDao, ConversationsDao, InstanceDao, NotificationsDao, NotificationPolicyDao, TimelineAccountDao, TimelineDao, TimelineStatusDao
- **الكيانات**: AccountEntity, ConversationEntity, HomeTimelineEntity, InstanceEntity, NotificationEntity, NotificationPolicyEntity, TimelineAccountEntity, TimelineStatusEntity
- **السكيمات**: 9 ملفات JSON ترحيل (v48-v70) في `app/schemas/`

---

## 📦 نموذج البيانات الأساسي | Core Model Module

`core/model/` يحتوي على **45+ كيان بيانات** مشترك:

| الكيان | الوصف |
|--------|-------|
| `Account`, `Status`, `Notification` | الكيانات الأساسية |
| `Attachment`, `Poll`, `Conversation` | المرفقات والاستطلاعات |
| `Filter`, `FilterV1`, `FilterKeyword` | تصفية المحتوى |
| `Instance`, `InstanceV1`, `InstanceConfiguration` | معلومات الخادم |
| `Announcement`, `Marker`, `MastoList` | الإعلانات والعلامات |
| `TrendingTag`, `TrendingTagsResult` | الوسوم الرائجة |
| `ScheduledStatus`, `StatusEdit`, `StatusSource` | المنشورات المجدولة |
| `Translation`, `PreviewCard`, `Card` | الترجمة والبطاقات |
| `Report`, `AccountWarning` | الإبلاغ |
| `Relationship`, `RelationshipSeveranceEvent` | العلاقات |
| `NewStatus`, `AppCredentials`, `AccessToken` | إنشاء منشور والمصادقة |

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
  <strong>جميع الحقوق محفوظة لـ 7X</strong><br/>
  <strong>© 2026-2027 7X. All rights reserved.</strong>
</p>
