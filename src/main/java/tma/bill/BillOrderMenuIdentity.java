package tma.bill;

import lombok.*;
import tma.bill.menu.MenuItem;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillOrderMenuIdentity implements Serializable {
  @ManyToOne
  @JoinColumn
  private MenuItem menu;

  @ManyToOne
  @JoinColumn
  private BillOrder bill;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BillOrderMenuIdentity)) return false;
    BillOrderMenuIdentity that = (BillOrderMenuIdentity) o;
    return Objects.equals(menu.getName(), that.menu.getName()) &&
        Objects.equals(bill.getId(), that.bill.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(menu.getName(), bill.getId());
  }
}
