package tma.bill;

import lombok.Data;
import lombok.NoArgsConstructor;
import tma.RestaurantApplication;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Data
public class BillOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
  private Set<BillOrderMenu> buildOrderMenus = new HashSet<>();

  public BillOrder(Integer orderNo) {
    this.id = orderNo;
  }

  public BillOrder(Integer orderNo, BillOrderMenu ... boms) {
    this.id = orderNo;
    for (BillOrderMenu bom : boms) {
      bom.setBill(this);
    }
    this.buildOrderMenus = Stream.of(boms).collect(Collectors.toSet());
  }
}
