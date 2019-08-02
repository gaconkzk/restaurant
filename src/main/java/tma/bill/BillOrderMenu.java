package tma.bill;

import lombok.Data;
import lombok.NoArgsConstructor;
import tma.bill.menu.MenuItem;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bill_menu")
@NoArgsConstructor
@Data
public class BillOrderMenu {
  @EmbeddedId
  BillOrderMenuIdentity id;

  private Integer quantity;
  private Date orderedDate;

  public BillOrderMenu(BillOrder bill) {
    this.getId().setBill(bill);
  }

  public BillOrderMenu(MenuItem menu, Integer quantity, Date orderedDate) {
    this.id = new BillOrderMenuIdentity();
    this.id.setMenu(menu);
    this.quantity = quantity;
    this.orderedDate = orderedDate;
  }
}
