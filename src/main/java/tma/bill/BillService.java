package tma.bill;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tma.RestaurantApplication;

import java.util.Optional;

@Service
public class BillService {
  private final BillRepository repository;

  @Autowired
  public BillService(BillRepository repository) {
    this.repository = repository;
  }

  public Page<OrderModel> findAll(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  public Optional<OrderModel> find(Integer id) {
    return repository.findById(id);
  }

  public OrderModel update(OrderModel bill) {
    try {
      if (bill.getId() == null) {
        throw new BadRequestResponse("Bill order number not existed");
      }

      return repository.save(bill);
    } catch (Exception ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw new InternalServerErrorResponse(ex.getMessage());
    }
  }

  public OrderModel create(OrderModel bill) {
    try {
      if (bill.getId() != null) {
        Optional<OrderModel> existed = repository.findById(bill.getId());
        if (existed.isPresent()) {
          throw new BadRequestResponse("Menu " + bill.getId() + " already existed.");
        }
      }

      return repository.save(bill);
    } catch (Exception ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw new InternalServerErrorResponse(ex.getMessage());
    }
  }
}
