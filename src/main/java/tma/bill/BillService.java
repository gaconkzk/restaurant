package tma.bill;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tma.RestaurantApplication;
import tma.bill.menu.MenuModel;
import tma.bill.menu.MenuRepository;
import tma.web.Bill;

import java.util.List;
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

  public Iterable<BillModel> findAll() {
    return this.repository.findAll();
  }

  public Page<BillModel> findAll(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  public Optional<BillModel> find(Integer id) {
    return repository.findById(id);
  }

  public BillModel create(BillModel bill) {
    try {
      if (bill.getId() != null) {
        throw new BadRequestResponse("Bill id should not existed.");
      }
      if (bill.getOrderNo() == null) {
        bill.setOrderNo(repository.maxOrderNo() + 1);
      } else {
        List<Bill> existed = repository.findByOrderNo(bill.getOrderNo());
        if (existed != null && existed.size() > 0) {
          throw new BadRequestResponse("Menu " + bill.getOrderNo() + " already existed.");
        }
      }
      MenuModel menu = menuRepository.findById(bill.getMenu().getId()).orElseThrow(() ->
          new BadRequestResponse("The menu in bill not existed."));
      bill.setMenu(menu);
      return repository.save(bill);
    } catch (Exception ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw new InternalServerErrorResponse(ex.getMessage());
    }
  }
}
