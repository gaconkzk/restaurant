package tma.bill;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tma.menu.MenuModel;

import java.util.Set;

@Repository
public interface BillRepository extends PagingAndSortingRepository<OrderModel, Integer> {
  @Query(value = "select max(b.id) from bill b", nativeQuery = true)
  Integer maxOrderNo();
}
