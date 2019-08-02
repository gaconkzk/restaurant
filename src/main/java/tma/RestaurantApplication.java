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
import tma.bill.BillModel;
import tma.bill.BillRepository;
import tma.bill.menu.MenuHandler;
import tma.bill.menu.MenuModel;
import tma.bill.menu.MenuRepository;
import tma.conf.JavalinConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    databaseInitialize();

    // Stop the server when the main thread shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
  }

  private void databaseInitialize() {
    List<MenuModel> menus = Arrays.asList(
        new MenuModel(
            1,
            "Hawaiian Pizza",
            "All-time favourite toppings, Hawaiian pizza in Tropical Hawaii style.",
            "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu1.jpg",
            300,
            "Italian,Ham,Pineapple"
        ),
        new MenuModel(
            2,
            "Chicken Tom Yum Pizza",
            "Best marinated chicken with pineapple and mushroom on Spicy Lemon sauce. Enjoy our tasty Thai style pizza.",
            "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu2.jpg",
            350,
            "Italian,Thai,Chicken,Mushroom,Hot"
        ),
        new MenuModel(
            3,
            "Xiaolongbao",
            "Chinese steamed bun",
            "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu3.jpg",
            200,
            "Chinese,Pork,Recommended"
        ),
        new MenuModel(
            4,
            "Kimchi",
            "Traditional side dish made from salted and fermented vegetables",
            "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu4.jpg",
            50,
            "Korean,Radish,Cabbage"
        ),
        new MenuModel(
            5,
            "Oolong tea",
            "Partially fermented tea grown in the Alishan area",
            "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu5.jpg",
            30,
            "Hot,Non-alcohol"
        ),
        new MenuModel(
            6,
            "Beer",
            "Fantastic flavors and authentic regional appeal beer",
            "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu6.jpg",
            60,
            "Alcohol"
        )
    );
    // Menu initialize
    this.menuRepo.saveAll(menus);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
    try {
      this.billRepo.saveAll(
          Arrays.asList(
              new BillModel(
                  1,
                  menus.get(0),
                  1,
                  sdf.parse("1/1/2017 10:00:00 AM")
              ),
              new BillModel(
                  1,
                  menus.get(3),
                  2,
                  sdf.parse("1/1/2017 10:00:00 AM")
              ),
              new BillModel(
                  1,
                  menus.get(3),
                  1,
                  sdf.parse("1/1/2017 11:00:00 AM")
              ),
              new BillModel(
                  2,
                  menus.get(2),
                  1,
                  sdf.parse("1/1/2017 12:00:00 PM")
              ),
              new BillModel(
                  2,
                  menus.get(5),
                  1,
                  sdf.parse("1/1/2017 12:00:00 PM")
              ),
              new BillModel(
                  3,
                  menus.get(4),
                  1,
                  sdf.parse("1/1/2017 3:00:00 PM")
              ),
              new BillModel(
                  3,
                  menus.get(5),
                  3,
                  sdf.parse("1/1/2017 3:00:00 PM")
              )
          )
      );
    } catch (Exception ex) {
      LOG.error("Error init database.", ex);
    }
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
