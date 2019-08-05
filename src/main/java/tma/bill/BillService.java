package tma.bill;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tma.RestaurantApplication;
import tma.menu.MenuRepository;

import java.util.Optional;

@Service
public class BillService {
  private final BillRepository repository;
  private final MenuRepository menuRepository;

  @Autowired
  public BillService(BillRepository repository, MenuRepository menuRepository) {
    this.repository = repository;
    this.menuRepository = menuRepository;
  }

  Iterable<OrderModel> findAll() {
    return this.repository.findAll();
  }

  Page<OrderModel> findAll(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  Optional<OrderModel> find(Integer id) {
    return repository.findById(id);
  }

  OrderModel update(OrderModel bill) {
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

  OrderModel create(OrderModel bill) {
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
