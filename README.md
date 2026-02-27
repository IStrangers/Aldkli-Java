

# Aldkli-Java

## 简介

Aldkli-Java 是一个基于注解的 API 元数据自动扫描与文档生成框架，专为 Spring Boot 应用设计。通过在代码中使用简洁的注解标注 API 信息，系统能够自动扫描并收集接口元数据，通过 REST 接口对外提供统一的 API 文档数据。

## 核心特性

- **注解驱动开发**：通过注解简洁标注 API 信息，无需繁琐的手动文档编写
- **自动扫描**：自动扫描指定包路径下的所有 API 注解
- **统一数据接口**：提供 REST API 统一获取所有接口元数据
- **Spring Boot 集成**：开箱即用的 Spring Boot 自动配置支持
- **结构化元数据**：完整的 API 分组、参数、返回值类型信息收集

## 快速开始

### 添加依赖

在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.msw</groupId>
    <artifactId>aldkli</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 配置扫描包路径

在 `application.properties` 或 `application.yml` 中配置需要扫描的包路径：

```properties
aldkli.scan-package=com.example.controller
```

### 使用注解标注 API

#### 1. 在 Controller 上使用 @ApiGroup

```java
@ApiGroup("用户管理")
@RequestMapping("/user")
public class UserController {
    // ...
}
```

#### 2. 在方法上使用 @Api、@ApiParams、@ApiReturnType

```java
@Api("获取用户列表")
@GetMapping("/list")
@ApiParams({
    @ApiParam(param = "username", description = "用户名", example = "admin"),
    @ApiParam(param = "pageNum", description = "页码", example = "1")
})
@ApiReturnType(name = "UserListResult", description = "用户列表", dataType = "List<User>")
public List<User> getUserList(
    @RequestParam(required = false) String username,
    @RequestParam(defaultValue = "1") Integer pageNum
) {
    // ...
}
```

#### 3. 在实体类上使用 @ApiEntity、@ApiProperty

```java
@ApiEntity("用户")
public class User {
    @ApiProperty("用户ID")
    private Long id;
    
    @ApiProperty("用户名")
    private String username;
    
    @ApiProperty("用户邮箱")
    private String email;
}
```

### 获取 API 文档数据

启动应用后，通过以下接口获取 API 元数据：

```
GET /aldkli/metaData/getApiEntryList
```

返回数据示例：

```json
[
  {
    "name": "用户管理",
    "scanPackagePath": "com.example.controller",
    "apiGroupMetaDataList": [
      {
        "name": "用户管理",
        "pathList": ["/user"],
        "apiMetaDataList": [
          {
            "name": "获取用户列表",
            "methodType": "GET",
            "pathList": ["/list"],
            "apiParamMetaDataList": [
              {
                "name": "username",
                "required": false,
                "description": "用户名",
                "type": "requestParam",
                "dataType": "String",
                "example": "admin"
              }
            ],
            "apiReturnTypeMetaData": {
              "name": "UserListResult",
              "description": "用户列表",
              "dataType": "List<User>",
              "children": []
            }
          }
        ]
      }
    ]
  }
]
```

## 注解说明

| 注解 | 作用目标 | 说明 |
|------|---------|------|
| `@Api` | 方法 | 定义 API 名称 |
| `@ApiGroup` | 类 | 定义 API 分组 |
| `@ApiParams` | 方法 | 定义 API 参数列表 |
| `@ApiParam` | 方法参数 | 定义单个参数信息 |
| `@ApiReturnType` | 方法 | 定义返回值类型信息 |
| `@ApiEntity` | 类 | 定义实体类信息 |
| `@ApiProperty` | 字段 | 定义实体属性信息 |

## 项目结构

```
src/main/java/com/msw/aldkli/
├── annotation/          # 注解定义
│   ├── Api.java
│   ├── ApiEntity.java
│   ├── ApiGroup.java
│   ├── ApiParam.java
│   ├── ApiParams.java
│   ├── ApiProperty.java
│   └── ApiReturnType.java
├── controller/          # REST 控制器
│   └── MetaDataController.java
├── meta/                # 元数据模型
│   ├── ApiEntry.java
│   ├── ApiGroupMetaData.java
│   ├── ApiMetaData.java
│   ├── ApiParamMetaData.java
│   ├── ApiReturnTypeMetaData.java
│   └── MethodType.java
├── scanner/             # API 扫描器
│   └── ApiScanner.java
├── service/             # 服务层
│   ├── MetaDataService.java
│   └── impl/MetaDataServiceImpl.java
├── util/                # 工具类
│   ├── ClassUtil.java
│   └── MultipleReturnValue.java
├── AldkliAutoConfiguration.java
└── AldkliContext.java
```

## License

本项目遵循 Apache License 2.0 协议。