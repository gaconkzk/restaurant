package tma.bill;

import lombok.*;
import tma.menu.MenuModel;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class OrderMenuIdentity implements Serializable {
  private Integer id;
  private MenuModel menu;
  private OrderModel order;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderMenuIdentity)) return false;
    OrderMenuIdentity that = (OrderMenuIdentity) o;
    return id.equals(that.id) &&
      Objects.equals(menu.getName(), that.menu.getName()) &&
      Objects.equals(order.getId(), that.order.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, menu.getName(), order.getId());
  }
}
