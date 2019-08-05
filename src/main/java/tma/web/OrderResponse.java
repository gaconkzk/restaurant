package tma.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse extends OrderRequest {
  Integer total;

  public OrderResponse(String menu, Integer quantity, Date orderedDate, Integer total) {
    super(menu, quantity, orderedDate);
    this.total = total;
  }
}
