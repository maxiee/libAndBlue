libAndBlue
==========

Android 平台上的一个简单的蓝牙库，对API进行简单地封装

可调用函数
==========

打开蓝牙：enable()
关闭蓝牙：disable()
蓝牙可见：discoverable(int duration)
获取配对设备：getBondDevices()
搜索设备：discovery()
搜索注册广播监听：registerBroadCast()
绑定设备：setBond(BluetoothDevice remoteDevice)
取消绑定：removeBond(BluetoothDevice remoteDevice)
蓝牙是否开启：isEnable()
