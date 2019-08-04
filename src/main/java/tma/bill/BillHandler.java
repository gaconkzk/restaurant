package tma.bill;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tma.RestaurantApplication;
import tma.bill.menu.MenuItem;
import tma.bill.menu.MenuService;
import tma.web.Bill;
import tma.web.Pager;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class BillHandler {
  private final BillService service;
  private final MenuService menuService;

  @Autowired
  public BillHandler(BillService service, MenuService menuService) {
    this.service = service;
    this.menuService = menuService;
  }
  public void getAll(Context context) {
    String pageStr = context.queryParam("page");
    String sizeStr = context.queryParam("size");
    if (pageStr!=null && sizeStr != null) {
      Pageable pageable = PageRequest.of(Integer.parseInt(pageStr) -1, Integer.parseInt(sizeStr));
      Page<BillOrder> bills = service.findAll(pageable);
      context.json(Pager.fromBill(bills)).status(200);
    } else {
      Iterable<BillOrder> bills = service.findAll();

      context.json(
          StreamSupport.stream(bills.spliterator(), false)
          .map(Bill::fromModel)
          .collect(Collectors.toList())
      ).status(200);
    }
  }

  interface Order2BillMenu extends Function<Bill.Order, BillOrderMenu> {
    BillOrderMenu apply(Bill.Order order) throws NotFoundResponse;
  }

  public void create(Context context) {
    try {
      Bill bill = context.bodyAsClass(Bill.class);

      Order2BillMenu order2BillMenu = (order) -> {
        MenuItem menu = Optional.ofNullable(menuService.findByName(order.getMenu()))
          .orElseThrow(() -> new NotFoundResponse("Menu " + order.getMenu() + " not existed."));
        return new BillOrderMenu(menu, order.getQuantity(), order.getOrderedDate());
      };

      BillOrderMenu[] billOrderMenus = bill.getOrders().stream().map(order2BillMenu).toArray(BillOrderMenu[]::new);

      BillOrder billorder = new BillOrder(bill.getId(), billOrderMenus);
      billorder = service.create(billorder);
      context.json(Bill.fromModel(billorder)).status(200);
    } catch (BadRequestResponse ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
    }
  }

  public void update(Context context) {

  }

  public void getOne(Context context) {
    Integer id = Integer.valueOf(context.pathParam("bill-id"));
    BillOrder bill = this.service.find(id).orElseThrow(() -> new NotFoundResponse("Bill number " + id + " not existed."));
    context.json(Bill.fromModel(bill)).status(200);
  }
}
