package com.ge.predix.solsvc.experience.datasource.boot;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * Datagrid Microservice
 * 
 * @author 212421693 Swapna Vad
 *
 */
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		PersistenceExceptionTranslationAutoConfiguration.class })
@ComponentScan(basePackages = ("com.ge.predix.solsvc"))
@PropertySource("classpath:application.properties")
public class DatasourceApplication extends PredixSpringBootInitializer {

	private static Logger log = LoggerFactory.getLogger(DatasourceApplication.class);

	
	/**
	 * @param args
	 *            -
	 */
	@SuppressWarnings("nls")
    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DatasourceApplication.class);
		ConfigurableApplicationContext ctx = app.run(args);

		log.info("Let's inspect the properties provided by Spring Boot:");
		MutablePropertySources propertySources = ((StandardServletEnvironment) ctx
				.getEnvironment()).getPropertySources();
		Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources
				.iterator();
		while (iterator.hasNext()) {
			Object propertySourceObject = iterator.next();
			if (propertySourceObject instanceof org.springframework.core.env.PropertySource) {
				org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
				log.info("propertySource=" + propertySource.getName()
						+ " values=" + propertySource.getSource() + "class="
						+ propertySource.getClass());
			}
		}

		log.info("Let's inspect the profiles provided by Spring Boot:");
		String profiles[] = ctx.getEnvironment().getActiveProfiles();
		for (int i = 0; i < profiles.length; i++)
			log.info("profile=" + profiles[i]);
	}

}
