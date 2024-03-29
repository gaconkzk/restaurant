package tma.components;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tma.RestaurantApplication;
import tma.bill.BillHandler;
import tma.menu.MenuHandler;
import tma.conf.RestaurantProperties;

import static io.javalin.apibuilder.ApiBuilder.*;

@Component
public class JavalinServer {
  private RestaurantProperties config;

  private MenuHandler menuHandler;

  private BillHandler billHandler;

  private Javalin app;

  @Autowired
  public JavalinServer(
    RestaurantProperties config, MenuHandler menuHandler, BillHandler billHandler
  ) {
    this.config = config;
    this.menuHandler = menuHandler;
    this.billHandler = billHandler;

    this.app = Javalin.create(conf -> {
      conf.server(() -> {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(config.getHost());
        connector.setPort(config.getPort());
        server.setConnectors(new Connector[] { connector });
        return server;
      });
      conf.requestLogger((ctx, ms) -> RestaurantApplication.LOG.debug("{}: {} in {}", ctx.method(), ctx.url(), ms));
      conf.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
    });
  }

  private OpenApiOptions getOpenApiOptions() {
    Info applicationInfo = new Info()
      .version("1.0")
      .description("Restaurant REST Api");
    return new OpenApiOptions(applicationInfo).path("/docs/json")
      .swagger(new SwaggerOptions("/docs").title("Restaurant REST Api"));
  }

  public void start() {
    this.app.start();
  }

  public void stop() {
    this.app.stop();
  }

  public void routes() {
    app.routes(() -> path("/api/v1", () -> {
      get("search/menus", this.menuHandler::search);
      get("/menus/check", this.menuHandler::check);

      get("/menus", this.menuHandler::getAll);
      post("/menus", this.menuHandler::create);
      patch("/menus/:menu-id", this.menuHandler::update);
      get("/menus/:menu-id", this.menuHandler::getOne);
      delete("/menus/:menu-id", this.menuHandler::delete);

      get("/bills", this.billHandler::getAll);
      post("/bills", this.billHandler::create);
      patch("/bills/:bill-id", this.billHandler::update);
      get("/bills/:bill-id", this.billHandler::getOne);
    }));
  }
}
