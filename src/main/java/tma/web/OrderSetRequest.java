package tma.web;

import lombok.Data;

import java.util.Set;

/**
 * This is for documenting
 */
@Data
public class OrderSetRequest {
  Set<OrderRequest> orders;
}
