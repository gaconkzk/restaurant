package tma.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import tma.bill.BillOrder;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Bill {
  Integer id;
  Integer totalPrice;
  Set<Order> orders;

  @Data
  @AllArgsConstructor
  public static
  class Order {
    String menu;
    Integer quantity;
    Date orderedDate;
    @JsonIgnore
    Integer total;
  }

  public static Bill fromModel(BillOrder billModel) {
    Bill b = new Bill();
    b.id = billModel.getId();

    b.orders = billModel.getBuildOrderMenus().stream()
        .map(buildOrderMenu -> {
          String menu = buildOrderMenu.getMenu().getName();
          Integer price = buildOrderMenu.getMenu().getPrice();
          Integer quantity = buildOrderMenu.getQuantity();
          Date orderedDate = buildOrderMenu.getOrderedDate();
          return new Order(menu, quantity, orderedDate, price*quantity);
        }).collect(Collectors.toSet());

    b.totalPrice = b.orders.stream()
        .map(o -> o.total).reduce((a, i) -> a += i).orElse(0);

    return b;
  }

//  public BillModel createModel() {
//    return new BillModel(
//        id,
//        billNo,
//        new MenuModel(menu_id, menu),
//        quantity,
//        this.orderedTime
//    );
//  }
}
