# SmartCard
#### OpenMobileAPI是提供手机客户端程序访问内置eSE或SIM完成Android设备SE空间管理和卡应用管理的API，Android P以前是通过Simalliance组织提供的API，手机厂商内置到手机系统中实现，Android P以后，谷歌将此API的实现纳入官方API中。

- [Simalliance官方地址](https://simalliance.org/)
- [Google OMA Api官方地址](https://developer.android.google.cn/reference/android/se/omapi/package-summary?hl=en)

SmartCard库是对OpenMobileAPI进行封装，方便快速集成访问手机SE，同时根据系统不同适配以上两种API，使用此库前，需确认当前客户端程序有访问SE的权限，一般需要手机厂商或者SIM卡厂商提供授权。

