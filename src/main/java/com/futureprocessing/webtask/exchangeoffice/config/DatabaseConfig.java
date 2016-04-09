package com.futureprocessing.webtask.exchangeoffice.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.futureprocessing.webtask.exchangeoffice.dao.UsersDao;
import com.futureprocessing.webtask.exchangeoffice.dao.UsersDaoImpl;
import com.futureprocessing.webtask.exchangeoffice.dao.WalletsDao;
import com.futureprocessing.webtask.exchangeoffice.dao.WalletsDaoImpl;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:db/hibernate.properties")
public class DatabaseConfig {

    @Autowired
    private Environment environment;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.addProperties(getHibernateProperties());
        sessionBuilder.scanPackages("com.futureprocessing.webtask.exchangeoffice.model");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;
    }

    @Autowired
    @Bean(name = "usersDao")
    public UsersDao getUsersDao(SessionFactory sessionFactory) {
        return new UsersDaoImpl(sessionFactory);
    }

    @Autowired
    @Bean(name = "walletsDao")
    public WalletsDao getWalletsDao(SessionFactory sessionFactory) {
        return new WalletsDaoImpl(sessionFactory);
    }

}
