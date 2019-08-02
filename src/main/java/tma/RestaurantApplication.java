package tma;

import io.javalin.Javalin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.Yaml;
import tma.bill.BillHandler;
import tma.bill.BillRepository;
import tma.bill.menu.MenuHandler;
import tma.bill.menu.MenuRepository;
import tma.conf.JavalinConfiguration;
import tma.utils.DatabaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.javalin.apibuilder.ApiBuilder.*;

@SpringBootApplication
public class RestaurantApplication implements CommandLineRunner {
  public static final Logger LOG = LoggerFactory.getLogger(RestaurantApplication.class);
  private static final JavalinConfiguration CONFIG = loadConfiguration();

  @Autowired
  private MenuRepository menuRepo;

  @Autowired
  private BillRepository billRepo;

  @Autowired
  private MenuHandler menuHandler;

  @Autowired
  private BillHandler billHandler;

  public static void main(String[] args) {
    SpringApplication.run(RestaurantApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    LOG.info("Starting Restaurant REST Service\n" +
            "     Version:    {}\n" +
            "     Host:       {}\n" +
            "     Port:       {}\n" +
            "     HTTPs:      {}\n" +
            "     API Version:{}\n",
        CONFIG.getVersion(),
        CONFIG.getHost(),
        CONFIG.getPort(),
        CONFIG.getHttps(),
        CONFIG.getApiVersion());

    Javalin app = Javalin.create(conf -> {
      conf.server(() -> {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(CONFIG.getHost());
        connector.setPort(CONFIG.getPort());
        server.setConnectors(new Connector[]{connector});
        return server;
      });
      conf.requestLogger(
          (ctx, ms) -> LOG.debug(
              "{}: {} in {}",
              ctx.method(),
              ctx.url(),
              ms
          )
      );
    }).start();

    routeInitialize(app, CONFIG.getApiVersion());

    DatabaseUtils.initDB(billRepo, menuRepo);

    // Stop the server when the main thread shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
  }

  private void routeInitialize(Javalin app, String apiVersion) {
    app.get("/", ctx -> ctx.result("Welcome to restaurant"));
    app.routes(() -> path("/api/v" + apiVersion, () -> {
      get("search/menus", this.menuHandler::search);
      crud("menus/:menu-id", this.menuHandler);
      get("bills", this.billHandler::getAll);
      post("bills", this.billHandler::create);
      patch("bills/:bill-id", this.billHandler::update);
      get("bills/:bill-id", this.billHandler::getOne);
    }));
  }

  private static JavalinConfiguration loadConfiguration() {
    // Loading the default configuration
    Yaml yaml = new Yaml();
    JavalinConfiguration config;
    try (InputStream in = Files.newInputStream(Paths.get("./conf/restaurant_configuration.yml"))) {
      config = yaml.loadAs(in, JavalinConfiguration.class);
    } catch (IOException e) {
      config = new JavalinConfiguration();
    }
    return config;
  }
}
