#VISCUIT SDK 적용가이드

목차
=================
* [VISCUIT SDK 구성](#viscuit-sdk-구성)
* [SDK 적용하기](#sdk-적용하기)
	* [1. AdroidManifest.xml 설정](#1-adroidmanifest.xml-설정)
		* [1.1 퍼미션 추가](#11-퍼미션-추가)
		* [1.2 Google Play Service meta-data 추가](#12-google-play-service-meta-data-추가)
		* [1.3 viscuit Activity 추가](#13-viscuit-activity-추가)
    * [2. VISCUIT 사용하기](#2-viscuit-사용하기)
    	* [2.1 라이브러리 IMPORT](#21-라이브러리-import)
    	* [2.2 VISCUIT 광고를 위한 변수 설정](#22-viscuit-광고를-위한-변수-설정)
    	* [2.3 광고 객체 초기화](#23-광고-객체-초기화)
    	* [2.4 콜백을 받기 위한 리스너 리스너 등록 및 광고 호출](#24-콜백을-받기-위한-리스너-등록-및-광고-호출)
    		* [2.4.1 viscuit_listener 등록](#241-viscuit_listener-등록)
    		* [2.4.2 Activity Life Cycle 체크](#242-activity-life-cycle-체크)

---

## VISCUIT SDK 구성
- viscuit_android_sdk_{version}.jar
- 샘플 프로젝트
- 연동가이드


## SDK 적용하기

###  1. AdroidManifest.xml 설정
####  1.1 퍼미션 추가
```ruby
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
Viscuit을 Android 6.0 이상에서 사용하실 경우에는
android.permission.WRITE_EXTERNAL_STORAGE 권한을 획득하셔야 합니다.

##### 1.2 Google Play Service meta-data 추가
```ruby
    <meta-data
        android:name="com.google.android.gms.version"
        android:value="@integer/google_play_services_version"/>
```
##### 1.3 viscuit Activity 추가
```xml
    <activity
        android:name="com.viscuit.sdk.viscuit_activity"
        android:launchMode="singleTask"
        android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen"
        android:configChanges="keyboardHidden|orientation|screenSize"
        android:clearTaskOnLaunch="false"
        android:windowSoftInputMode="stateHidden" >
    </activity>
```

### 2. VISCUIT 사용하기


#### 2.1 라이브러리 IMPORT
배포된 viscukit_android.jar를 프로젝트 내에 library로 import 한다.(ex./libs)


#### 2.2 VISCUIT 초기화
현재 슬롯 상태를 초기화 한다. Init시 광고 정보를 가져와서 영상 정보를 저장합니다.
슬롯 아이디를 변경할 이유가 없다면 Application에 최초 1번 설정하는것이 좋습니다.
```java
ViscuitSDK.init(this, "viscuit", "test_ads", null);
```
Viscuit을 Android 6.0 이상에서 사용하실 경우에는
android.permission.WRITE_EXTERNAL_STORAGE 권한을 미리 획득하신후 초기화를 하셔야 합니다.
#### 2.3 콜백을 받기 위한 리스너 등록 및 광고 호출
리워드 지급 목적으로 제공되는 Listener입니다.
사용자가 동영상 광고 시청에 대한 CallBack을 제공합니다.

```java
ViscuitSDK.setViscuitListener(new onViscuitListner() {
			@Override
			public void adcallbackmessage(ViscuitResult result) {
				switch(result) {
				case ERROR:
					break;
				case NOAD:
					break;
				case SKIP:
					break;
				case SUCCESS:
					break;
				}
			}
		});
```

####2.4 광고 호출
동영상 광고를 노출 합니다.
이때 재생할 영상이 없다면 CallBack으로 NoAd가 전달 됩니다.
```java
ViscuitSDK.viscuitStart();
```


####2.5 라이프 사이클 등록
올바른 광고 재생을 위해 Activity의 생명 주기를 전달합니다.
사용자의 화면 전환에 대한 처리를 하기 위함이니 Fragment가 아닌 Activity에서 호출해 주시기 바랍니다.
```java
	@Override
	public void onPause() {
		super.onPause();
		//라이프사이클 등록
		ViscuitSDK.onPause(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		//라이프사이클 등록
		ViscuitSDK.onResume(this);
	}
```



####2.6 Progard
난독화를 하실 경우에는 아래 코드를 추가해주시기 바랍니다.
난독화가 제대로 동작하지 않을 경우에는 동영상 재생 후 랜딩이 제대로 되지 않을수 있습니다.
```java
#Google 라이브러리
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}

#Vistcuit 라이브러리관련
-keep class com.viscuit.sdk.** { *; }

```
