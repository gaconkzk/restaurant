package tma.bill.menu;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tma.RestaurantApplication;
import tma.web.Pager;

@Component
public class MenuHandler implements CrudHandler {
  @Autowired
  private MenuService service;

  public MenuHandler(MenuService menuRepository) {
    this.service = menuRepository;
  }

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

  @Override
  public void delete(@NotNull Context context, @NotNull String s) {
    Integer id = Integer.valueOf(context.pathParam("menu-id"));
    this.service.delete(id);
    context.status(200);
  }

  @Override
  public void getOne(@NotNull Context context, @NotNull String s) {
    Integer id = Integer.valueOf(context.pathParam("menu-id"));
    MenuModel menu = this.service.find(id);
    context.json(menu).status(200);
  }

  @Override
  public void update(@NotNull Context context, @NotNull String s) {
    MenuModel menu = context.bodyAsClass(MenuModel.class);
    this.service.update(menu);
    context.json(menu).status(200);
  }

  @Override
  public void getAll(@NotNull Context context) {
    String pageStr = context.queryParam("page");
    String sizeStr = context.queryParam("size");
    if (pageStr!=null && sizeStr != null) {
      Pageable pageable = PageRequest.of(Integer.parseInt(pageStr) -1, Integer.parseInt(sizeStr));
      Page<MenuModel> menus = service.findAll(pageable);
      context.json(Pager.fromMenu(menus)).status(200);
    } else {
      Iterable<MenuModel> menus = service.findAll();
      context.json(menus).status(200);
    }
  }

  public void search(@NotNull Context context) {
    String pageStr = context.queryParam("page");
    String sizeStr = context.queryParam("size");
    if (pageStr!=null && sizeStr != null) {
      Pageable pageable = PageRequest.of(Integer.parseInt(pageStr) -1, Integer.parseInt(sizeStr));
      Page<MenuModel> menus = service.search(context.queryParam("keyword"), pageable);
      context.json(Pager.fromMenu(menus)).status(200);
    } else {
      Iterable<MenuModel> menus = service.search(context.queryParam("keyword"));
      context.json(menus).status(200);
    }
  }
}
