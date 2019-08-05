package tma.menu;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tma.RestaurantApplication;
import tma.web.CheckMenusResponse;
import tma.web.Pager;

import java.util.Optional;

import static tma.Utils.makePageable;

@Component
public class MenuHandler implements CrudHandler {
  @Autowired
  private MenuService service;

  public MenuHandler(MenuService menuRepository) {
    this.service = menuRepository;
  }

  @OpenApi(
    path = "/api/v1/menus",
    method = HttpMethod.POST,
    summary = "Create new menu",
    operationId = "createMenuV1",
    requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = MenuModel.class)),
    responses = {@OpenApiResponse(status = "200", content = @OpenApiContent(from = MenuModel.class))}
  )
  @Override
  public void create(@NotNull Context context) {
    try {
      MenuModel menu = context.bodyAsClass(MenuModel.class);
      menu = service.create(menu);
      context.json(menu).status(200);
    } catch (BadRequestResponse ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
    }
  }

  @OpenApi(
    path = "/api/v1/menus/:menu-id",
    method = HttpMethod.DELETE,
    summary = "Delete menu",
    operationId = "deleteMenuV1",
    responses = {@OpenApiResponse(status = "200"),@OpenApiResponse(status = "404", description = "Menu not found")}
  )
  @Override
  public void delete(@NotNull Context context, @NotNull String s) {
    Integer id = Integer.valueOf(context.pathParam("menu-id"));
    this.service.delete(id);
    context.status(200);
  }

  @OpenApi(
    path = "/api/v1/menus/:menu-id",
    method = HttpMethod.GET,
    summary = "Get menu by id",
    operationId = "getOneMenuV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = MenuModel.class))
  )
  @Override
  public void getOne(@NotNull Context context, @NotNull String s) {
    Integer id = Integer.valueOf(context.pathParam("menu-id"));
    MenuModel menu = this.service.find(id);
    context.json(menu).status(200);
  }


  @OpenApi(
    path = "/api/v1/menus/:menu-id",
    method = HttpMethod.PATCH,
    summary = "Update menu name, price, description, and image",
    operationId = "updateMenuV1",
    requestBody = @OpenApiRequestBody(content = @OpenApiContent(from = MenuModel.class)),
    responses = {@OpenApiResponse(status = "200", content = @OpenApiContent(from = MenuModel.class))}
  )
  @Override
  public void update(@NotNull Context context, @NotNull String s) {
    MenuModel menu = context.bodyAsClass(MenuModel.class);
    this.service.update(menu);
    context.json(menu).status(200);
  }

  @OpenApi(
    path = "/api/v1/menus",
    method = HttpMethod.GET,
    queryParams = {
      @OpenApiParam(name = "page", description = "Page number, start with 1"),
      @OpenApiParam(name = "size", description = "Number of items per page")
    },
    summary = "Get all menus. when using with 'page' and 'size' parameters, " +
      "return 'PagerMenu' properties",
    operationId = "getAllMenusV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Pager.PagerMenu.class))
  )
  @Override
  public void getAll(@NotNull Context context) {
    Pageable pageable = makePageable(context);
    Page<MenuModel> menus = service.findAll(pageable);
    context.json(Pager.fromMenu(menus)).status(200);
  }

  @OpenApi(
    path = "/api/v1/search/menus",
    method = HttpMethod.GET,
    queryParams = {
      @OpenApiParam(name = "keyword", description = "All menus having name, description or tags contains this keyword will be returned."),
      @OpenApiParam(name = "page", description = "Page number, start with 1"),
      @OpenApiParam(name = "size", description = "Number of items per page")
    },
    summary = "Search all menu having name, description or tags containing 'keyword'.",
    operationId = "searchMenusV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Pager.PagerMenu.class))
  )
  public void search(@NotNull Context context) {
    Pageable pageable = makePageable(context);
    Page<MenuModel> menus = service.search(
      Optional.ofNullable(context.queryParam("keyword")).orElse(""),
      pageable);
    context.json(Pager.fromMenu(menus)).status(200);
  }


  @OpenApi(
    path = "/api/v1/menus/check",
    method = HttpMethod.GET,
    summary = "Get list menus items and their corresponded ordered quantities, prices, and total price of all .",
    operationId = "getAllCheckMenusV1",
    responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = CheckMenusResponse.class))
  )
  @Transactional // We need to enable transactional here for calculating order prices/quantity
  public void check(Context context) {
    // get all menus
    Page<MenuModel> menuModels = service.findAll(Pageable.unpaged());
    CheckMenusResponse response = new CheckMenusResponse(menuModels);
    context.json(response).status(200);
  }
}
