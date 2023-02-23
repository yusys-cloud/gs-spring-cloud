# XD 模式

XA 模式性能较差一般不使用。编程模型上，XA 模式与 AT 模式保持完全一致，仅需修改数据源:

```
 @Bean("dataSource")
    public DataSource dataSource(DruidDataSource druidDataSource) {
       

        // DataSourceProxyXA for XA mode
        return new DataSourceProxyXA(druidDataSource);
    }
    
```