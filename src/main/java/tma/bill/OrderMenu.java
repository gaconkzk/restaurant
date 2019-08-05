package tma.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import tma.menu.MenuModel;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(OrderMenuIdentity.class)
@Table(name = "order_menu")
@NoArgsConstructor
@Data
public class OrderMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;
  @Id
  @ManyToOne
  @JoinColumn
  MenuModel menu;
  @Id
  @ManyToOne
  @JoinColumn
  @JsonIgnore
  OrderModel order;

  private Integer quantity;
  private Date orderedDate;

  public OrderMenu(OrderModel order) {
    this.order = order;
  }

  public OrderMenu(MenuModel menu, Integer quantity, Date orderedDate) {
    this.menu = menu;
    this.quantity = quantity;
    this.orderedDate = orderedDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof OrderMenu)) return false;
    OrderMenu that = (OrderMenu) o;
    return id.equals(that.id) &&
      Objects.equals(menu.getName(), that.menu.getName()) &&
      Objects.equals(order.getId(), that.order.getId()) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(orderedDate, that.orderedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, menu.getName(), order.getId(), quantity, orderedDate);
  }
}
