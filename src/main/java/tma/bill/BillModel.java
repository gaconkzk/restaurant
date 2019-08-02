package tma.bill;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tma.bill.menu.MenuModel;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bill")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class BillModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;

  Integer orderNo;

  @ManyToOne
  @JoinColumn(name="menu_id", nullable = false)
  MenuModel menu;

  Integer quantity;

  Date date;

  public BillModel(Integer orderNo, MenuModel menu, Integer quantity, Date date) {
    this.orderNo = orderNo;
    this.menu = menu;
    this.quantity = quantity;
    this.date = date;
  }
}
