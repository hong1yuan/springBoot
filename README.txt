springBoot整合
mybatis
hbase
redis
kafka
定时任务
动态定时任务
Md5加密
AES加密
多线程随机写数据
读取配置文件
多线程
启动停止脚步
拦截器
rpc
文件图片上传
Jasypt加密
@SpringBootTest注解进行单元测试
java队列
版本比较
ajax登录
热部署
扫描dao层 有@MapperScan(注解则不需要在mapper接口中写@Mapper注解（启动类中写@MapperScan和mapper接口中写@Mapper注解存在一个就行）
http停止服务（http://127.0.0.1:30000/shutdown）

增加spring-boot-starter-actuator监控POM 增加mysql version为8.0
配置文件增加监控配置 端口：30000等
已查明之前监控不好用是因为接口安全未关闭导致 之后会加入接口安全security相关功能
以下是相关路径
GET 	/autoconfig 	查看自动配置的使用情况
GET 	/configprops 	查看配置属性，包括默认配置
GET 	/beans 	查看bean及其关系列表
GET 	/dump 	打印线程栈
GET 	/env 	查看所有环境变量
GET 	/env/{name} 	查看具体变量值
GET 	/health 	查看应用健康指标
GET 	/info 	查看应用信息（需要自己在application.properties里头添加信息，比如info.contact.email=easonjim@163.com）
GET 	/mappings 	查看所有url映射
GET 	/metrics 	查看应用基本指标
GET 	/metrics/{name} 	查看具体指标
POST 	/shutdown 	关闭应用（要真正生效，得配置文件开启endpoints.shutdown.enabled: true）
GET 	/trace 	查看基本追踪信息

JDBC连接 批量插入数据


打包 
右键工程（或者右键pom文件）--> run As-->maven build 输入命令  clean install -P online（或者clean package -P online）(install是安装 package是打包  -P online是只把online的配置文件打包)

start-springBoot.bat 是windows 启动脚步
start-springBoot.sh  是linux   启动脚步