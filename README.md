# my-mybatis-generator

修改Mybatis-generator生成类的注释

添加`MyGenerator`使得只需要配置一张表就可生成数据库所有表数据

执行：运行`org.mybatis.Application.main()`

生成的domain格式（省略了get、set等方法）：
```java
/**
 * DBTable: s_user
 *
 * Generated by MyBatis Generator. on 2018-12-19 23:44:26
 * @author mengtian
 */
public class SUser implements Serializable {
    /**
     * id
     *
     * 逻辑主键
     */
    private String id;

    /**
     * login_name
     *
     * 登录账号
     */
    private String loginName;
```
