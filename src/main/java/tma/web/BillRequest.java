package tma.web;

import lombok.Data;
import tma.bill.OrderModel;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BillRequest {
  Integer orderNo;
  Set<OrderRequest> orders;
}
