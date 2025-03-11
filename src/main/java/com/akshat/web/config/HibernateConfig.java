package com.akshat.web.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

	private Environment env;

	public Environment getEnv() {
		return env;
	}

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

    @Bean
    DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		return dataSource;
	}
    
    @Bean(name = "entityManagerFactory")
    LocalSessionFactoryBean entityManagerFactory() {
    	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    	sessionFactory.setDataSource(dataSource());
    	sessionFactory.setPackagesToScan(env.getProperty("spring.jpa.properties.hibernate.packagesToScan"));
    	sessionFactory.setHibernateProperties(hibernateProperties());
    	return sessionFactory;
    }
    
    private Properties hibernateProperties() {
    	Properties properties = new Properties();
    	properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
    	properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
    	properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    	properties.put("hibernate.current_session_context_class", "thread");
    	return properties;
    }
    
    @Bean
    HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
    	return new HibernateTransactionManager(sessionFactory);
    }
	
}
