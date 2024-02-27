# Handler

handler组成

包含一个可执行方法

run()

实际handler是向某一个Thread的Looper对象中添加了一条MessageQueue

Looper会最终循环到这条Message并在对应的Thread中执行