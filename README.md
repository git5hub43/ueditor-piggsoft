# ueditor-piggsoft

[![Join the chat at https://gitter.im/piggsoft/ueditor-piggsoft](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/piggsoft/ueditor-piggsoft?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
对[ueditor](https://github.com/fex-team/ueditor) java工具包改写

##为什么要重写？
* 简单易用，开源，功能丰富。我喜欢。
* java后台写的很好，但是我的项目中文件并非存在本地，而在另外的服务器统一管理。
* 需要将里面耦合的一部分给拆开了。
* 计划先拆开，然后一步步优化。

##怎么拆？
* 将所用到的部分类改成普通的bean，去掉部分静态化，隐式加入spring，用DI来管理类。
* 加入回调类，在文件在本地存储完后，回调，该类需要自己编写。

##改动说明
* 加入[UeditorContextListener](/ueditor-piggsoft/src/main/java/com/piggsoft/ueditor/UeditorContextListener.java)，在项目初始化时，写入rootPath到System.properties。
* 加入[Context](/ueditor-piggsoft/src/main/java/com/piggsoft/ueditor/context/Context.java)，封装Conf和HttpServletRequest，且成为TheadLocal变量。
* 加入[Constants](/ueditor-piggsoft/src/main/java/com/piggsoft/ueditor/utils/Constants.java)，将字符串变量统一管理。
* 加入[Configuration](/ueditor-piggsoft/src/main/java/com/piggsoft/ueditor/context/Configuration.java)，不再从map中取值.
