# 使用Profile进行RecycleView过程性能指标分析

## 数据为

```
public class Player {
    boolean IsPlaying;
    boolean Sex;
    String Name;
    String Profession;
}  
```

## 数据大小为1000条时

### 内存指标为峰值消耗145M内存

![image-20240103105856997](https://s2.loli.net/2024/01/03/m3aSgdsC5wYQ7Rx.png)

## 数据大小为10000条时得到类似的值，说明增加数据量不会直接导致内存使用的增加



# 使用recycle view缓存时，对内存情况进行分析

### 当缓存数据设置为100条时

```
//设置缓存100条数据时
nlRecycleView.setItemViewCacheSize(100);
```

![image-20240103110851157](https://s2.loli.net/2024/01/03/yjJzFtN8DaGmKbp.png)

##### 如图所见，随着滑动列表，内存使用量越来越大

##### 从最初的43M内存占用达到63M内存占用，这也解释了为什么我们的手机程序运行后越用越卡的问题，即使用过程中内存不断增加，导致系统的硬件资源被消耗，最终逐渐变卡

# 最终通过测试发现，在数据仅为文本时，即使设置大小为100的recycle view缓存，在10000条数据列表量的测试下，也最终无任何OOM和卡顿问题





# 使用图片的情况下进行性能分析

![image-20240103112513807](https://s2.loli.net/2024/01/03/8S5Qcgf3Flnhd4K.png)



