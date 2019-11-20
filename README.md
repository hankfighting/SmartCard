# SmartCard
#### OpenMobileAPI是提供手机客户端程序访问内置eSE或SIM完成Android设备SE空间管理和卡应用管理的API，Android P以前是通过Simalliance组织提供的API，手机厂商内置到手机系统中实现，Android P以后，谷歌将此API的实现纳入官方API中。

- [Simalliance官方地址](https://simalliance.org/)
- [Google OMA Api官方地址](https://developer.android.google.cn/reference/android/se/omapi/package-summary?hl=en)

SmartCard库是对OpenMobileAPI进行封装，方便快速集成访问手机SE，同时根据系统不同适配以上两种API，使用此库前，需确认当前客户端程序有访问SE的权限，一般需要手机厂商或者SIM卡厂商提供授权。

# 使用介绍
添加依赖

Step 1：Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2：Add the dependency


```
dependencies {
	 implementation 'com.github.hankfighting:SmartCard:V1.0'
}
```

Step 3：代码调用（必须在子线程中调用）


```
new Thread(new Runnable() {
    @Override
    public void run() {
       CardResult cardResult = SmartCard.getInstance().execute("00A4040008A000000151000000");
       if(cardResult.getStatus() == 0) {
          LogUtil.e(cardResult.getRapdu());
       } else {
          LogUtil.e(cardResult.getMessage());
       }
    }
}).start();
```


结束，调用就是这么简单，主要是需要确认客户端程序对SE的访问权限，与打包所用的签名有关。

