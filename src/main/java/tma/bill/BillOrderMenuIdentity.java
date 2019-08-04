package tma.bill;

import lombok.*;
import tma.bill.menu.MenuItem;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class BillOrderMenuIdentity implements Serializable {
  private Integer id;
  private MenuItem menu;
  private BillOrder bill;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BillOrderMenuIdentity)) return false;
    BillOrderMenuIdentity that = (BillOrderMenuIdentity) o;
    return id.equals(that.id) &&
      Objects.equals(menu.getName(), that.menu.getName()) &&
      Objects.equals(bill.getId(), that.bill.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, menu.getName(), bill.getId());
  }
}
