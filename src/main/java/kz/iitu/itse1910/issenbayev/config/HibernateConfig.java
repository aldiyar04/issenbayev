package kz.iitu.itse1910.issenbayev.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final ConfigurableEnvironment env;

    private boolean isDevProfile;
    private boolean isTestProfile;

    @Autowired
    public HibernateConfig(ConfigurableEnvironment env) {
        this.env = env;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        isDevProfile = activeProfiles.contains("dev");
        isTestProfile = activeProfiles.contains("test");

        if (isDevProfile && isTestProfile) {
            throw new IllegalStateException("Both dev and test profiles are set. Datasource configuration needs " +
                    "either one or the other.");
        } else if (!isDevProfile && !isTestProfile) {
            throw new IllegalStateException("No valid profile is set. Datasource configuration needs " +
                    "either dev or test profile to be set.");
        }

        //        print();
//        String[] profiles = env.getActiveProfiles();
//        for (String p: profiles) {
//            System.out.println(p);
//        }
//        print();
    }

    void print() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws IOException {
        return new HibernateTransactionManager(sessionFactory());
    }

    @Bean(name="entityManagerFactory")
    public SessionFactory sessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setHibernateProperties(hibernateProperties());
        sessionFactoryBean.setPackagesToScan("kz.iitu.itse1910.issenbayev.entity");
        sessionFactoryBean.afterPropertiesSet();
        return sessionFactoryBean.getObject();
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSource = DataSourceBuilder.create();
        if (isDevProfile) {
            setAsPostgreSQL(dataSource);
        } else if (isTestProfile) {
            setAsH2(dataSource);
        }
        return dataSource.build();
    }

    private void setAsPostgreSQL(DataSourceBuilder dataSource) {
        dataSource.driverClassName("org.postgresql.Driver");
        dataSource.url("jdbc:postgresql://localhost:5432/issenbayev");
        dataSource.username("postgres");
        dataSource.password("pass");
    }

    private void setAsH2(DataSourceBuilder dataSource) {
        dataSource.driverClassName("org.h2.Driver");
        dataSource.url("jdbc:h2:mem:issenbayev;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        dataSource.username("sa");
        dataSource.password("pass");
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();

        props.put("hibernate.dll-auto", "none");

        // Caching
        props.put("hibernate.cache.use_second_level_cache", true);
        props.put("hibernate.cache.use_query_cache", true);
        props.put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");

        // Fetching
        props.put("hibernate.jdbc.batch_size", 10);
        props.put("hibernate.jdbc.fetch_size", 50);

        // Show SQL
        props.put("hibernate.format_sql", true);
        props.put("hibernate.use_sql_comments", true);
        props.put("hibernate.show_sql", true);

        return props;
    }
}
