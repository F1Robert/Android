# Gradle 在Gradle官方仓库发布自研的plugins

## 1.create account 创建gradle账号

​	https://plugins.gradle.org/user/register

​	注册后点击邮箱中的链接激活

## 2.创建API KEY并且在Gradle中进行配置

​	https://plugins.gradle.org/u/nolan?tab=publishing

​	并且将API KEY的内容写入gradle的

​	HOME_DIR/.gradle/gradle.properties

​	同样的API KEY可以多次无限次使用到任何想要发布的plugin中

​	![image-20231228105025116](https://s2.loli.net/2023/12/28/IzFxKbyALpf467q.png)

​	

在~user_home\.gradle\gradle.properties下新增

```
gradle.publish.key=nPujj57ZoiJ6jvLjKK8MIHUXOHUcgr4F
gradle.publish.secret=4gW2VJH0mmYsYnNGTqYLAQh3R9wZkXEt
```

![image-20231228105200525](https://s2.loli.net/2023/12/28/VShJHFuGLNo7Ok9.png)

## 3.配置gradle plugin的发布名称和版本 同样可以修改配置进行更新操作

![image-20231226092442594](https://s2.loli.net/2023/12/26/5SVdX4jsOa36JCi.png)

## 4.配置gradle的详细信息

​	需要先启用gradle-java-plugin

![image-20231228105326831](https://s2.loli.net/2023/12/28/FkWvQVrwqKRD9Ld.png)



1.group 对应gradle的owner项 可以填自身或者填其它

2.version 版本 需要各版本对应

3.website 项目的网站

4.vcsUrl x项目的git repository的地址 如果其它人想要contribute你的项目 他可以通过git仓库找到它

![image-20231226093659623](https://s2.loli.net/2023/12/26/7IDOcHb8RnylrY4.png)

​	5.id 在gradle仓库中显示的项目名称

​	6.displayname 短的人类可读的正常名称

​	7.description 项目的描述 功能 用途等

​	8.tags.set[] 项目的标签，可以是多个，通常写项目关联的场景/用途等

​	9.implementionclasses 项目的类

![image-20231226093722025](https://s2.loli.net/2023/12/26/93RypkEqs6MKOXn.png)







## 示例图

![image-20231226093645939](https://s2.loli.net/2023/12/26/BXA4Vg6ONyEU2mD.png)



## 5.发布你的plugin

​	可以在cmd窗口命令行也可以在gradle的任务中找到publishPlugins

![image-20231226094904844](https://s2.loli.net/2023/12/26/qJ5C7WnAU8MmRhB.png)