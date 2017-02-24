# Android-BLE-Lock
基于多级安全机制的蓝牙智能门锁Android客户端实现。


该项目客户端基于Android平台，通过CC2541蓝牙芯片和底层进行通信，以HT32F1656单片机为控制核心，基于蓝牙技术、生物传感技术，RFID技术等，实现智能记忆门锁功能。
利用生物传感技术、RFID技术实现多种识别验证机制。通过APP选择系统工作模式，以适应不同的工作环境。同时通过备用复位系统，解决失效死机问题，通过备用电池电源供电系统，防止主电源断电的续航工作。

该开源内容仅为Android客户端，调试方式时使用[串口调试助手（丁丁）](http://www.cr173.com/soft/43649.html)，该串口调试助手可以自定义存储命令，实践中调试比较方便。

相关技术博文请参考个人博客：http://zhaidy.cc

欢迎提问，如果喜欢，期待您的star

软件运行截图：

<img src="https://github.com/rockzhai/BLE-Lock/blob/master/Screenshot/login.png" width = "360" height = "590" alt="login" align=center />
<img src="https://github.com/rockzhai/BLE-Lock/blob/master/Screenshot/main.png" width = "360" height = "590" alt="login" align=center />
<img src="https://github.com/rockzhai/BLE-Lock/blob/master/Screenshot/record.png" width = "360" height = "590" alt="login" align=center />
<img src="https://github.com/rockzhai/BLE-Lock/blob/master/Screenshot/usermannger.png" width = "360" height = "590" alt="login" align=center />
<img src="https://github.com/rockzhai/BLE-Lock/blob/master/Screenshot/about.png" width = "360" height = "590" alt="login" align=center />
<img src="https://github.com/rockzhai/BLE-Lock/blob/master/Screenshot/setting.png" width = "360" height = "590" alt="login" align=center />
