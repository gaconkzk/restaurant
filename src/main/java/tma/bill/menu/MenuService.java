package tma.bill.menu;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tma.RestaurantApplication;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
  private MenuRepository menuRepository;

  @Autowired
  public MenuService(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  MenuItem create(@NotNull MenuItem menu) {
    try {
      if (menu.getId() != null) {
        throw new BadRequestResponse("Menu id should not existed.");
      }
      MenuItem existed = menuRepository.findByName(menu.getName());
      if (existed != null) {
        throw new BadRequestResponse("Menu " + menu.getName() + " already existed.");
      }
      return menuRepository.save(menu);
    } catch (Exception ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw new InternalServerErrorResponse(ex.getMessage());
    }
  }

  void delete(@NotNull Integer id) {
    try {
      this.menuRepository.deleteById(id);
    } catch (EmptyResultDataAccessException ex) {
      RestaurantApplication.LOG.error("Error delete", ex);
      throw new NotFoundResponse("Menu " + id + " not existed.");
    } catch (Exception ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw new InternalServerErrorResponse(ex.getMessage());
    }
  }

  MenuItem find(@NotNull Integer id) {
    Optional<MenuItem> menu = this.menuRepository
        .findById(id);
    return menu.orElseThrow(() -> new NotFoundResponse("Menu" + id + " not found"));
  }

  void update(MenuItem menu) {
    menuRepository.save(menu);
  }

  Page<MenuItem> findAll(Pageable pageable) {
    return menuRepository.findAll(pageable);
  }

  Page<MenuItem> search(String keyword, Pageable pageable) {
    return menuRepository.findByNameContainingOrTagsContainingOrDescriptionContaining(keyword, keyword, keyword, pageable);
  }

  Iterable<MenuItem> search(String keyword) {
    return menuRepository.findByNameContainingOrTagsContainingOrDescriptionContaining(keyword, keyword, keyword);
  }

  Iterable<MenuItem> findAll() {
    return menuRepository.findAll();
  }

  public MenuItem findByName(String name) {
    return menuRepository.findByName(name);
  }
}
