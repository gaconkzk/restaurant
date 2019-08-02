package tma.bill.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MenuRepository extends PagingAndSortingRepository<MenuModel, Integer> {
  Page<MenuModel> findAll(Pageable pageable);
  Page<MenuModel> findByNameContainingOrTagsContaining(String keyword1, String keyword2, Pageable pageable);

  List<MenuModel> findByName(String name);

  Iterable<MenuModel> findByNameContainingOrTagsContaining(String keyword1, String keyword2);
}
