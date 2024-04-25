## Android通过intent传递parcelable数据中遇到的问题

#### 1.问题场景

##### 当前场景需要使用intent来传递一个数据类型这个类型是自定义类的一个class，

通常做法是

​	1.在intent中put放入该数据类

​	2.让该数据类声明为parcelable数据

​	3.在activity oncreate时取出该数据

#### 2.目前的情况

​	能放入数据，数据不为null，但是在activity中取出时，发现数据为null，这是为啥呢

#### 3.问题原因

​	后续排查发现，这个parcelable的对象，有部分的数据项是null，因此在取的过程中对应不上，这个情况下需要特别声明某数据项可能为null的情况，kotlin配置如下：

```
val videonum: String?
```

### 4.总结

Android的parcelable数据转换过程和对应协议规定必须严格对应传入和取出的数据类型以及数据值