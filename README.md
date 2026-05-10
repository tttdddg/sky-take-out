黑马-苍穹外卖-后端
大二学生学习Java后端开发的入门练手项目
已完成：后端、前端、小程序端
商家端：Web前端+后端 

​				后台管理 增删改查、分页、查询等功能

用户端：微信小程序

​				点餐、支付

![image-20260210180500607](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260210180500607.png)

![image-20260210171216651](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260210171216651.png)





![image-20260210170559431](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260210170559431.png)

![image-20260210170439090](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260210170439090.png)

![image-20260210170518791](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260210170518791.png)





#### 配置：

​	nginx不能在中文目录下运行

​	nginx反向代理

​	登录：用MD5（不可逆）对明文进行加密




基于maven、分模块开发

移除swagger注解和依赖，接口调试使用Apifox



员工管理：

1.新增员工接口：

​	把登录接口中产生的token设为全局变量，去获取jwt令牌调试新增员工接口

​    **收不到JWT，导致后续开发功能无法测试** x

​	**<u>实际情况：Apifox 里的请求没把 token 带对-->在“接口”里加请求头Authorization: <登录拿到的token></u>**

​	eg：

![image-20260309154335880](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260309154335880.png)

解决方案：

![image-20260309160617047](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260309160617047.png)

![image-20260309160640714](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260309160640714.png)



​	ThreadLocal线程隔离：每发起一次请求，都是单独的线程-->方便记录每个当前登录用户的id   

​																											（可右键计算代码结果看id

2.分页查询接口：

​	统一处理日期类型格式问题：在WebMvcConfiguration中扩展Spring Mvc的消息转换器

3.启用禁用员工账号接口

4.编辑员工接口：

​	根据id查询接口

​	编辑信息接口



分类管理接口（6个）：和员工管理1-4个类似



菜品管理：

1.公共字段自动填充：

​	枚举、注解、AOP、反射

​	自定义注解 AutoFill，用于标识需要进行公共字段自动填充的方法

​	-->自定义切面类 AutoFillAspect，统一拦截加入了 AutoFill 注解的方法，通过反射为公共字段赋值

​	-->在 Mapper 的方法（insert、update）上加入 AutoFill 注解

2.新增菜品接口：

​	通用接口：文件上传接口

​	插入菜品数据

​    插入口味

3.菜品分页查询接口：

​	有字段命名相同

​	每条数据一一对应封装成vo对象   eg：c.name as categoryName

4.删除菜品接口：

​	一次一个/批量删除

​	起售中/套餐关联(菜品和套餐一对多，有中间表)  --> 不可删

​	删除后，关联的口味数据也要删   动态sql、性能问题

5.修改菜品：

​	根据id查询接口

​	根据类型查询分类接口（前面已实现）

​	文件上传接口（前面已实现）

​	修改菜品接口：先删除再重新插入



##### **Redis**

基于内存（读写性能高）的k-v数据库

适合存储热点数据、企业应用

> 启动：redis-server.exe redis.windows.conf
>
> stop：ctrl+c
>
> 
>
> 默认port：6379
>
> 
>
> 连接：redis-cli.exe （新bash） redis-cli.exe  -h localhost -p 6379 -a 密码
>
> ​														keys *
>
> 退出：exit
>
> 客户端默认无密码-->设：改.conf    (和mysql不同)

常用数据类型： string

​							hash（散列，java中hashMap）

​							list（可重复，java中LinkedList）

​							set（无序、无重复，java中hashSet）

​							sorted set/zset（有序、无重复）  eg：排行榜

![image-20260302042220280](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260302042220280.png)

常用命令：

![image-20260303043319213](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260303043319213.png)

![image-20260302042706554](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260302042706554.png)![image-20260303042719757](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260303042719757.png)				

![image-20260303043033491](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260303043033491.png)

![image-20260303043213538](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260303043213538.png)

![image-20260303043241613](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260303043241613.png)





店铺相关接口：

1. 设置店铺状态接口：无需数据库操作
2. 获取店铺状态接口
3. 查询店铺营业状态接口：新controller





**HttpClient：**

HTTP协议最新版本，提供HTTP协议的客户端编程工具包

![image-20260303234027329](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260303234027329.png)

创建HttpClient对象-->创建Http请求对象-->调用HttpClient的execute方法发送请求

阿里云镜像已经传递了httpclient的jar包 （common）



微信登录接口开发：

![image-20260309101108008](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260309101108008.png)

小程序商品浏览：

1. 查询分类接口
1. 根据分类 id 查询菜品接口
1. 根据分类 id 查询套餐接口
1. 根据套餐 id 查询包含的菜品接口

缓存菜品：

​	通过redis缓存菜品数据，减少数据库查询操作

![image-20260310160514263](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260310160514263.png)

​	每个存一份缓存数据，有变更就清理缓存

	1. 缓存菜品数据   DishController
	1. 清理缓存数据   redisTemplate



**Spring Cache：**

​					缓存框架，基于注解

![image-20260313092933716](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260313092933716.png)

![image-20260313092955112](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260313092955112.png)

效果一致：

![image-20260313102941227](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260313102941227.png)



缓存套餐:

​	cache依赖注入-->启动类加注解-->方法类

购物车：

​	添加购物车接口-->查看购物车接口-->清空购物车接口



弄清支付流程：

导入地址簿功能代码：

![image-20260317081049828](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260317081049828.png)

用户下单

订单支付：微信支付

![image-20260317120308135](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260317120308135.png)

！：5、10、13

1. JSAPI：调用该接口后台产生预支付交易单
2. 小程序调起支付
3. 获取临时域名   内网穿透工具cpolar    cpolar http 8080-->拿到临时域名：https://30d861da.r9.cpolar.top/

![image-20260317141619561](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260317141619561.png)

![image-20260317143431478](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260317143431478.png)

​		导入订单支付代码



Spring Task：

​	Spring框架的任务调度工具，可按照约定时间自动执行某个代码逻辑   定时处理√

​	eg：信用卡每月还款提醒、未支付订单、应用生日祝福通知

   cron表达式：字符串√  可定义任务触发时间    [在线Cron表达式生成器](https://cron.qqe2.com/)

​						   6-7个域，空格分隔，每个域代表一个含义（s、min、h、d、m、w、y）

​							eg：2022.10.12 9:00am --> 0 0 9 12 10  ？2022（s、min、h、d、m、w、y）

![image-20260317152112290](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260317152112290.png)			

订单状态定时处理接口：

1. 超时订单
2. 派送中订单



**WebSocket：**

​		基于TCP的一种新的网络协议，浏览器和服务器只需要一次握手就可以创建持久性连接并进行双向数据传输

![image-20260318023355709](C:\Users\20914\AppData\Roaming\Typora\typora-user-images\image-20260318023355709.png)

eg：视频弹幕、网页聊天、实况更新

前端案例：

```html
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <title>WebSocket Demo</title>
</head>
<body>
    <input id="text" type="text" />
    <button onclick="send()">发送消息</button>
    <button onclick="closeWebSocket()">关闭连接</button>
    <div id="message">
    </div>
</body>
<script type="text/javascript">
    var websocket = null;
    var clientId = Math.random().toString(36).substr(2);

    //判断当前浏览器是否支持WebSocket
    if('WebSocket' in window){
        //连接WebSocket节点
        websocket = new WebSocket("ws://localhost:8080/ws/"+clientId);
    }
    else{
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function(){
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    websocket.onopen = function(){
        setMessageInnerHTML("连接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function(event){
        setMessageInnerHTML(event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function(){
        setMessageInnerHTML("close");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function(){
        websocket.close();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //发送消息
    function send(){
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
	
	//关闭连接
    function closeWebSocket() {
        websocket.close();
    }
</script>
</html>

```

用户下单后第一时间通知商家-->语音播报、弹出提示框

 	1. 来电提醒
 	2. 客户催单接口



**Apache ECharts:**

​		基于js的可视化图表库，用直观的图表来表示数据

四个维度统计：先写sql定思路

 1. 营业额统计接口：

    ​		日期和营业额，用，分隔-->StringUtils.join()无法识别，用stream做分隔

 2. 用户统计接口

 3. 订单统计接口

 4. 销量排名top10接口



工作台代码导入

**Apache POI：**

​	处理文件格式的开源项目，对各种文件进行读写操作

​	POI-->excel文件读写

​	eg: 交易明细、业务系统报表

导出运营数据excel报表接口：对应表格来写就懂了

	1. 设计excel文件模板
	1. 查询近30天运营数据
	1. 数据写入模板
	1. 通过输出流把excel文件下载到客户端浏览器

​	
