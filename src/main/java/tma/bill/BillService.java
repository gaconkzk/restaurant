package tma.bill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

  public Iterable<BillOrder> findAll() {
    return this.repository.findAll();
  }

  public Page<BillOrder> findAll(Pageable pageable) {
    return this.repository.findAll(pageable);
  }

  public Optional<BillOrder> find(Integer id) {
    return repository.findById(id);
  }

//  public BillOrder create(BillOrder bill) {
//    try {
//      if (bill.getId() != null) {
//        throw new BadRequestResponse("Bill id should not existed.");
//      }
//      if (bill.getOrderNo() == null) {
//        bill.setOrderNo(repository.maxOrderNo() + 1);
//      } else {
//        List<Bill> existed = repository.findByOrderNo(bill.getOrderNo());
//        if (existed != null && existed.size() > 0) {
//          throw new BadRequestResponse("Menu " + bill.getOrderNo() + " already existed.");
//        }
//      }
//      MenuModel menu = menuRepository.findById(bill.getMenu().getId()).orElseThrow(() ->
//          new BadRequestResponse("The menu in bill not existed."));
//      bill.setMenu(menu);
//      return repository.save(bill);
//    } catch (Exception ex) {
//      RestaurantApplication.LOG.error(ex.getMessage(), ex);
//      throw new InternalServerErrorResponse(ex.getMessage());
//    }
//  }
}
