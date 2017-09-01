package com.codi.laolaiqiao.biz;

import java.sql.SQLException;
import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.codi.base.dataSource.CodiDataSource;
import com.codi.laolaiqiao.common.util.DebugableHashMap;
import com.codi.laolaiqiao.common.validation.ValidatorFactory;

@EnableJpaRepositories(basePackages = { "com.codi.laolaiqiao.biz.dao" })
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackages = { "com.codi.laolaiqiao.biz.service.impl", "com.codi.laolaiqiao.biz.component" })
public class BizSpringConfiguration {

	@Bean
	public DataSource dataSource() {
		try {
			return new CodiDataSource("laolaiqiao");
		} catch (SQLException e) {
			throw new Error("Init datasource fail !!!", e);
		}
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.MYSQL);
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
		vendorAdapter.setGenerateDdl(false);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.codi.laolaiqiao.api.domain", "com.codi.laolaiqiao.api.domain.sys");
		factory.setDataSource(dataSource());

		// init jpa properties
		HashMap<String, String> jpaPropertiesMap = new DebugableHashMap<>();
		jpaPropertiesMap.put("hibernate.ejb.naming_strategy", "com.codi.laolaiqiao.common.jdbc.CustomNamingStrategy");
		jpaPropertiesMap.put("javax.persistence.validation.mode", "none");
		factory.setJpaPropertyMap(jpaPropertiesMap);
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		txManager.setDataSource(dataSource());
		return txManager;
	}

	@Bean
	public TransactionTemplate transactionTemplate() {
		return new TransactionTemplate(transactionManager());
	}

	@Bean
	public Validator validator() {
		return ValidatorFactory.buildValidator();
	}
}
