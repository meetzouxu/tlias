# 1 Overview

tlias 这个项目主要是用 SpringBoot 实现简单的员工和部门的增删查改，另外还包括文件上传、登录等功能。通过这个入门项目可以学习到一个 SpringBoot 项目的整体框架。

来源：[黑马JavaWeb开发教程](https://www.bilibili.com/video/BV1m84y1w7Tb/?share_source=copy_web&vd_source=01b5eac057dbbcc3bd810b2f249c3420)

![[Image/Pasted image 20240724184448.png]]

## 1.1 Submodule Design

将模块全部放在一个 Java 项目里不便于维护和复用，分模块设计方便项目的扩展，以及资源调用。

![[Image/Pasted image 20240724185857.png]]

# 2 Environment

## 2.1 Database Table(dept、emp)

```sql
-- 部门管理
create table dept(
    id int unsigned primary key auto_increment comment '主键ID',
    name varchar(10) not null unique comment '部门名称',
    create_time datetime not null comment '创建时间',
    update_time datetime not null comment '修改时间'
) comment '部门表';

insert into dept (id, name, create_time, update_time) values(1,'学工部',now(),now()),(2,'教研部',now(),now()),(3,'咨询部',now(),now()), (4,'就业部',now(),now()),(5,'人事部',now(),now());

-- 员工管理(带约束)
create table emp (
  id int unsigned primary key auto_increment comment 'ID',
  username varchar(20) not null unique comment '用户名',
  password varchar(32) default '123456' comment '密码',
  name varchar(10) not null comment '姓名',
  gender tinyint unsigned not null comment '性别, 说明: 1 男, 2 女',
  image varchar(300) comment '图像',
  job tinyint unsigned comment '职位, 说明: 1 班主任,2 讲师, 3 学工主管, 4 教研主管, 5 咨询师',
  entrydate date comment '入职时间',
  dept_id int unsigned comment '部门ID',
  create_time datetime not null comment '创建时间',
  update_time datetime not null comment '修改时间'
) comment '员工表';

INSERT INTO emp
	(id, username, password, name, gender, image, job, entrydate,dept_id, create_time, update_time) VALUES
	(1,'jinyong','123456','金庸',1,'1.jpg',4,'2000-01-01',2,now(),now()),
	(2,'zhangwuji','123456','张无忌',1,'2.jpg',2,'2015-01-01',2,now(),now()),
	(3,'yangxiao','123456','杨逍',1,'3.jpg',2,'2008-05-01',2,now(),now()),
	(4,'weiyixiao','123456','韦一笑',1,'4.jpg',2,'2007-01-01',2,now(),now()),
	(5,'changyuchun','123456','常遇春',1,'5.jpg',2,'2012-12-05',2,now(),now()),
	(6,'xiaozhao','123456','小昭',2,'6.jpg',3,'2013-09-05',1,now(),now()),
	(7,'jixiaofu','123456','纪晓芙',2,'7.jpg',1,'2005-08-01',1,now(),now()),
	(8,'zhouzhiruo','123456','周芷若',2,'8.jpg',1,'2014-11-09',1,now(),now()),
	(9,'dingminjun','123456','丁敏君',2,'9.jpg',1,'2011-03-11',1,now(),now()),
	(10,'zhaomin','123456','赵敏',2,'10.jpg',1,'2013-09-05',1,now(),now()),
	(11,'luzhangke','123456','鹿杖客',1,'11.jpg',5,'2007-02-01',3,now(),now()),
	(12,'hebiweng','123456','鹤笔翁',1,'12.jpg',5,'2008-08-18',3,now(),now()),
	(13,'fangdongbai','123456','方东白',1,'13.jpg',5,'2012-11-01',3,now(),now()),
	(14,'zhangsanfeng','123456','张三丰',1,'14.jpg',2,'2002-08-01',2,now(),now()),
	(15,'yulianzhou','123456','俞莲舟',1,'15.jpg',2,'2011-05-01',2,now(),now()),
	(16,'songyuanqiao','123456','宋远桥',1,'16.jpg',2,'2007-01-01',2,now(),now()),
	(17,'chenyouliang','123456','陈友谅',1,'17.jpg',NULL,'2015-03-21',NULL,now(),now());
```

## 2.2 Create SpringBoot Project

创建springboot工程，引入对应的起步依赖（Spring Web、MyBatis Framework、MySQL Driver、Lombok）

详情参考：[[Java/Java Language/Java Web/05-SpringBoot|05-SpringBoot]]

## 2.3 Mybatis Configuation

配置文件application.properties中引入mybatis的配置信息，准备对应的实体类（部门+员工类）

- application.properties ：

~~~properties
#数据库连接
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/tlias
spring.datasource.username=root
spring.datasource.password=123456

#开启mybatis的日志输出
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl

#开启数据库表字段 a_Column 到实体类属性 aColumn 的驼峰映射
mybatis.configuration.map-underscore-to-camel-case=true
~~~

>提前创建好 tlias 数据库。

- 在pojo文件夹下创建实体类Dept和Emp：

~~~java
/*部门类*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    private Integer id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
~~~

~~~java
/*员工类*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Short gender;
    private String image;
    private Short job;
    private LocalDate entrydate;
    private Integer deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
~~~


- 准备对应的Controller、Mapper、Service(接口、实现类)基础结构

项目工程结构：

![image-20221213224927868](Image/image-20221213224927868.png)

### 2.3.1 Controller

1. 在Controller文件夹下创建控制层：

- DeptController

~~~java
package com.github.controller;
import org.springframework.web.bind.annotation.RestController;

//部门管理控制器
@RestController
public class DeptController {
}
~~~

- EmpController

~~~java
package com.github.controller;
import org.springframework.web.bind.annotation.RestController;

//员工管理控制器
@RestController
public class EmpController {
}
~~~

### 2.3.2 Service

2. 在Service文件夹下创建业务层：

- DeptService

~~~java
package com.github.service;

//部门业务规则
public interface DeptService {
}
~~~

- impl文件夹下的DeptServiceImpl

~~~java
package com.github.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//部门业务实现类
@Slf4j
@Service
public class DeptServiceImpl implements DeptService {
}
~~~

- EmpService

~~~java
package com.github.service;

//员工业务规则
public interface EmpService {
}
~~~

- impl文件夹下的EmpServiceImpl

~~~java
package com.github.service.impl;
import com.github.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

//员工业务实现类
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

}
~~~

### 2.3.3 Mapper

3. 在Mapper文件夹下创建数据访问层：

- DeptMapper

~~~java
package com.github.mapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptMapper {
}
~~~

- EmpMapper

~~~java
package com.github.mapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpMapper {
}
~~~

## 2.4 Development Specification-Restful

REST（REpresentational State Transfer），表述性状态转换，它是一种软件架构风格

- 传统风格
```
http://localhost:8080/user/getById?id=1     GET：查询id为1的用户
http://localhost:8080/user/saveUser         POST：新增用户
http://localhost:8080/user/updateUser       POST：修改用户
http://localhost:8080/user/deleteUser?id=1  GET：删除id为1的用户
```

- REST风格
```
http://localhost:8080/users/1  GET：查询id为1的用户
http://localhost:8080/users    POST：新增用户
http://localhost:8080/users    PUT：修改用户
http://localhost:8080/users/1  DELETE：删除id为1的用户
```

其中总结起来，就一句话：通过URL定位要操作的资源，通过HTTP动词(请求方式)来描述具体的操作。

在REST风格的URL中，通过四种请求方式，来操作数据的增删改查。 

- GET ： 查询
- POST ：新增
- PUT ：修改
- DELETE ：删除

前后端工程在进行交互时，使用统一响应结果 Result：

- pojo文件夹下新建Result类：

```java
package com.github.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static Result success(){
        return new Result(1,"success",null);
    }
    //查询 成功响应
    public static Result success(Object data){
        return new Result(1,"success",data);
    }
    //失败响应
    public static Result error(String msg){
        return new Result(0,msg,null);
    }
}
```

# 3 Development Process

1. 查看页面原型，明确需求
2. 阅读接口文档
3. 思路分析
4. 接口开发
5. 接口测试
6. 前后端联调

# 4 Dept

## 4.1 Query Dept

- Controller 查询部门数据：

```java
@Slf4j  
@RestController  
public class DeptController {  
    @Autowired  
    private DeptService deptService;  
  
    /**  
     * 查询部门数据  
     * @return  
     */  
    //@RequestMapping(value = "/depts" , method = RequestMethod.GET)  
    @GetMapping("/depts")  
    public Result list() {  
        log.info("查询所有部门数据");  
        List<Dept> deptList = deptService.list();  
        return Result.success(deptList);  
    }  
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface DeptService {  
    /**  
     * 查询全部部门数据  
     * @return  
     */  
    List<Dept> list();  
}
```

```java
@Slf4j  
@Service  
public class DeptServiceImpl implements DeptService {  
    @Autowired  
    private DeptMapper deptMapper;  
  
    @Override  
    public List<Dept> list() {  
        return deptMapper.list();  
    }  
}
```

- Mapper 请求数据库

```java
@Mapper  
public interface DeptMapper {  
    /**  
     * 查询全部部门数据  
     * @return  
     */  
    @Select("select * from dept")  
    List<Dept> list();  
}
```

### 4.1.1 Postman

![[Image/Pasted image 20240703153400.png]]

- 使用nginx可以实现前后端联调。解压压缩包，启动nginx，访问测试：[http://localhost:90](http://localhost:90/)

### 4.1.2 Nginx

1. nginx服务器接收请求

```
http://localhost:90/api/depts
```

2. nginx服务器发送给tomcat服务器

```
http://localhost:8080/depts
```

3. 返回数据给前端渲染

所以在这里，nginx服务器取代了浏览器或者postman发送请求的角色。

## 4.2 Delete Dept

注意根据事务的概念，删除部门的同时也要删除部门下的员工：

- Controller 删除部门数据：

```java
@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
	
    /**
     * 删除部门及其对应员工
     * @param id
     * @return
     */
    @DeleteMapping("/depts/{id}")
    public Result delete(@PathVariable Integer id) {
        //日志记录
        log.info("根据id删除部门：{}",id);
        //调用service层功能
        deptService.delete(id);
        //响应
        return Result.success();
    }
    ...
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface DeptService {  
    /**
     * 删除部门及对应员工
     * @param id
     */
    void delete(Integer id); 
}
```

```java
@Slf4j  
@Service  
public class DeptServiceImpl implements DeptService {  
    @Autowired  
    private DeptMapper deptMapper;  
    
	@Autowired  
	private EmpMapper empMapper;

	@Transactional
    @Override
    public void delete(Integer id) {
        deptMapper.deleteById(id);

        empMapper.deleteByDeptId(id);
    }  
}
```

- Mapper 请求数据库

```java
@Mapper  
public interface DeptMapper {  
    /**
     * 根据ID删除部门
     * @param id
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);
	
	/**
     * 根据部门id删除员工
     * @param deptId
     */
    @Delete("delete from emp where dept_id = #{deptId}")
    void deleteByDeptId(Integer deptId);
}
```

### 4.2.1 Spring Transaction

- @Transactional
- 位置：业务（service）层的方法上、类上、接口上
- 作用：将当前方法交给Spring进行事务管理，方法执行前，开启事务；成功执行完毕，提交事务；出现异常，回滚事务

- application.yml

```yml
#spring事务管理日志
logging:
  level:
    org.springframework.jdbc.support.JdbcTransactionManager: debug
```

默认情况下，只有出现 RuntimeException 才回滚异常。假如我们使用了其他类型的异常，Transaction 注解就不会回滚了。

rollbackFor：用于控制出现何种异常类型，回滚事务。示例：

```java
@Transactional(rollbackFor = Exception.class)
```

propagation：事务的传播，当一个事务方法被另一个事务方法调用时，这个事务方法应当如何进行事务控制，比如这里的 b 方法：

```java
@Transactional
public void a(){
	...
	userService.b();
	...
}

@Transactional(propagation = Propagation.REQUIRED)
public void b(){
	//...
}
```


| 属性值          | 含义                 |
| ------------ | ------------------ |
| REQUIRED     | 默认值，需要事务，有则加入，无则新建 |
| REQUIRES_NEW | 需要新事务，无论有无总是创建新事务  |
>如果共用一个事务，回滚就都回滚了
## 4.3 Insert Dept

- Controller 插入数据

```java
@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
	
    /**
     * 新增部门
     * @param dept
     * @return
     */
    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept){
        //记录日志
        log.info("新增部门：{}",dept);
        //调用service层添加功能
        deptService.add(dept);
        //响应
        return Result.success();
    }
    ...
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface DeptService {  
    /**
     * 新增部门
     * @param dept
     */
    void add(Dept dept);
}
```

```java
@Slf4j  
@Service  
public class DeptServiceImpl implements DeptService {  
    @Autowired  
    private DeptMapper deptMapper;  
  
    @Override
    public void add(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        deptMapper.insert(dept);
    }  
}
```

- Mapper 请求数据库

```java
@Mapper  
public interface DeptMapper {  
    /**
     * 新增部门
     * @param dept
     */
    @Insert("insert into dept (name, create_time, update_time) values (#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);
}
```

## 4.4 Query by ID Dept

- Controller 请求数据

```java
@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
	
    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    @GetMapping("/depts/{id}")
    public Result listbyId(@PathVariable Integer id){
        log.info("根据id查询部门：{}",id);
        List<Dept> list= deptService.listbyId(id);
        return Result.success(list);
    }
    ...
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface DeptService {  
    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    List<Dept> listbyId(Integer id);
}
```

```java
@Slf4j  
@Service  
public class DeptServiceImpl implements DeptService {  
    @Autowired  
    private DeptMapper deptMapper;  
  
    @Override
    public List<Dept> listbyId(Integer id) {
        return deptMapper.listbyId(id);
    }  
}
```

- Mapper 请求数据库

```java
@Mapper  
public interface DeptMapper {  
    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    @Select("select * from dept where id = #{id}")
    List<Dept> listbyId(Integer id);
}
```

## 4.5 Update Dept

- Controller 更新数据

```java
@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
	
    /**
     * 根据id修改部门
     * @param id
     * @param dept
     * @return
     */
    @PutMapping("/depts/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Dept dept){
        log.info("根据id修改部门：{}",id);
        deptService.update(id,dept);
        return Result.success();
    }
    ...
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface DeptService {  
    /**
     * 根据id修改部门
     * @param id
     * @param dept
     */
    void update(Integer id, Dept dept);
}
```

```java
@Slf4j  
@Service  
public class DeptServiceImpl implements DeptService {  
    @Autowired  
    private DeptMapper deptMapper;  
  
    @Override
    public void update(Integer id, Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(id, dept);
    }
}
```

- Mapper 请求数据库

```java
@Mapper  
public interface DeptMapper {  
    /**
     * 根据id修改部门
     * @param id
     * @param dept
     */
    @Update("update dept set name = #{dept.name}, update_time = #{dept.updateTime} where id = #{id}")
    // 如果你的mapper方法中有多个参数, 你可以使用注解@Param给你的参数一个别名(供sql语句中使用), 否则的话你的参数将会以"param"为前缀 后面跟上参数的位置作为别名.
    // 例如:在这里如果不使用@Param，结果是 #{param1}, #{param2}；使用#Param后，结果是 #{id}, #{dept}
    void update(@Param("id")Integer id, @Param("dept")Dept dept);
}
```

# 5 Emp

假如前端要求返回两个SQL的结果应该怎么处理呢？答案是将这两个结果封装到一个 JavaBean 对象中：

- 在pojo文件夹下新建 JavaBean 包装类：

```java
/**  
 * 分页查询包装类  
 */  
@Data  
@NoArgsConstructor  
@AllArgsConstructor  
public class PageBean {  
    private Long total;//总记录数  
    private List rows;//数据列表  
}
```

## 5.1 Query Pagination Emp

- Controller 分页查询

```java
@Slf4j  
@RestController  
@RequestMapping("/emps")  
public class EmpController {  
  
    @Autowired  
    private EmpService empService;  
  
    //分页查询  
    @GetMapping  
    public Result page(@RequestParam(defaultValue = "1") Integer page,  
                       @RequestParam(defaultValue = "10") Integer pageSize) {  
        //记录日志  
        log.info("分页查询，参数：{},{}", page, pageSize);  
        //调用业务层分页查询功能  
        PageBean pageBean = empService.page(page, pageSize);  
        //响应  
        return Result.success(pageBean);  
    }  
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface EmpService {  
    /**  
     * 分页查询  
     * @param page  
     * @param pageSize  
     * @return  
     */  
    PageBean page(Integer page, Integer pageSize);  
}
```

```java
@Slf4j  
@Service  
public class EmpServiceImpl implements EmpService {  
    @Autowired  
    private EmpMapper empMapper;  
  
    @Override  
    public PageBean page(Integer page, Integer pageSize) {  
        //1、获取总记录数  
        Long count = empMapper.count();  
  
        //2、获取分页查询结果列表  
        Integer start = (page - 1) * pageSize; //计算起始索引 , 公式: (页码-1)*页大小  
        List<Emp> empList = empMapper.list(start, pageSize);  
  
        //3、封装PageBean对象  
        PageBean pageBean = new PageBean(count , empList);  
        return pageBean;  
    }  
}
```

- Mapper 请求数据库

```java
@Mapper  
public interface EmpMapper {  
    //获取总记录数  
    @Select("select count(*) from emp")  
    public Long count();  
  
    //获取当前页的结果列表  
    @Select("select * from emp limit #{start}, #{pageSize}")  
    public List<Emp> list(@Param("start") Integer start, @Param("pageSize") Integer pageSize);  
}
```

### 5.1.1 RequestParam Annotation

1. **绑定请求参数到方法参数**：

```java
@GetMapping("/greet")
public String greet(@RequestParam String name) {
   return "Hello, " + name;
}
```

2. **指定请求参数的名称**：

```java
@GetMapping("/greet")
public String greet(@RequestParam("username") String name) {
   return "Hello, " + name;
}
```

3. **设置请求参数的默认值**：

```java
@GetMapping("/greet")
public String greet(@RequestParam(defaultValue = "Guest") String name) {
   return "Hello, " + name;
}
```

4. **设置请求参数为可选**：

```java
@GetMapping("/greet")
public String greet(@RequestParam(required = false) String name) {
   if (name == null) {
	   name = "Guest";
   }
   return "Hello, " + name;
}
```

### 5.1.2 PageHelper

1、在pom.xml引入依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.4.2</version>
</dependency>
```

2、EmpServiceImpl

```java
@Override
public PageBean page(Integer page, Integer pageSize) {
    // 设置分页参数
    PageHelper.startPage(page, pageSize); 
    // 执行分页查询
    List<Emp> empList = empMapper.list(); 
    // 获取分页结果
    Page<Emp> p = (Page<Emp>) empList;   
    //封装PageBean
    PageBean pageBean = new PageBean(p.getTotal(), p.getResult()); 
    return pageBean;
}
```

3、EmpMapper

```java
@Mapper
public interface EmpMapper {
    //获取当前页的结果列表
    @Select("select * from emp")
    public List<Emp> list();
}
```

## 5.2 Conditional Pagination Query Emp

- 条件查询SQL：

```sql
select * 
from emp
where 
  name like concat('%','张','%')   -- 条件1：根据姓名模糊匹配
  and gender = 1                   -- 条件2：根据性别精确匹配
  and entrydate = between '2000-01-01' and '2010-01-01'  -- 条件3：根据入职日期范围匹配
order by update_time desc;
```

- Controller 分页查询

```java
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    //条件分页查询
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Short gender,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        //记录日志
        log.info("分页查询，参数：{},{},{},{},{},{}", page, pageSize,name, gender, begin, end);
        //调用业务层分页查询功能
        PageBean pageBean = empService.page(page, pageSize, name, gender, begin, end);
        //响应
        return Result.success(pageBean);
    }
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface EmpService {
    /**
     * 条件分页查询
     * @param page     页码
     * @param pageSize 每页展示记录数
     * @param name     姓名
     * @param gender   性别
     * @param begin   开始时间
     * @param end     结束时间
     * @return
     */
    PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end);
}
```

```java
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
        //设置分页参数
        PageHelper.startPage(page, pageSize);
        //执行条件分页查询
        List<Emp> empList = empMapper.list(name, gender, begin, end);
        //获取查询结果
        Page<Emp> p = (Page<Emp>) empList;
        //封装PageBean
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;
    }
}
```

- Mapper 请求数据库

```java
@Mapper
public interface EmpMapper {
    //获取当前页的结果列表
    public List<Emp> list(@Param("name") String name, @Param("gender") Short gender, @Param("begin")LocalDate begin, @Param("end") LocalDate end);
}
```

- EmpMapper.xml

注意XML映射文件同包同名：

1. 文件名和接口名一致
2. namespace 和接口全类名（右键 copy reference）一致
3. id、返回和接口中的方法名、返回类型一致

![[Image/Pasted image 20240704155256.png]]

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.mapper.EmpMapper">

    <!-- 条件分页查询 -->
    <select id="list" resultType="com.github.pojo.Emp">
        select * from emp
        <where>
            <if test="name != null and name != ''">
                name like concat('%',#{name},'%')
            </if>
            <if test="gender != null">
                and gender = #{gender}
            </if>
            <if test="begin != null and end != null">
                and entrydate between #{begin} and #{end}
            </if>
        </where>
        order by update_time desc
    </select>
</mapper>
```


### 5.2.1 DateTimeFormat Annotation

指定前端传递的日期格式：

```java
@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin
```

## 5.3 Delete Emp

- Controller 批量删除员工

```java
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("批量删除，ids：{}", ids);
        empService.delete(ids);
        return Result.success();
    }
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface EmpService {

    /**
     * 批量删除操作
     * @param ids id集合
     */
     void delete(List<Integer> ids);
}
```

```java
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public void delete(List<Integer> ids) {
        empMapper.delete(ids);
    }
}
```

- Mapper 请求数据库

```java
@Mapper
public interface EmpMapper {
    /**
     * 批量删除
     * @param ids
     */
    void delete(@Param("ids") List<Integer> ids);
}
```

- EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.mapper.EmpMapper">

    <!--批量删除员工-->
    <delete id="delete">
        delete from emp where id in
        <foreach collection="ids" item="id" separator="," open="(" close=")">
            #{id}
        </foreach>
    </delete>
    ...

</mapper>
```

## 5.4 Insert Emp

- Controller 新增员工

```java
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 新增员工
     * @param ids
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工，参数：{}", emp);
        empService.save(emp);
        return Result.success();
    }
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface EmpService {

    /**
     * 新增员工
     * @param emp
     */
    void save(Emp emp);
}
```

```java
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
    }
}
```

- Mapper 请求数据库

```java
@Mapper
public interface EmpMapper {
    /**
     * 新增员工
     * @param emp
     */
    @Insert("insert into emp(username, name, gender, image, job, entrydate, dept_id, create_time, update_time) " +
            "values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);
}
```

- EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.mapper.EmpMapper">

    <!--批量删除员工-->
    <delete id="delete">
        delete from emp where id in
        <foreach collection="ids" item="id" separator="," open="(" close=")">
            #{id}
        </foreach>
    </delete>
    ...

</mapper>
```

## 5.5 Query by ID Emp

- Controller 新增员工

```java
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("查询员工，id：{}", id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface EmpService {

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Emp getById(Integer id);
}
```

```java
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public Emp getById(Integer id) {
        return empMapper.getById(id);
    }
}
```

- Mapper 请求数据库

```java
@Mapper
public interface EmpMapper {
    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select * from emp where id = #{id}")
    Emp getById(Integer id);
}
```

## 5.6 Update Emp

- Controller 新增员工

```java
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 新增员工
     * @param ids
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工，参数：{}", emp);
        empService.save(emp);
        return Result.success();
    }
}
```

- Controller 传递给 Service 处理逻辑：

```java
public interface EmpService {

    /**
     * 新增员工
     * @param emp
     */
    void save(Emp emp);
}
```

```java
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);
    }
}
```

- Mapper 请求数据库

```java
@Mapper
public interface EmpMapper {
    /**
     * 新增员工
     * @param emp
     */
    @Insert("insert into emp(username, name, gender, image, job, entrydate, dept_id, create_time, update_time) " +
            "values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);
}
```

- EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.github.mapper.EmpMapper">

    <!--批量删除员工-->
    <delete id="delete">
        delete from emp where id in
        <foreach collection="ids" item="id" separator="," open="(" close=")">
            #{id}
        </foreach>
    </delete>
    ...

</mapper>
```
# 6 File Upload

文件上传，是指将本地图片、视频、音频等文件上传到服务器，供其他用户浏览或下载的过程。

## 6.1 Local Store

- 前端界面（存放在`resources/static/upload.html`）：

```html
<form action="/upload" method="post" enctype="multipart/form-data">  
    姓名: <input type="text" name="username"><br>  
    年龄: <input type="text" name="age"><br>  
    头像: <input type="file" name="image"><br>  
    <input type="submit" value="提交">  
</form>
```

- 服务端接收 Controller：

```java
@Slf4j  
@RestController  
public class UploadController {  
    @PostMapping("/upload")  
    public Result upload(String username, Integer age, MultipartFile image) throws IOException {  
        log.info("文件上传：{}, {}, {}", username, age, image);  
        // 获取原始文件名  
        String originalFilename = image.getOriginalFilename();  
  
        // 获取后缀  
        int index = originalFilename.lastIndexOf(".");  
        String extname = originalFilename.substring(index);  
        String newFileName = UUID.randomUUID().toString() + extname;  
        log.info("新的文件名:{}",newFileName);  
  
        // 文件存储到 D:\ComputerScience\Java        image.transferTo(new File("D:\\ComputerScience\\Java\\" +newFileName));  
        return Result.success();  
    }  
}
```

默认状态上传文件的最大值是1mb，可以在配置文件`application.properties`中修改：

```properties
# 单个文件上传大小限制  
spring.servlet.multipart.max-file-size=10MB  
  
# 单个请求上传大小限制（单次请求多个）  
spring.servlet.multipart.max-request-size=100MB
```

## 6.2 Aliyun OSS

阿里云对象存储OSS（Object Storage Service），是一款海量、安全、低成本、高可靠的云存储服务。使用OSS，您可以通过网络随时存储和调用包括文本、图片、音频和视频等在内的各种文件。

1. 参照官方SDK编写入门程序。

SDK：Software Development Kit 的缩写，软件开发工具包，包括辅助软件开发的依赖（jar包）、代码示例等，都可以叫做SDK。

2. 开通对象存储服务(OSS)，打开OSS管理控制台界面。
3. 创建bucket：名称设置为`webleadnews`，权限设置为公共读。

Bucket：**存储空间**是用户用于存储对象（Object，就是文件）的容器，所有的对象都必须隶属于某个存储空间。

4. 获取AccessKey：创建AccessKey->获取AccessKey ID、AccessKey Secret

### 6.2.1 SDK

[Java_对象存储 OSS-阿里云帮助中心 ](https://help.aliyun.com/zh/oss/developer-reference/java/?spm=a2c4g.11186623.0.0.56942921JenouO)

SDK：Software Development Kit 的缩写，软件开发工具包，包括辅助软件开发的依赖（jar包）、代码示例等，都可以叫做SDK。

在Maven项目中加入依赖项：

- 在Maven工程中使用OSS Java SDK，只需在`pom.xml`中加入相应依赖即可。以3.15.1版本为例，在`<dependencies>`中加入如下内容：

```
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.15.1</version>
</dependency>
```

- 如果使用的是Java 9及以上的版本，则需要添加jaxb相关依赖。添加jaxb相关依赖示例代码如下：

```
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>
<!-- no more than 2.3.3-->
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.3</version>
</dependency>
```

- 上传文件的示例Demo文件

```java
package com.github;  
  
import com.aliyun.oss.ClientException;  
import com.aliyun.oss.OSS;  
import com.aliyun.oss.common.auth.*;  
import com.aliyun.oss.OSSClientBuilder;  
import com.aliyun.oss.OSSException;  
import com.aliyun.oss.model.PutObjectRequest;  
import com.aliyun.oss.model.PutObjectResult;  
import java.io.FileInputStream;  
import java.io.InputStream;  
  
public class Demo {  
  
    public static void main(String[] args) throws Exception {  
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。  
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";  
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。  
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();  
        // 填写Bucket名称，例如examplebucket。  
        String bucketName = "web-tlias-zouxu";  
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。  
        String objectName = "jdk11.exe";  
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。  
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。  
        String filePath= "D:\\ComputerScience\\DevelopTools\\Java\\jdk-11.0.15.1_windows-x64_bin.exe";  
  
        // 创建OSSClient实例。  
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);  
  
        try {  
            InputStream inputStream = new FileInputStream(filePath);  
            // 创建PutObjectRequest对象。  
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);  
            // 创建PutObject请求。  
            PutObjectResult result = ossClient.putObject(putObjectRequest);  
        } catch (OSSException oe) {  
            System.out.println("Caught an OSSException, which means your request made it to OSS, "  
                    + "but was rejected with an error response for some reason.");  
            System.out.println("Error Message:" + oe.getErrorMessage());  
            System.out.println("Error Code:" + oe.getErrorCode());  
            System.out.println("Request ID:" + oe.getRequestId());  
            System.out.println("Host ID:" + oe.getHostId());  
        } catch (ClientException ce) {  
            System.out.println("Caught an ClientException, which means the client encountered "  
                    + "a serious internal problem while trying to communicate with OSS, "  
                    + "such as not being able to access the network.");  
            System.out.println("Error Message:" + ce.getMessage());  
        } finally {  
            if (ossClient != null) {  
                ossClient.shutdown();  
            }  
        }  
    }  
}
```

## 6.3 Integrated Aliyun OSS

- 在com.github文件夹下新建utils文件夹，创建AliOSSUtils文件：

```java
/**  
 * 阿里云 OSS 工具类  
 */  
@Component
public class AliOSSUtils {  
  
    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";  
    // private String accessKeyId = "LTAI4GCH1vX6DKqJWxd6nEuW";  
    // private String accessKeySecret = "yBshYweHOpqDuhCArrVHwIiBKpyqSL";    
    private String bucketName = "web-tlias-zouxu";  
  
    /**  
     * 实现上传图片到OSS  
     */    public String upload(MultipartFile file) throws IOException, ClientException {  
        // 获取上传的文件的输入流  
        InputStream inputStream = file.getInputStream();  
  
        // 避免文件覆盖  
        String originalFilename = file.getOriginalFilename();  
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));  
  
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。  
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();  
  
        //上传文件到 OSS        
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);  
        // OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);  
        ossClient.putObject(bucketName, fileName, inputStream);  
  
        //文件访问路径  
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;  
        // 关闭ossClient  
        ossClient.shutdown();  
        return url;// 把上传到oss的路径返回  
    }  
  
}
```

- 更新UploadController从本地改为阿里云存储

```java
@Slf4j  
@RestController  
public class UploadController {  
    @Autowired  
    private AliOSSUtils aliOSSUtils;  
    @PostMapping("/upload")  
    public Result upload(MultipartFile image) throws IOException, ClientException {  
        log.info("文件上传, 文件名：{}",image.getOriginalFilename());  
        // 调用阿里云工具类进行上传  
        String url = aliOSSUtils.upload(image);  
        log.info("文件上传成功，访问路径：{}",url);  
  
        return Result.success(url);  
    }  
}
```

## 6.4 Value Annotation

将配置信息写在Java代码里很麻烦

- AliOSSUtils

```java
private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";  
// private String accessKeyId = "LTAI4GCH1vX6DKqJWxd6nEuW";  
// private String accessKeySecret = "yBshYweHOpqDuhCArrVHwIiBKpyqSL";    
private String bucketName = "web-tlias-zouxu";
```

解决方法：将配置文件写在 application.properties 利用注解读取信息

@Value 注解通常用于外部配置的属性注入，具体用法为： @Value("${配置文件中的key}")

- application.properties：

```properties
# 阿里云OSS配置  
aliyun.oss.endpoint=https://oss-cn-hangzhou.aliyuncs.com  
# aliyun.oss.accessKeyId=LTAI5tFBLg3K9cqehYvLdT1t  
# aliyun.oss.ccessKeySecret=had3BW3uYHkXIxF1H9IMH2O9bbbq3j  
aliyun.oss.bucketName=web-tlias-zouxu
```

- AliOSSUtils

```java
@Value("${aliyun.oss.endpoint}")  
private String endpoint;  
// @Value("${aliyun.oss.accessKeyId}")  
// private String accessKeyId;  
// @Value("${aliyun.oss.ccessKeySecret}")  
// private String accessKeySecret;  
@Value("${aliyun.oss.bucketName}")  
private String bucketName;
```

# 7 ConfigurationProperties 

@Configuration：当前类是配置类，自动包括了 @Component

@ConfigurationProperties 注解可以简化上面的AliOSSUtils，@Value 只能一个一个的进行的进行外部属性的注入。@ConfigurationProperties 可以批量的将外部属性配置注入到bean对象的属性中。

- AliOSSUtils

```java
@Autowired  
private AliOSSProperties aliOSSProperties;

String endpoint = aliOSSProperties.getEndpoint();  
// String accessKeyId = aliOSSProperties.getAccessKeyId();  
// String accessKeySecret = aliOSSProperties.getAccessKeySecret();  
String bucketName = aliOSSProperties.getBucketName();
```

- 在utils文件夹下创建AliOSSProperties

```java
@Data  
@Component  
@ConfigurationProperties(prefix = "aliyun.oss")  
public class AliOSSProperties {  
	private String endpoint;  
	// private String accessKeyId;  
	// private String accessKeySecret;  
	private String bucketName;  
}
```

- pom.xml

如果添加 @ConfigurationProperties 出现Sping Boot Configuration Annotation Processor not configured

```xml
<!--自动识别@ConfigurationProperties标识的bean对象-->  
<dependency>  
	<groupId>org.springframework.boot</groupId>  
	<artifactId>spring-boot-configuration-processor</artifactId>  
</dependency>
```

# 8 Login

## 8.1 Basic Login

- Controller

```java
@Slf4j  
@RestController  
public class LoginController {  
	@Autowired
    private EmpService empService;  
  
    @PostMapping("/login")  
    public Result login(@RequestBody Emp emp) {  
        log.info("员工登录：{}", emp);  
        Emp e = empService.login(emp);  
        return e != null ? Result.success(e) : Result.error("用户名或密码错误");  
    }  
}
```

- Service

```java
public interface EmpService {  
    /**
     * 员工登陆
     * @param emp
     * @return
     */
    Emp login(Emp emp);
}
```

```java
@Slf4j  
@Service  
public class EmpServiceImpl implements EmpService {  
    @Autowired  
    private EmpMapper empMapper;  
  
    @Override
    public Emp login(Emp emp) {
        return empMapper.getByUsernameAndPassWord(emp);
    } 
}
```

- Mapper

```java
@Mapper
public interface EmpMapper {
    /**
     * 根据用户名和密码查询员工
     * @param emp
     * @return
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getByUsernameAndPassWord(Emp emp);
}
```

## 8.2 Login Verification

现有的登陆机制存在问题：在未登录的情况下，我们也可以直接访问部门管理、员工管理等功能。

![[Image/Pasted image 20240708185817.png]]

### 8.2.1 Cookie and Session

- 会话：用户打开浏览器，访问web服务器的资源，**会话建立**，直到有一方断开连接，**会话结束**。在一次会话中可以包含多次请求和响应。

>一次连接过程。

- 会话跟踪：一种**维护浏览器状态的方法**，服务器需要识别多次请求是否来自于同一浏览器，以便在同一次会话的多次请求间共享数据。

- 会话跟踪方案：
	- 客户端会话跟踪技术：Cookie
	- 服务端会话跟踪技术：Session

**Cookie**：

![[Image/图片2.png]]

- 优点：HTTP协议中支持的技术（放在HTTP请求和响应头里）
- 缺点：
	- 移动端APP无法使用Cookie
	- 不安全，用户可以自己禁用Cookie
	- Cookie不能跨域

**Session**：

![](Image/方案二：Session.png)

- 优点：服务端存储，安全；服务器解析Session对象存储数据和获取数据，底层基于Cookie（根据传的Cookie里面的SessionID来寻找Session对象，确保多次请求共用一个Session）
- 缺点：
	- 服务器集群环境下无法直接使用Session（服务器负载均衡）
	- Cookie的缺点

**令牌技术**：

![](Image/方案三：令牌技术.png)

- 优点：
	- 支持PC端、移动端（这个令牌可以放在Cookie中，也可以放到其他地方）
	- 解决集群环境下的认证问题
	- 减轻服务器端存储压力
- 缺点：需要自己实现

### 8.2.2 JSON  Web  Token

JWT 全称：JSON  Web  Token [官网](https://jwt.io/)

定义了一种简洁的、自包含的格式，用于在通信双方以json数据格式**安全的传输信息**。由于数字签名（最后一部分）的存在，这些信息是可靠的。

- 登录成功后，生成令牌。在这个案例中，JWT会存储在 Local Storage 中（移动端和PC都可以使用）
- 后续每个请求，都要在请求头header中携带JWT令牌，请求头的名称为token，值为JWT令牌。系统在每次请求处理之前，先校验令牌，通过后，再处理。
- 点击退出登录后，浏览器会清空 Local Storage 中的JWT令牌。

>JWT令牌还有时效性，这也就是为什么隔一段时间就需要重新登录。

- pom.xml

```xml
<dependency>  
	<groupId>io.jsonwebtoken</groupId>  
	<artifactId>jjwt</artifactId>  
	<version>0.9.1</version>  
</dependency>
```

- 生成JWT

```java
@Test  
public void testGenJWT(){  
    Map<String, Object> Cliams = new HashMap<>();  
    Cliams.put("username","zhangsan");  
    Cliams.put("id","1");  
    String jwt = Jwts.builder()  
            .signWith(SignatureAlgorithm.HS256, "github") // 签名算法  
            .setClaims(Cliams) //自定义内容（载荷）  
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //设置过期时间为1h  
            .compact();  
    System.out.println(jwt);  
}
```

![](Image/JWT令牌生成.png)

第一、第二部分经过了Base64（A-Z a-z 0-9 + /）来表示二进制数据的编码方法，第三个部分经过了数字签名之后的结果。

获取的 JWT 令牌：

```json
eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJleHAiOjE3MjA1MTk4NzIsInVzZXJuYW1lIjoiemhhbmdzYW4ifQ.rGAvHe18KZ03Mf35eOR61N5hOnytyyHNyh7_9kTlIac
```

Decoded：

![[Image/Pasted image 20240709171443.png]]

- 校验JWT

```java
@Test  
public void testParseJWT() {  
    Claims claims = Jwts.parser()  
            .setSigningKey("github")  
            .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJleHAiOjE3MjA1MTk4NzIsInVzZXJuYW1lIjoiemhhbmdzYW4ifQ.rGAvHe18KZ03Mf35eOR61N5hOnytyyHNyh7_9kTlIac")  
            .getBody();  
  
    System.out.println(claims);  
}
```

### 8.2.3 Use JWT in Login

- 在 utils 文件夹下新建 JwtUtils：

```java
public class JwtUtils {  
  
    private static String signKey = "github";  
    private static Long expire = 43200000L;  
  
    /**  
     * 生成JWT令牌  
     * @param claims JWT第二部分负载 payload 中存储的内容  
     * @return  
     */  
    public static String generateJwt(Map<String, Object> claims){  
        String jwt = Jwts.builder()  
                .addClaims(claims)  
                .signWith(SignatureAlgorithm.HS256, signKey)  
                .setExpiration(new Date(System.currentTimeMillis() + expire))  
                .compact();  
        return jwt;  
    }  
  
    /**  
     * 解析JWT令牌  
     * @param jwt JWT令牌  
     * @return JWT第二部分负载 payload 中存储的内容  
     */  
    public static Claims parseJWT(String jwt){  
        Claims claims = Jwts.parser()  
                .setSigningKey(signKey)  
                .parseClaimsJws(jwt)  
                .getBody();  
        return claims;  
    }  
}
```

- 更新UploadController:

```java
@Slf4j  
@RestController  
public class LoginController {  
    @Autowired  
    private EmpService empService;  
  
    @PostMapping("/login")  
    public Result login(@RequestBody Emp emp) {  
        log.info("员工登录：{}", emp);  
        Emp e = empService.login(emp);  
  
        // 登陆成功。生成令牌，下发令牌：  
        if (e != null) {  
            Map<String, Object> claims = new HashMap<>();  
            claims.put("id", e.getId());  
            claims.put("name", e.getName());  
            claims.put("username", e.getUsername());  
  
            String jwt = JwtUtils.generateJwt(claims);  
            return Result.success(jwt);  
        }  
  
        // 登陆错误  
        return Result.error("用户名或密码错误");  
    }  
}
```

## 8.3 Filter

概念：Filter 过滤器，是 JavaWeb 三大组件(Servlet、Filter、Listener)之一。

过滤器可以把对资源的请求拦截下来，从而实现一些特殊的功能。

过滤器一般完成一些通用的操作，比如：登录校验、统一编码处理、敏感字符处理等。

![[Image/Pasted image 20240709184512.png]]
### 8.3.1 Demo Filter

定义Filter：定义一个类，实现 Filter 接口，并重写其所有方法。

配置Filter：Filter类上加 @WebFilter 注解，配置拦截资源的路径。

- 创建filter文件夹下的DemoFilter类：

```java
@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {
    @Override //初始化方法, 只调用一次
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init 初始化方法执行了");
    }

    @Override //拦截到请求之后调用, 调用多次
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Demo 拦截到了请求...放行前逻辑");
        //放行
        chain.doFilter(request,response);

        System.out.println("Demo 拦截到了请求...放行后逻辑");
    }

    @Override //销毁方法, 只调用一次
    public void destroy() {
        System.out.println("destroy 销毁方法执行了");
    }
}
```

>init方法和destroy方法有默认实现

为了使用Java三大组件中的Servlet，需要在启动类（TliasApplication）上加 @ServletComponentScan 开启Servlet组件支持

```java
@ServletComponentScan// 开启了对Servelt组件的支持
```

| 拦截路径   | urlPatterns值 | 含义                    |
|--------|--------------|-----------------------|
| 拦截具体路径 | /login       | 只有访问 /login 路径时，才会被拦截 |
| 目录拦截   | /emps/*      | 访问/emps下的所有资源，都会被拦截   |
| 拦截所有   | /*           | 访问所有资源，都会被拦截          |

### 8.3.2 Filter Chain

一个Web应用中，可以配置多个过滤器，多个过滤器就形成了一个过滤器链。

过滤器的顺序：注解配置的Filter，优先级是按照过滤器类名（字符串）的排序。

### 8.3.3 Use Filter in Login

- 登录请求不需要校验令牌，别的都需要。
- 拦截到请求后通过操作：令牌校验合法。

![](Image/登录拦截-Filter.png)

```xml
<!--fastJSON-->  
<dependency>  
	<groupId>com.alibaba</groupId>  
	<artifactId>fastjson</artifactId>  
	<version>1.2.76</version>  
</dependency>
```

- filter文件夹下新建LoginCheckFilter类：

```java
@Slf4j
//@WebFilter(urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1.获取请求url。
        String url = req.getRequestURL().toString();
        log.info("请求的url: {}",url);

        // 2.判断请求url中是否包含login，如果包含，说明是登录操作，放行。
        if(url.contains("login")){
            log.info("登录操作, 放行...");
            chain.doFilter(request,response);
            return;
        }

        // 3.获取请求头中的令牌（token）。
        String jwt = req.getHeader("token");

        // 4.判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头token为空,返回未登录的信息");
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return;
        }

        // 5.解析token，如果解析失败，返回错误结果（未登录）。
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {//jwt解析失败
            e.printStackTrace();
            log.info("解析令牌失败, 返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return;
        }

        // 6.放行。
        log.info("令牌合法, 放行");
        chain.doFilter(request, response);

    }
}
```

## 8.4 Interceptor

拦截器（Interceptor）是一种动态拦截方法调用的机制，类似于过滤器。Spring框架中提供的，用于动态拦截控制器方法的执行。

作用：拦截请求，在指定的方法调用前后，根据业务需要执行预先设定的代码。

![[Image/Pasted image 20240710145815.png]]
### 8.4.1 Demo Interceptor

拦截器是一种动态拦截方法调用的机制，类似于过滤器。Spring框架中提供的，用来动态拦截控制器方法的执行。

作用：拦截请求，在指定的方法调用前后，根据业务需要执行预先设定的代码。

- 创建interceptor文件夹下，新建LoginCheckInterceptor类：

```java
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override //目标资源方法运行前运行, 返回true: 放行, 放回false, 不放行
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		System.out.println("preHandle ...");
        return true;// 返回true才能访问目标资源
    }

    @Override //目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle ...");
    }

    @Override //视图渲染完毕后运行, 最后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion...");
    }
}
```

- 创建config文件夹下，创建WebConfig

```java
@Configuration //配置类
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }
}
```

| 拦截路径      | 含义            | 举例                                        |
|-----------|---------------|-------------------------------------------|
| /*        | 一级路径          | 能匹配/depts，/emps，/login，不能匹配 /depts/1      |
| /**       | 任意级路径         | 能匹配/depts，/depts/1，/depts/1/2             |
| /depts/*  | /depts下的一级路径  | 能匹配/depts/1，不能匹配/depts/1/2，/depts         |
| /depts/** | /depts下的任意级路径 | 能匹配/depts，/depts/1，/depts/1/2，不能匹配/emps/1 |

### 8.4.2 Interceptor Execution 

![](Image/拦截器-执行流程.png)

所以拦截器和过滤器的执行过程：

Filter放行前逻辑->prehandle->运行资源方法->postHander->afterCompletion->Filter放行后逻辑

区别：

- 接口规范：过滤器实现Filter接口，而拦截器实现HandlerInterceptor接口。
- 拦截范围不同：过滤器Filter会拦截所有的资源，而Interceptor只会拦截Spring的资源。

### 8.4.3 Use Interceptor in Login

- LoginCheckInterceptor：

```java
@Slf4j  
@Component  
public class LoginCheckInterceptor implements HandlerInterceptor {  
    @Override //目标资源方法运行前运行, 返回true: 放行, 放回false, 不放行  
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {  
  
        // 1.获取请求url。  
        String url = req.getRequestURL().toString();  
        log.info("请求的url: {}",url);  
  
        // 2.判断请求url中是否包含login，如果包含，说明是登录操作，放行。  
        if(url.contains("login")){  
            log.info("登录操作, 放行...");  
            return true;  
        }  
  
        // 3.获取请求头中的令牌（token）。  
        String jwt = req.getHeader("token");  
  
        // 4.判断令牌是否存在，如果不存在，返回错误结果（未登录）。  
        if(!StringUtils.hasLength(jwt)){  
            log.info("请求头token为空,返回未登录的信息");  
            Result error = Result.error("NOT_LOGIN");  
            //手动转换 对象--json --------> 阿里巴巴fastJSON  
            String notLogin = JSONObject.toJSONString(error);  
            resp.getWriter().write(notLogin);  
            return false;  
        }  
  
        // 5.解析token，如果解析失败，返回错误结果（未登录）。  
        try {  
            JwtUtils.parseJWT(jwt);  
        } catch (Exception e) {//jwt解析失败  
            e.printStackTrace();  
            log.info("解析令牌失败, 返回未登录错误信息");  
            Result error = Result.error("NOT_LOGIN");  
            //手动转换 对象--json --------> 阿里巴巴fastJSON  
            String notLogin = JSONObject.toJSONString(error);  
            resp.getWriter().write(notLogin);  
            return false;  
        }  
  
        // 6.放行。  
        log.info("令牌合法, 放行");  
        return true;  
    }  
  
    @Override //目标资源方法运行后运行  
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {  
        System.out.println("postHandle ...");  
    }  
  
    @Override //视图渲染完毕后运行, 最后运行  
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {  
        System.out.println("afterCompletion...");  
    }  
}
```

# 9 Exception

- 创建exception文件夹下的全局异常处理器GlobalExceptionHandle

```java
@RestControllerAdvice  
public class GlobalExceptionHandler {  
    @ExceptionHandler(Exception.class)// 当前方法捕获所有异常  
    public Result ex(Exception ex){  
        ex.printStackTrace();  
        return Result.error(" 对不起,操作失败,请联系管理员 ");  
    }  
}
```

# 10 Spring Transaction

默认情况下，只有出现 RuntimeException 才回滚异常。rollbackFor属性用于控制出现何种异常类型，回滚事务。

```java
@Transactional(rollbackFor = Exception.class)
@Override  
public void delete(Integer id) {  
	deptMapper.deteleById(id);// 删除部门
	int i = 1/0;// 模拟抛出异常
	empMapper.deleteByDeptId(id);// 删除部门下的员工信息
}
```

事务传播行为：指的就是当一个事务方法被另一个事务方法调用时，这个事务方法应该如何进行事务控制。

| 属性值           | 含义                                 |
|---------------|------------------------------------|
| REQUIRED      | 【默认值】需要事务，有则加入，无则创建新事务             |
| REQUIRES_NEW  | 需要新事务，无论有无，总是创建新事务                 |
| SUPPORTS      | 支持事务，有则加入，无则在无事务状态中运行              |
| NOT_SUPPORTED | 不支持事务，在无事务状态下运行,如果当前存在已有事务,则挂起当前事务 |
| MANDATORY     | 必须有事务，否则抛异常                        |
| NEVER         | 必须没事务，否则抛异常                        |

>当我们不希望事务之间互相影响的时候可以使用 `REQUIRES_NEW` ，比如下订单需要记录日志，不论订单保存与否都需要存储日志，如果使用 `REQUIRED` 来保存日志，那么在订单失败的时候不会存储日志。
