package servlet.config;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Stage;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import jakarta.servlet.ServletContextListener;

/** 
 * Bind/Injection config
 * 
 * @author halflite
 *
 */
public class AppModule extends AbstractModule {

  @Override
  protected void configure() {    
    // bind microprofile-config.properties
    final Map<String, String> props = new HashMap<>();
    Sets.newLinkedHashSet(ConfigProvider.getConfig().getConfigSources())
        .stream()
        .map(ConfigSource::getProperties)
        .forEach(props::putAll);
    Names.bindProperties(this.binder(), props);
  }
  
  @Provides
  @Singleton
  public Server providesServer(
      @Named("appContextListener") ServletContextListener appContextListener,
      @Named("server.host") String  host,
      @Named("server.port") Integer port) {
    Tomcat tomcat = new Tomcat();
    tomcat.setHostname(host);
    tomcat.setPort(port);
 
//    ServletContextHandler contextHandler =
//        new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
//    contextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
//    contextHandler.addEventListener(appContextListener);

    return tomcat;
  }

  @Provides
  @Singleton
  @Named("appContextListener")
  public ServletContextListener providesAppContextListener(@Named("env.stage") String stage) {
    return new GuiceServletContextListener() {
      @Override
      protected Injector getInjector() {
        return Guice.createInjector(Stage.valueOf(stage), new AppServletModule());
      }
    };
  }}
