package servlet.config;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    // TODO Auto-generated method stub
    return null;
  }
}
