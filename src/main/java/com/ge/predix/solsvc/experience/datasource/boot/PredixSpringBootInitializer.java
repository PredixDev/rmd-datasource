package com.ge.predix.solsvc.experience.datasource.boot;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * @author tturner
 *
 */
@ImportResource(
{
    "classpath*:META-INF/spring/datasource-scan-context.xml",
    "classpath*:META-INF/spring/datasource-cxf-context.xml",
    "classpath*:META-INF/spring/datasource-app-context.xml"
})
public class PredixSpringBootInitializer
{

    /**
     * Ensure the Tomcat container comes up, not the Jetty one.
     * @return - the factory
     */
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory()
    {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() ;
         
        return tomcat;
    }

    /**
     * Spin up a CXFServlet and tie to the mentioned URL
     * 
     * @return -
     */
    @SuppressWarnings("nls")
    @Bean
    public ServletRegistrationBean servletRegistrationBean()
    {
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");
    }

}
