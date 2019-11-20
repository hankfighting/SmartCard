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
	 implementation 'com.github.hankfighting:SmartCard:1.0.1'
}
```

Step 3：代码调用

1. 设置Reader类型，默认为eSE

```
SmartCard.getInstance().setmReaderType(EnumReaderType.READER_TYPE_ESE);
```

2. 执行APDU指令（必须在子线程中调用）
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
3. 关闭SE通道和服务

```
//执行完成指令调用
SmartCard.getInstance().closeChannel();
//退出客户端必须调用
SmartCard.getInstance().closeService();
```

结束，调用就是这么简单，主要是需要确认客户端程序对SE的访问权限，与打包所用的签名有关。


# API列表

方法名 | 方法描述|调用示例
-----|-----|-----
setmReaderType | 设置Reader类型，有ESE、SIM、SD三种| SmartCard.getInstance().setmReaderType(EnumReaderType.READER_TYPE_ESE);
execute（String command） | 执行APDU指令|SmartCard.getInstance().execute("00A4040008A000000151000000");
execute（String command, String expSw） | 执行APDU指令并校验响应状态字,多个期望状态字需要用“&brvbar;”隔开|SmartCard.getInstance().execute("00A4040008A000000151000000","9000");
closeChannel（） | 关闭SE通道，执行完成指令调用|SmartCard.getInstance().closeChannel();
closeService（） | 关闭SE服务，退出程序必须调用|SmartCard.getInstance().closeService();

# 响应码STATUS
方法名 | 方法描述
-----|-----
0|成功
-1|错误
-2|期望状态字与响应状态字校验失败

# LICENSE

```
MIT License

Copyright (c) 2019 Hank

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
