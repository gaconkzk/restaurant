package tma.bill;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends PagingAndSortingRepository<BillOrder, Integer> {
//  List<Bill> findByOrderNo(Integer orderNo);

  @Query(value = "select max(b.id) from bill b", nativeQuery = true)
  Integer maxOrderNo();
}
