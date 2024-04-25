# 讲透Android1.0~Android14各版本权限差异

众所周知，Android自诞生以来逐渐更新到了Android14的版本，Android更新的总体思路是对设备安全性，用户个人隐私性的保护越来越强，对应用权限的管理越来越严格和细分。

![image-20240416092504779](https://s2.loli.net/2024/04/16/gEF3Pw2OyRmxGLt.png)



## 初代目 Android1.0~Android4.4

非系统级权限可直接在mainfest中声明，操作系统允许让应用自行获取



## 二代目 Android5.0~Android8.0

在非系统权限中再分列出部分的危险权限，包含**读写权限/相机/日历/位置/联系人/安装应用/读取手机状态**/等权限，这些危险权限会在程序运行或安装时申请，需要用户手动允许



## 三代目 对各权限的严格化和细化调整 Android9.0~Android14.0

#### Android9主要针对后台应用获取位置信息进行了限制

![image-20240416093350805](https://s2.loli.net/2024/04/16/Ln9diKQNxqIDj7h.png)

#### Android10引入分区存储机制，限制应用对外部存储空间的访问，引入 `QUERY_ALL_PACKAGES` 权限

![image-20240416093400068](https://s2.loli.net/2024/04/16/1CbGwUYvcrlEfj3.png)

#### **Android 11.0 R**增强麦克风和摄像头权限控制，引入 `BLUETOOTH_CONNECT` 权限

![image-20240416093410338](https://s2.loli.net/2024/04/16/2GqOgpziTFfmBUZ.png)

#### Android12增强位置信息访问控制，引入 `NEARBY_WIFI` 权限

![image-20240416093421762](https://s2.loli.net/2024/04/16/HK1gNlaophR2svC.png)

#### Android13细化媒体权限，引入读取剪贴板权限、限制后台位置权限、允许应用访问设备语言偏好

![image-20240416093430767](https://s2.loli.net/2024/04/16/xj8nf59v1gQX7HA.png)

#### Android14引入访问媒体文件权限、限制应用通知权限、细化蓝牙扫描权限

![image-20240416093439310](https://s2.loli.net/2024/04/16/7JAoRSGsFfN5UxL.png)