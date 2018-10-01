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
    	* [2.2 VISCUIT 초기화](#22-viscuit-초기화)
    	* [2.3 콜백을 받기 위한 리스너 등록](#23-콜백을-받기-위한-리스너-등록)
    	* [2.4 광고 호출](#24-콜백을-받기-위한-리스너-등록-및-광고-호출)
    	* [2.5 광고 정보 갱신하기](#25-광고-정보-갱신하기)
    	* [2.6 광고 상태 체크](#26-광고-상태-체크)
    	* [2.7 라이프 사이클 등록](#27-라이프-사이클-등록)
    	* [2.8 개발 모드 셋팅](#28-개발-모드-셋팅)
    	* [2.9 Proguard](#29-proguard)
    * [3. VISCUIT 서비스 참고사항](#3-VISCUIT-서비스-참고사항)
    
	


---

## VISCUIT SDK 구성
- <span style="color:red"> 1.1.5 버전 : viscuit_android_sdk_1_1_5.jar</span>
- <span style="color:red"> 1.1.6 버전 : viscuit_android_sdk_1_1_6.aar</span>
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
##### 1.3 <span style="color:red">viscuit Activity 추가</span>
```xml
    <activity
        android:name="com.viscuit.sdk.ViscuitActivity"
        android:clearTaskOnLaunch="false"
        android:configChanges="keyboardHidden|orientation|screenSize"
        android:launchMode="singleTask"
        android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen"
        android:windowSoftInputMode="stateHidden" >
    </activity>
```

### 2. VISCUIT 사용하기


#### 2.1 라이브러리 IMPORT 

#### 2.1.1 라이브러리 IMPORT (버전 : 1.1.5을 사용할 경우)
배포된 viscukit_android.jar를 프로젝트 내에 library로 import 한다.(ex./libs)

#### 2.1.2 라이브러리 IMPORT (버전 : 1.1.6을 사용할 경우)
- jar 파일이 아니라 aar 파일임을 주의한다. 
- 배포된 viscuit_android_sdk_1_1_6.aar를 프로젝트 내에 library로 import 한다.(ex./libs)
- build.gradle 파일을 열고 아래의 사항을 추가한다. 
- aar 파일을 추가할 때 아래의 정보를 참고하여 추가한다. 
	- 패키지 : com.viscuit.sdk 
	- 파일네임 : viscuit_android_sdk_1_1_6
	- 버전 : 1.1.6

android {

	...
	
	useLibrary  'org.apache.http.legacy'	# org.apache.http 패키지를 사용하기 위해서 사용한다. 
	
	... 
	
}

repositories {

    flatDir {
    
        dirs 'libs'	# viscuit_android_sdk_1_1_6.aar 파일이 있는 디렉토리를 지정한다 
	
    }
}

dependencies {

    implementation fileTree(dir: 'libs', include: ['*.jar'])
    
    implementation ('com.viscuit.sdk:viscuit_android_sdk_1_1_6:1.1.6@aar')
    
	... 
	
}



#### 2.2 VISCUIT 초기화
현재 슬롯 상태를 초기화 한다. Init시 광고 정보를 가져와서 영상 정보를 저장합니다.
슬롯 아이디를 변경할 이유가 없다면 Application에 최초 1번 설정하는것이 좋습니다.
```java
ViscuitSDK.init(this, "viscuit", "test_ads");
```
Viscuit을 Android 6.0 이상에서 사용하실 경우에는
android.permission.WRITE_EXTERNAL_STORAGE 권한을 미리 획득하신후 초기화를 하셔야 합니다.

#### 2.3 <span style="color:red"> 콜백을 받기 위한 리스너 등록</span>
리워드 지급 목적으로 제공되는 Listener입니다.
사용자가 동영상 광고 시청에 대한 CallBack을 제공합니다.

<span style="color:red"> ADReady CallBAck 삭제됨. 1.1.5</span>

```java
ViscuitSDK.setViscuitListener(new onViscuitListner() {
			@Override
			public void adcallbackmessage(ViscuitResult result) {
				switch(result) {
				case ERROR:
					//네트워크 에러 및 광고 시청 중간에 앱 종료 등
					break;
				case NOAD:
					// 광고 없음
					break;
				case SKIP:
					//스킵 상품의 광고를 스킵함.
					break;
				case SUCCESS:
					//정상적으로 광고 시청을 모두 완료함.
					//리워드 지급
					break;
				}
			}
		});
```

#### 2.4 광고 호출
동영상 광고를 노출 합니다.
이때 재생할 영상이 없다면 CallBack으로 NoAd가 전달 됩니다.
```java
ViscuitSDK.viscuitStart();
```


#### 2.5 <span style="color:red">광고 정보 갱신하기</span>

서버로부터 최신의 광고 데이터를 받아온다.

```java
ViscuitSDK.reloadAdStatus(this);
ViscuitSDK.reloadAdStatus(this, onReloadListener listener);
```

#### 2.6 <span style="color:red">광고 상태 체크</span>

SDK 내부에 있는 광고 상태를 리턴한다.
ViscuitSDK.reloadAdStatus(this) 를 호출 함으로써 최신의 정보로 갱신이 가능하다.

```java
(boolean) ViscuitSDK.isAdReady(this);
```

#### 2.7 라이프 사이클 등록
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


#### 2.8 개발 모드 셋팅
개발모드를 설정한다. 해당 값이 true일 경우에는 샘플 광고가 노출 됩니다.
또한 시뮬레이터에서도 동작합니다.
```java
ViscuitSDK.setDevType(true);
```


#### 2.9 Proguard
난독화를 하실 경우에는 아래 코드를 추가해주시기 바랍니다.
난독화가 제대로 동작하지 않을 경우에는 동영상 재생 후 랜딩이 제대로 되지 않을수 있습니다.
```java
#Google 라이브러리
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}

#Vistcuit 라이브러리관련
-keep class com.viscuit.sdk.** { *; }

```

### 3. VISCUIT 서비스 참고사항
- 광고 영상은 Download & Play 방식을 취한다. 
- 광고 다운로드 시점은 광고 재생 요청시에만 광고 영상을 다운로드한다. 
- 다운로드된 영상은 viscuit 이라는 디렉토리에 생성된다. 
- 그 밖의 이미지 파일이나 viscuit 서비스를 위해서 필요한 파일들도 된다. 
