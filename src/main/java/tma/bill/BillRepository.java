package tma.bill;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends PagingAndSortingRepository<OrderModel, Integer> {
}
