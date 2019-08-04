package tma;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tma.components.DBInitializer;
import tma.components.HttpServer;
import tma.conf.RestaurantProperties;

@SpringBootApplication
@EnableConfigurationProperties(RestaurantProperties.class)
public class RestaurantApplication implements CommandLineRunner {
  public static final Logger LOG = LoggerFactory.getLogger(RestaurantApplication.class);

  @Autowired
  HttpServer server;

  @Autowired
  DBInitializer db;

  public static void main(String[] args) {
    SpringApplication.run(RestaurantApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    server.start();
    server.routes();

    db.init();

    // Stop the server when the main thread shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
  }
}
