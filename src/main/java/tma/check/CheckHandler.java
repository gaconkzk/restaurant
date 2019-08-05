package tma.check;

import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tma.bill.BillService;
import tma.menu.MenuModel;
import tma.menu.MenuService;
import tma.web.CheckMenusResponse;

@Component
public class CheckHandler {
  private BillService billService;
  private MenuService menuService;

  @Autowired
  public CheckHandler(BillService billService, MenuService menuService) {
    this.billService = billService;
    this.menuService = menuService;
  }

  @OpenApi(
    path = "/api/v1/checks",
    method = HttpMethod.GET,
    summary = "Get list menus items and their corresponded ordered quantities, prices, and total price of all .",
    operationId = "getAllCheckMenusV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = CheckMenusResponse.class))
  )
  @Transactional // We need to enable transactional here for calculating order prices/quantity
  public void get(Context context) {
    // get all menus
    Page<MenuModel> menuModels = menuService.findAll(Pageable.unpaged());
    CheckMenusResponse response = new CheckMenusResponse(menuModels);
    context.json(response).status(200);
  }
}
