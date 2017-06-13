package br.com.fiap.fuse.bean;

import java.sql.Driver;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class JpaBeanFactory {
	private static Logger log = LoggerFactory.getLogger(JpaBeanFactory.class);
	
	@Bean(name="fiapdbDS")
	public DataSource dataSource() {
		try {
			SimpleDriverDataSource dataSource = 
					new SimpleDriverDataSource(
							(Driver)Class.forName("org.postgresql.Driver").newInstance(),
							"jdbc:postgresql:fiap-db-1",
							"fiap",
							"fiap");
			return dataSource;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/*
	@Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPersistenceUnitName("fiap-postgres-1");
        entityManagerFactory.setJpaProperties(jpaProperties());
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactory.setJpaDialect(jpaDialect());
        return entityManagerFactory;
    }
	
	@Bean(name = "jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.ProgressDialect");
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }
	
	@Bean(name = "jpaDialect")
    public JpaDialect jpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        jpaTransactionManager.setDataSource(dataSource());
        jpaTransactionManager.setJpaDialect(jpaDialect());
        return jpaTransactionManager;
    }

    @Bean(name = "persistenceExceptionTranslation")
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.ProgressDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        return properties;
    }

	@Bean(name="jpa")
	@PersistenceUnit
	public JpaComponent createEntityManager(EntityManagerFactory emf) {
    	JpaComponent jpaComponent = new JpaComponent();
    	jpaComponent.setEntityManagerFactory(emf);
    	return jpaComponent;
	}	
	*/
}
