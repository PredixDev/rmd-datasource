package com.ge.predix.solsvc.experience.datasource.boot;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * Datagrid Microservice
 * 
 * @author 212421693
 *
 */
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		JpaRepositoriesAutoConfiguration.class,
		PersistenceExceptionTranslationAutoConfiguration.class })
@ComponentScan(basePackages = ("com.ge.predix.solsvc"))
@PropertySource("classpath:application.properties")
@Controller
public class DatasourceApplication extends PredixSpringBootInitializer {

	private static Logger log = LoggerFactory.getLogger(DatasourceApplication.class);
	
	
	@SuppressWarnings("javadoc")
    @Value("${spring.profiles.active:local}")
	String profile ;
	
	@SuppressWarnings("javadoc")
    @Value("${java.docs.url:null}")
    String docsUrl ;

	
	/**
     * @return the profile
     */
    public String getProfile()
    {
        return this.profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(String profile)
    {
        this.profile = profile;
    }

    /**
	 * @param args
	 *            -
	 */
    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DatasourceApplication.class);
		@SuppressWarnings("resource")
        ConfigurableApplicationContext ctx = app.run(args);

		log.info("Let's inspect the properties provided by Spring Boot:"); //$NON-NLS-1$
		MutablePropertySources propertySources = ((StandardServletEnvironment) ctx
				.getEnvironment()).getPropertySources();
		Iterator<org.springframework.core.env.PropertySource<?>> iterator = propertySources
				.iterator();
		while (iterator.hasNext()) {
			Object propertySourceObject = iterator.next();
			if (propertySourceObject instanceof org.springframework.core.env.PropertySource) {
				org.springframework.core.env.PropertySource<?> propertySource = (org.springframework.core.env.PropertySource<?>) propertySourceObject;
				log.info("propertySource=" + propertySource.getName() //$NON-NLS-1$
						+ " values=" + propertySource.getSource() + "class=" //$NON-NLS-1$ //$NON-NLS-2$
						+ propertySource.getClass());
			}
		}

		log.info("Let's inspect the profiles provided by Spring Boot:"); //$NON-NLS-1$
		String profiles[] = ctx.getEnvironment().getActiveProfiles();
		for (int i = 0; i < profiles.length; i++)
			log.info("profile=" + profiles[i]); //$NON-NLS-1$
	}
    
    /**
     * @param request -
     * @param name -
     * @param model -
     * @return -
     */
    @RequestMapping("/")
    public String greetings(HttpServletRequest request ,@RequestParam(value = "name", required = false, defaultValue = "Predix") String name,
            Model model)
    {   StringBuffer requesturi = request.getRequestURL();
        String applicationURl = requesturi.toString().replaceAll("http", "https");//$NON-NLS-1$ //$NON-NLS-2$ 
        if("local".equalsIgnoreCase(this.profile)) { //$NON-NLS-1$
            applicationURl = requesturi.toString(); // localhost support for http
        }
        model.addAttribute("api",applicationURl.toString()+"api");//$NON-NLS-1$ //$NON-NLS-2$ 
        model.addAttribute("health",applicationURl.toString()+"health");//$NON-NLS-1$ //$NON-NLS-2$ 
        model.addAttribute("docs",this.docsUrl);//$NON-NLS-1$ 
        
        return "index"; //$NON-NLS-1$
    }
    
    /**
     * @param request -
     * @param response -
     * @throws IOException -
     */
    @RequestMapping("/api")
    public @ResponseBody void api(HttpServletRequest request ,HttpServletResponse response ) throws IOException
    {   String applicationURl = getApplicationUrl(request);
        response.sendRedirect(applicationURl.replace("/api", "/swagger-api/index.html?url=/services/swagger.json")); //$NON-NLS-1$//$NON-NLS-2$

    }
    
  
    /**
     * @param request -
     * @param response -
     * @throws IOException -
     */
    @RequestMapping("/health")
    public @ResponseBody void health(HttpServletRequest request ,HttpServletResponse response ) throws IOException
    {  
        
        String applicationURl = getApplicationUrl(request);
        response.sendRedirect(applicationURl.replace("/health", "/services/experience/health")); //$NON-NLS-1$ //$NON-NLS-2$

    }
   
    /**
     * 
     * @param request
     * @return - Application URL
     */
    private String getApplicationUrl (final HttpServletRequest request){
   
        String applicationURl = request.getRequestURL().toString().replaceAll("http", "https");//$NON-NLS-1$ //$NON-NLS-2$ 
        if("local".equalsIgnoreCase(this.profile)) { //$NON-NLS-1$
            applicationURl = request.getRequestURL().toString(); // localhost support for http
        }
        return applicationURl;
    }
    
}
