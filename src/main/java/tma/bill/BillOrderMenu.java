package tma.bill;

import lombok.Data;
import lombok.NoArgsConstructor;
import tma.bill.menu.MenuItem;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(BillOrderMenuIdentity.class)
@Table(name = "bill_menu")
@NoArgsConstructor
@Data
public class BillOrderMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;
  @Id
  @ManyToOne
  @JoinColumn
  MenuItem menu;
  @Id
  @ManyToOne
  @JoinColumn
  BillOrder bill;

  private Integer quantity;
  private Date orderedDate;

  public BillOrderMenu(BillOrder bill) {
    this.bill = bill;
  }

  public BillOrderMenu(MenuItem menu, Integer quantity, Date orderedDate) {
    this.menu = menu;
    this.quantity = quantity;
    this.orderedDate = orderedDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BillOrderMenu)) return false;
    BillOrderMenu that = (BillOrderMenu) o;
    return id.equals(that.id) &&
      Objects.equals(menu.getName(), that.menu.getName()) &&
      Objects.equals(bill.getId(), that.bill.getId()) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(orderedDate, that.orderedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, menu.getName(), bill.getId(), quantity, orderedDate);
  }
}
