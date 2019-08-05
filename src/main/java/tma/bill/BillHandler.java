package tma.bill;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.plugin.openapi.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tma.RestaurantApplication;
import tma.menu.MenuModel;
import tma.menu.MenuService;
import tma.web.*;

import java.util.Set;
import java.util.stream.Collectors;

import static tma.Utils.makePageable;

@Component
public class BillHandler {
  private final BillService service;
  private final MenuService menuService;

  @Autowired
  public BillHandler(BillService service, MenuService menuService) {
    this.service = service;
    this.menuService = menuService;
  }

  @OpenApi(
    path = "/api/v1/bills",
    method = HttpMethod.GET,
    queryParams = {
      @OpenApiParam(name = "page", description = "Page number, start with 1"),
      @OpenApiParam(name = "size", description = "Number of items per page")
    },
    summary = "Get all bills. Paging (optional) enabled.",
    operationId = "getAllBillsV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Pager.PagerBill.class))
  )
  public void getAll(Context context) {
    Pageable pageable = makePageable(context);
    Page<OrderModel> bills = service.findAll(pageable);
    context.json(Pager.fromBill(bills)).status(200);
  }

  @OpenApi(
    path = "/api/v1/bills/:bill-id",
    method = HttpMethod.GET,
    summary = "Get bills by ordered number",
    operationId = "getOneBillV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = BillResponse.class))
  )
  public void getOne(Context context) {
    Integer id = Integer.valueOf(context.pathParam("bill-id"));
    OrderModel bill = this.service.find(id).orElseThrow(() -> new NotFoundResponse("Bill number " + id + " not existed."));
    context.json(BillResponse.fromModel(bill)).status(200);
  }

  @OpenApi(
    path = "/api/v1/bills",
    method = HttpMethod.POST,
    summary = "Create new bill order item",
    operationId = "createBillOrderV1",
    requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = OrderSetRequest.class)),
    responses = {@OpenApiResponse(status = "200", content = @OpenApiContent(from = BillResponse.class))}
  )
  public void create(Context context) {
    try {
      OrderSetRequest bill = context.bodyAsClass(OrderSetRequest.class);

      // double check menus in database
      OrderMenu[] orderMenus = getOrderMenus(bill.getOrders());

      OrderModel orderModel = new OrderModel(orderMenus);
      orderModel = service.create(orderModel);
      context.json(BillResponse.fromModel(orderModel)).status(200);
    } catch (BadRequestResponse ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  @OpenApi(
    path = "/api/v1/bills/:bill-id",
    method = HttpMethod.PATCH,
    summary = "Update bill quantity, menus",
    operationId = "updateBillOrderV1",
    requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = BillRequest.class)),
    responses = {@OpenApiResponse(status = "200", content = @OpenApiContent(from = BillResponse.class))}
  )
  public void update(Context context) {
    try {
      BillRequest bill = context.bodyAsClass(BillRequest.class);
      OrderMenu[] orderMenus = getOrderMenus(bill.getOrders());

      OrderModel orderModel = new OrderModel(bill.getOrderNo(), orderMenus);
      orderModel = service.update(orderModel);
      context.json(BillResponse.fromModel(orderModel)).status(200);
    } catch (BadRequestResponse ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw ex;
    }
  }

  @NotNull
  private OrderMenu[] getOrderMenus(Set<OrderRequest> orders) {
    // double check menus in database
    Set<String> menuNames = orders.stream().map(OrderRequest::getMenu).collect(Collectors.toSet());
    final Set<MenuModel> menuModels = menuService.getMenusByName(menuNames);
    if (menuModels.size() < menuNames.size()) {
      throw new BadRequestResponse("There are invalid menu in your request");
    }

    // make OrderMenu base on menu orders request
    return orders.stream()
      .map(order -> new OrderMenu(
        menuModels.stream().filter(mm -> mm.getName().equals(order.getMenu())).findFirst()
          .orElseThrow(null), // This should never happended, since we already check it above
        order.getQuantity(),
        order.getOrderedDate()
      )).toArray(OrderMenu[]::new);
  }
}
