package tma.bill;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import tma.web.Bill;

import java.util.List;

public interface BillRepository extends PagingAndSortingRepository<BillModel, Integer> {
  List<Bill> findByOrderNo(Integer orderNo);

  @Query(value = "select max(b.order_no) from bill b", nativeQuery = true)
  Integer maxOrderNo();
}
