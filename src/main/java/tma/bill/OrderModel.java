package tma.bill;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Data
public class OrderModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
  private Set<OrderMenu> ordersMenus = new HashSet<>();

  public OrderModel(Integer orderNo, OrderMenu... boms) {
    this.id = orderNo;
    for (OrderMenu bom : boms) {
      bom.setOrder(this);
    }
    this.ordersMenus = Stream.of(boms).collect(Collectors.toSet());
  }
}
