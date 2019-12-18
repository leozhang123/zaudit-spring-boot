# zaudit-spring-boot
audit log on spring framework

## Getting Started
配置maven依赖
```xml
        <dependency>
			<groupId>com.github.leozhang123</groupId>
			<artifactId>zaudit-spring-boot-starter</artifactId>
			<version>0.1</version>
        </dependency>
```
示例：
```code
    @PostMapping("/login")
    @ResponseBody
    @AuditLog(value="login",desc="登录")
    public Map<String, Object> login(String username,String password, HttpServletRequest request){
    .......
    }
```
