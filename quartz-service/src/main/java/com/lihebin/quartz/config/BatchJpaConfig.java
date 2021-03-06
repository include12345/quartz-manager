package com.lihebin.quartz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Created by lihebin on 2019/4/17.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryManage",
        transactionManagerRef = "transactionManagerManage",
        basePackages = {"com.lihebin.quartz.dao"}) //设置Repository所在位置
public class BatchJpaConfig {

    @Autowired
    private DataSource manageDataSource;

    @Autowired
    private JpaProperties jpaManageProperties;

    @Primary
    @Bean
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryManage(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryManage")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryManage(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(manageDataSource).packages("com.lihebin.quartz.model")//设置实体类所在位置
                .properties(jpaManageProperties.getProperties())
                .persistenceUnit("managePersistenceUnit")//持久化单元创建一个默认即可，多个便要分别命名
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManagerManage(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryManage(builder).getObject());
    }

}
