package com.zj.shop.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.zj.shop.service", "com.zj.shop.utils", "com.zj.shop.mapper", "com.zj.shop.inter.pss"})
public class Configurator {

    private Logger logger = Logger.getLogger(Configurator.class);

    private
    @Value("${mysql.database.url}")
    String database_url;
    private
    @Value("${mysql.database.user}")
    String database_username;
    private
    @Value("${mysql.database.password}")
    String database_password;
    @Value("${order.pss.connect.time.out}")
    private int        timeOut;
    @Value("${order.pss.read.time.out}")
    private int        readTimeOut;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private SqlSessionFactory              sqlSessionFactory;
    @Autowired
    private SimpleClientHttpRequestFactory simpleClientHttpRequestFactory;

    @Bean
    public DataSource dataSource() {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(database_url);
            dataSource.setUsername(database_username);
            dataSource.setPassword(database_password);
            dataSource.setInitialSize(1);
            dataSource.setMinIdle(1);
            dataSource.setMaxActive(20);
            dataSource.setMaxWait(60000);
            dataSource.setTimeBetweenEvictionRunsMillis(60000);
            dataSource.setMinEvictableIdleTimeMillis(300000);
            dataSource.setValidationQuery("select 'x' from dual");
            dataSource.setTestWhileIdle(true);
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(false);
            dataSource.setPoolPreparedStatements(false);
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            dataSource.setFilters("stat");
            return dataSource;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setConfigLocation(new ClassPathResource("mybatis/Config.xml"));
            return factoryBean.getObject();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Bean
    public MapperHelper mapperHelper() {
        MapperHelper mapperHelper = new MapperHelper();
        Properties properties = new Properties();
        properties.setProperty("ORDER", "AFTER");
        mapperHelper.setProperties(properties);
        //        tk.mybatis.mapper.entity.Config config = new tk.mybatis.mapper.entity.Config();
        //        mapperHelper.setConfig(config);
        mapperHelper.registerMapper(Mapper.class);
        mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
        return mapperHelper;
    }

    @Bean
    public DataSourceTransactionManager dsTransactionManager() {
        DataSourceTransactionManager dsTransactionManager = new DataSourceTransactionManager();
        dsTransactionManager.setDataSource(dataSource);
        return dsTransactionManager;
    }

    private
    @Value("${email.host}")
    String host;
    private
    @Value("${email.username}")
    String username;
    private
    @Value("${email.password}")
    String password;
    private
    @Value("${email.port}")
    int    port;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setPort(port);
        mailSender.setDefaultEncoding("UTF-8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailProperties.setProperty("mail.mime.charset", "UTF-8");
        javaMailProperties.setProperty("mail.transport.protocol", "smtp");
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    private
    @Value("${mailtplt.from}")
    String from;

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject("sfs");
        return simpleMailMessage;
    }

    /**
     * FreeMarker模板配置
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/template/");
        freeMarkerConfigurer.setDefaultEncoding("UTF-8");
        return freeMarkerConfigurer;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(30);
        return taskExecutor;
    }

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(timeOut);
        simpleClientHttpRequestFactory.setReadTimeout(readTimeOut);
        return simpleClientHttpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        return restTemplate;
    }

//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() {
//        SimpleThreadPoolTaskExecutor threadPoolTaskExecutor = new SimpleThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setThreadCount(20);
//
//        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
//        schedulerFactoryBean.setConfigLocation(new ClassPathResource("platform.properties"));
//        schedulerFactoryBean.setDataSource(dataSource);
//        schedulerFactoryBean.setTransactionManager(transactionManager);
//        // schedulerFactoryBean.setTaskExecutor(threadPoolTaskExecutor);
//        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
//        schedulerFactoryBean.setAutoStartup(true);
//        return schedulerFactoryBean;
//    }

    @Value("${redis.host.name}")
    private String  hostName;
    @Value("${redis.host.port}")
    private Integer hostPort;
    @Value("${redis.max.idle}")
    private Integer maxIdle;
    @Value("${redis.max.wait}")
    private Integer maxWait;

    @Bean
    public ShardedJedisPool shardedJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(true);
        List<JedisShardInfo> jedisShardInfos = new ArrayList<>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo(hostName, hostPort);
        jedisShardInfos.add(jedisShardInfo);
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, jedisShardInfos);
        return shardedJedisPool;
    }
}
