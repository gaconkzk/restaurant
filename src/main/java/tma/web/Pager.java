package tma.web;

import lombok.Data;
import tma.bill.BillOrder;
import tma.bill.menu.MenuItem;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Pager<T> {
  int page;
  int totalPages;
  int totalItems;

  List<T> data;


  boolean first;
  boolean last;

  public static class PagerMenu extends Pager<MenuItem> {}
  public static class PagerBill extends Pager<Bill> {}

  public static PagerMenu fromMenu(org.springframework.data.domain.Page<MenuItem> page) {
    PagerMenu p = new PagerMenu();
    p.page = page.getNumber() + 1;
    p.totalPages = page.getTotalPages();
    p.totalItems = (int) page.getTotalElements();
    p.data = page.getContent();
    p.first = page.isFirst();
    p.last = page.isLast();

    return p;
  }

  public static PagerBill fromBill(org.springframework.data.domain.Page<BillOrder> page) {
    PagerBill p = new PagerBill();
    p.page = page.getNumber() + 1;
    p.totalPages = page.getTotalPages();
    p.totalItems = (int) page.getTotalElements();
    p.data = page.getContent().stream().map(Bill::fromModel).collect(Collectors.toList());
    p.first = page.isFirst();
    p.last = page.isLast();

    return p;
  }
}
