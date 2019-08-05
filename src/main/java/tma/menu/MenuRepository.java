package tma.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuRepository extends PagingAndSortingRepository<MenuModel, Integer> {
  Page<MenuModel> findAll(Pageable pageable);
  Page<MenuModel> findByNameContainingOrTagsContainingOrDescriptionContaining(String keyword1, String keyword2, String keyword3, Pageable pageable);

  MenuModel findByName(String name);

  Iterable<MenuModel> findByNameContainingOrTagsContainingOrDescriptionContaining(String keyword1, String keyword2, String keyword3);
}
