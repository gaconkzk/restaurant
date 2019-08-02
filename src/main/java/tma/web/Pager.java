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

  public static Pager fromMenu(org.springframework.data.domain.Page<MenuItem> page) {
    Pager p = new Pager<MenuItem>();
    p.page = page.getNumber() + 1;
    p.totalPages = page.getTotalPages();
    p.totalItems = (int) page.getTotalElements();
    p.data = page.getContent();
    p.first = page.isFirst();
    p.last = page.isLast();

    return p;
  }

  public static Pager fromBill(org.springframework.data.domain.Page<BillOrder> page) {
    Pager p = new Pager<Bill>();
    p.page = page.getNumber() + 1;
    p.totalPages = page.getTotalPages();
    p.totalItems = (int) page.getTotalElements();
    p.data = page.getContent().stream().map(Bill::fromModel).collect(Collectors.toList());
    p.first = page.isFirst();
    p.last = page.isLast();

    return p;
  }
}
