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
import tma.bill.menu.MenuModel;
import tma.web.Bill;
import tma.web.Pager;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.awt.SystemColor.menu;

@Component
public class BillHandler {
  private final BillService service;

  @Autowired
  public BillHandler(BillService service) {
    this.service = service;
  }
  public void getAll(Context context) {
    String pageStr = context.queryParam("page");
    String sizeStr = context.queryParam("size");
    if (pageStr!=null && sizeStr != null) {
      Pageable pageable = PageRequest.of(Integer.parseInt(pageStr) -1, Integer.parseInt(sizeStr));
      Page<BillModel> bills = service.findAll(pageable);
      context.json(Pager.fromBill(bills)).status(200);
    } else {
      Iterable<BillModel> bills = service.findAll();

      context.json(
          StreamSupport.stream(bills.spliterator(), false)
          .map(Bill::fromModel)
          .collect(Collectors.toList())
      ).status(200);
    }
  }

  public void create(Context context) {
    try {
      Bill bill = context.bodyAsClass(Bill.class);
      BillModel billModel = service.create(bill.createModel());
      context.json(Bill.fromModel(billModel)).status(200);
    } catch (BadRequestResponse ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
    }
  }

  public void update(Context context) {

  }

  public void getOne(Context context) {
    Integer id = Integer.valueOf(context.pathParam("bill-id"));
    BillModel bill = this.service.find(id).orElseThrow(() -> new NotFoundResponse("Bill number " + id + " not existed."));
    context.json(Bill.fromModel(bill)).status(200);
  }
}
