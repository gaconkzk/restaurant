package tma.web;

import lombok.Data;
import tma.bill.BillModel;
import tma.bill.menu.MenuModel;

import java.util.Date;

@Data
public class Bill {
  Integer id;
  Integer billNo;
  String menu;
  Integer menu_id;
  Integer quantity;
  Date orderedTime;

  public static Bill fromModel(BillModel billModel) {
    Bill b = new Bill();
    b.id = billModel.getId();
    b.billNo = billModel.getOrderNo();
    b.menu_id = billModel.getMenu().getId();
    b.menu = billModel.getMenu().getName();
    b.quantity = billModel.getQuantity();
    b.orderedTime = billModel.getDate();

    return b;
  }

  public BillModel createModel() {
    return new BillModel(
        id,
        billNo,
        new MenuModel(menu_id, menu),
        quantity,
        this.orderedTime
    );
  }
}
