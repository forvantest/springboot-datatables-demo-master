com.larry.SddApplication

http://localhost:8080/

## 使用Spring Boot Jquery Datatables實現管理平台的表格


最近在公司做一個運營平台,增刪改查的那種,需要一個多功能的表格,網上看到Jquery的DataTables功能很豐富,查詢,排序,翻頁等等功能完善,
但是[DataTables官網](http://www.datatables.net/)上的例子,表格數據都沒有從服務端獲取,生產上使用還得自己摸索下.另外,公司使用
Spring boot這個框架,配置簡單,下面我們一起做一個整合的例子

### 新建Spring boot的應用
新建個項目springboot-datatables-demo,我使用的是intellij idea,創建個maven項目,在pom裡面引用包後,創建main方法即可主要代碼如下:
(詳細講解可以參考我的上一篇日誌[第一個Spring Boot應用](http://suqun.github.io/2016/02/17/%E7%AC%AC%E4%B8%80%E4%B8%AASpring-Boot%E5%BA%94%E7%94%A8/))

####pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.larry</groupId>
    <artifactId>springboot-datatables-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.2.5.RELEASE</version>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>c3p0</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.1.2</version>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.nekohtml</groupId>
            <artifactId>nekohtml</artifactId>
            <version>1.9.21</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.4</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>1.10.19</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>0.4.8</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.8</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-releases</id>
            <name>Spring Releases</name>
            <url>https://repo.spring.io/libs-release</url>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-releases</id>
            <name>Spring Releases</name>
            <url>https://repo.spring.io/libs-release</url>
        </pluginRepository>
    </pluginRepositories>
</project>
```

#### SddApplication.java

```java
package com.larry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SddApplication {
    public static void main(String[] args) {
        SpringApplication.run(SddApplication.class, args);
    }
}
```

ok,添加完上面兩個主要的內容maven引入相關包以後,就可以運行啦,在SDDApplication上右擊run一下.....額,是不是報錯了,貌似把數據庫給忘啦.
我們使用mysql數據庫,需要添加數據庫配置類config.java,添加配置文件application.properties

#### 建表
```bash
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `description` text,
  `hot` int(8) DEFAULT '0',
  `keywords` text,
  `url` varchar(255) NOT NULL,
  `disabled` int(2) NOT NULL DEFAULT '0',
  `name` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_delete` bit(1) DEFAULT NULL COMMENT '是否刪除，0：刪除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

####application.properties添加數據庫配置信息

```bash
spring.thymeleaf.mode=LEGACYHTML5
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.cache=false

spring.datasource.c3p0.driver-class-name=com.mchange.v2.c3p0.ComboPooledDataSource
spring.datasource.c3p0.jdbc-url=jdbc:mysql://127.0.0.1:3306/test?useUnicode=yes&characterEncoding=UTF-8
spring.datasource.c3p0.username=root
spring.datasource.c3p0.password=
spring.datasource.c3p0.min-evictable-idle-time-millis=30000
spring.jpa.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
由於我使用的是thymeleaf模板引擎,需要在resource下面添加文件夾templates,再添加個html, 隨便建個html叫index.html,在body裡面寫個 hello world！ 好了。

然後,我們run一下試試
```bash
2016-04-03 15:29:27.850  INFO 1847 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2016-04-03 15:29:27.852  INFO 1847 --- [           main] com.larry.SddApplication                 : Started SddApplication in 4.522 seconds ...
```
當我們發現這兩句話時,說明我們項目已經啟動成功了.不過輸入http://localhost:8080/瀏覽器仍然會報錯,我們配置下路由讓/路徑默認跳轉到index模板上.

###MvcConfig

```java
package com.larry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("").setViewName("index");
    }
}
```

重啟後,瀏覽器中輸入http://localhost:8080 哈哈，是不是看到熟悉的 hello world！ 啦。

### datatables
囉嗦了半天終於進入主題了。。。。我在github上發現有個哥們已經封裝了一套[spring data jpa + jquery datatables](https://github.com/darrachequesne/spring-data-jpa-datatables)的項目，直接pom裡面引用下，就ok拉，下面看看具體怎麼是用

#### Maven 依賴
```xml
        <dependency>
            <groupId>com.github.darrachequesne</groupId>
            <artifactId>spring-data-jpa-datatables</artifactId>
            <version>2.0</version>
        </dependency>
```

注意這哥們使用的hibernate包是4.3.10.Final，這個和spring-boot用的hibernate要一致，否則啟動的時候就會報錯，所以我選擇了1.2.5.RELEASE的版本的spring boot。

#### 啟用DataTablesRepository工廠
```java
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class SddApplication {
    public static void main(String[] args) {
        SpringApplication.run(SddApplication.class, args);
    }
}
```

#### 擴展DataTablesRepository接口
```java
public interface AppRepository extends DataTablesRepository<App, Long> {
}
```

#### 設置model屬性
```java
public class App {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @URL
    private String url;
    private String description;
    private String keywords;
    private boolean disabled;
    private int hot;

    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;
    private boolean isDelete;
}
```

#### 包含jquery.spring-friendly.js
It overrides jQuery data serialization to allow Spring MVC to correctly map input parameters (by changing column[0][data] to column[0].data in request payload)

#### index.html 
index.html
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!--<link rel="stylesheet" href="//cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css">-->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css">
    <link rel="stylesheet" href="/dataTables.bootstrap.css">
</head>
<body>
<div class="box">
    <div class="box-header with-border">
        <button type="button" class="btn btn-info" >增加12條數據</button>
    </div><!-- /.box-header -->
    <div class="box-body">
        <table id="appTable" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th></th>
                <th>操作</th>
                <th>名稱</th>
                <th>地址</th>
                <th>關鍵字</th>
                <th>描述</th>
                <th>熱度</th>
                <th>添加日期</th>
                <th>更新時間</th> <!--日期格式在application.properties添加：spring.jackson.date-format=yyyy-MM-dd HH:mm:ss-->
                <th>狀態</th>
            </tr>
            </thead>
        </table>
    </div><!-- /.box-body -->
</div>
<script src="/jQuery-2.1.4.min.js"></script>
<script src="/jquery.dataTables.js"></script>
<script src="/dataTables.bootstrap.min.js"></script>
<script src="/jquery.spring-friendly.js"></script>
<script>
    $().ready(function () {
        $('#appTable').DataTable({
            ajax: '/all',
            serverSide: true,
            order: [
                [8, 'desc']//更新時間倒序
            ],
            columns: [{
                data: null,
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return "<td><input type='checkbox' name='allocated' value='" + row.id + "'></td>";
                }
            }, {
                data: '',
                orderable: false,
                searchable: false,
                render: function (data, type, row) {
                    return "<td><button type='button' class='btn btn-primary btn-sm' onclick='editApp(" + row.id + ")'>編輯</button> &nbsp;" +
//                            "<button type='button' class='btn btn-info btn-sm' onclick='detail("+row.id+")'>詳情</button>" +
                            "&nbsp;<button type='button' class='btn btn-warning btn-sm' onclick='deleteSingle("+row.id+")'>刪除</button>" +
                            "</td>";
                }
            }, {
                data: 'name'
            }, {
                data: 'url',
                render: function (data, type, row) {
                    var shortUrl;
                    if(data.length<30){
                        shortUrl = data ;
                    } else {
                        shortUrl = data.substring(0,30)+"...";
                    }
                    return "<a href='" + data + "' target='_blank'>"+shortUrl+"</a>";
                }
            }, {
                data: 'keywords'
            }, {
                data: 'description'
            }, {
                data: 'hot'
            }, {
                data: 'createTime'
            }, {
                data: 'updateTime'
            }, {
                data: 'disabled',
                render: function (data, type, row) {
                    if (row.disabled) {
                        return "<input type='checkbox' name='state-checkbox' value='" + row.id + "'>";
                    } else {
                        return "<input type='checkbox' name='state-checkbox' value='" + row.id + "' checked>";
                    }
                }
            }]
//            initComplete: function () {
//                $("input[name='state-checkbox']").bootstrapSwitch();
//            },
//            drawCallback: function() {//Function that is called every time DataTables performs a draw.
//                $("input[name='state-checkbox']").bootstrapSwitch();
//            }


        });
    });
    
    $("button").on("click", function () {
        $.get("init")
                .success(function (data) {
                    window.location="/";
                });
    });
</script>
</body>
</html>
```

#### AppController
```java
    @ResponseBody
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public DataTablesOutput<App> messages(@Valid DataTablesInput input) {
        return appRepository.findAll(input);
    }
```

啟動後，添加數據後，顯示效果如下，已經可以分頁搜索排序了。
![表格](http://7xpk5e.com1.z0.glb.clouddn.com/datatables.png)
到這裡就可以看到，我們的基本目標已經完成了。不過仍然有個問題，現在的這個查詢用的是`findAll(input)`,如果我們要添加過濾條件進行查詢呢。其實[darrachequesne](https://github.com/darrachequesne)這個哥們已經封裝了。'DataTablesOutput<T> findAll(DataTablesInput var1, Specification<T> var2);'調用這個就可以，下面我們來看看具體怎麼用

### 使用過濾條件查詢
過濾查詢使用到了[Spring Data JPA - Specifications](https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/),自己要去熟悉了解下這個。

我們創建個Specification條件，然後調用'DataTablesOutput<T> findAll(DataTablesInput var1, Specification<T> var2);'
假設我們要查詢所有刪除狀態為false的記錄

#### AppSpec
```java
public class AppSpec {
    public static Specification<App> isNotDelete() throws Exception {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDelete"));
    }
}
```
注意：使用了lambda表達式，其實就是創建了內部類

#### 修改AppController
```java
@RequestMapping(value = "all", method = RequestMethod.GET)
    public DataTablesOutput<App> messages(@Valid DataTablesInput input) {
        try {
            return appRepository.findAll(input, AppSpec.isNotDelete());
        } catch (Exception e) {
            return null;
        }
    }
```

重啟，添加12條數據，設置6條記錄刪除狀態為true，看看效果
![過濾刪除記錄](http://7xpk5e.com1.z0.glb.clouddn.com/datatables-delete.png)
表格下面顯示：
Showing 1 to 6 of 6 entries (filtered from 12 total entries)
說明我們過濾成功了！

打完收工！
詳細代碼，歡迎從我的github上獲取：[spring-data-jpa-jquery-datatables]()

