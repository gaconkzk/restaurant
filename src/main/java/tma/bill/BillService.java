package tma.bill;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tma.RestaurantApplication;
import tma.bill.menu.MenuRepository;

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

  Iterable<BillOrder> findAll() {
    return this.repository.findAll();
  }

  Page<BillOrder> findAll(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  Optional<BillOrder> find(Integer id) {
    return repository.findById(id);
  }

  BillOrder create(BillOrder bill) {
    try {
      if (bill.getId() == null) {
        bill.setId(repository.maxOrderNo() + 1);
      } else {
        Optional<BillOrder> existed = repository.findById(bill.getId());
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
