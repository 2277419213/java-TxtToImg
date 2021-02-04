# JAVA 小工具

### 文本转图片

因为公司有将代码保存为图片，存档留底的习惯，所以为了更加快捷的将前端代码转成图片保存，所以有了这个小工具类！  
因为公司前端用的是 vue 框架，所以主要要生成代码图片的就只有"components","https","router","store","util","views"这几个文件夹  
具体不同文件夹的，可以到[src\main\java\com\ninestar\doctool\utils\FileUtils.java#L19](src\main\java\com\ninestar\doctool\utils\FileUtils.java#L19)这里替换

### 大概思路

1、按服务器思维，先在指定目录下载前端上传来的代码压缩包

- 修改默认路径可到[src\main\java\com\ninestar\doctool\utils\FileUtils.java#L18](src\main\java\com\ninestar\doctool\utils\FileUtils.java#L18)这里替换

2、为了避免并发文件名冲突，这里取时间作为文件名的前缀，虽然只到秒，但是正常公司不会刚好那么巧吧？！  
3、将收到的代码解压，然后遍历判断是否有存在要转换的文件夹  
4、将文件夹内的文件，不管是.vue 结尾也好，.java 结尾也好，给他改成.txt 结尾，这里用到递归，防止文件夹俄罗斯套娃  
5、将刚刚改名的 txt 文件转为图片，这里动态计算文本高度，`但是宽度不知道怎么计算，有知道的请指导下`，不过一般按照编译器规范，一行不超 80 个字符，所以给了 1000 是稳妥妥够用的。

- 修改默认宽度可到[src\main\java\com\ninestar\doctool\utils\TextToImage.java#L29](src\main\java\com\ninestar\doctool\utils\TextToImage.java#L29)

6、生成图片后，给放到 src 目录下的文件夹，同理也用了时间作为文件夹的前缀  
7、将刚刚生成的图片文件夹打包，然后返回给用户下载

### 使用方式

安装完 mvn 依赖，就可以 debug 了，端口是 9999，直接运行就是 index 页面[http://localhost:9999](http://localhost:9999)

### 其他

这里还写了一个 websocket，之前没有玩过 springboot 的 websocket，就也写了一个，运行[http://localhost:9999/websocket](http://localhost:9999/websocket)就可以看到效果
