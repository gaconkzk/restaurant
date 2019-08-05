package tma.web;

import lombok.Data;
import tma.bill.OrderModel;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BillResponse {
  Integer orderNo;
  Integer totalPrice;
  Set<OrderResponse> orders;

  public static BillResponse fromModel(OrderModel order) {
    BillResponse b = new BillResponse();
    b.orderNo = order.getId();

    b.orders = order.getOrdersMenus().stream()
        .map(buildOrderMenu -> {
          String menu = buildOrderMenu.getMenu().getName();
          Integer price = buildOrderMenu.getMenu().getPrice();
          Integer quantity = buildOrderMenu.getQuantity();
          Date orderedDate = buildOrderMenu.getOrderedDate();
          return new OrderResponse(menu, quantity, orderedDate, price*quantity);
        }).collect(Collectors.toSet());

    b.totalPrice = b.orders.stream()
        .map(o -> o.total).reduce((a, i) -> a += i).orElse(0);

    return b;
  }
}
