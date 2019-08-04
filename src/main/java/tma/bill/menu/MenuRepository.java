package tma.bill.menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MenuRepository extends PagingAndSortingRepository<MenuItem, Integer> {
  Page<MenuItem> findAll(Pageable pageable);
  Page<MenuItem> findByNameContainingOrTagsContainingOrDescriptionContaining(String keyword1, String keyword2, String keyword3, Pageable pageable);

  MenuItem findByName(String name);

  Iterable<MenuItem> findByNameContainingOrTagsContainingOrDescriptionContaining(String keyword1, String keyword2, String keyword3);
}
