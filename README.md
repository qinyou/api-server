**简介**  
 `api-server` 前后端分离 后台脚手架，包含基础权限管理功能，便于二次开发。

**运行**  
```
1. IDEA clone 本项目
2. 将api_server.sql 导入 mysql, 配置application-dev.yml 中数据库连接信息
3. 运行 ApiServerApplication
4. swagger api : http://localhost:8888/v1/swagger-ui.html
5. 预览：http://localhost:8888/v1/index.html
```

**依赖**  
```
- spring-boot 2.2.0.RELEASE
- Mybatis-Plus 3.2.0
- spring-security
- java-jwt
- hutool-all 5.0.6
```

**截图**  
![资源](./预览/resource.png)
![角色](./预览/role.png)
![角色配资源](./预览/roleConfigResources.png)
![用户](./预览/user.png)
![用户配角色](./预览/userConfigRoles.png)
![日志](./预览/log.png)
![消息](./预览/msg.png)


**前台项目（基于ant-design-pro-vue)**  
[https://gitee.com/code-together/api-server-client](https://gitee.com/code-together/api-server-client)
