package tma.menu;

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

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class MenuService {
  private MenuRepository menuRepository;

  @Autowired
  public MenuService(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  public MenuModel create(@NotNull MenuModel menu) {
    try {
      if (menu.getId() != null) {
        throw new BadRequestResponse("Menu id should not existed.");
      }
      MenuModel existed = menuRepository.findByName(menu.getName());
      if (existed != null) {
        throw new BadRequestResponse("Menu " + menu.getName() + " already existed.");
      }
      return menuRepository.save(menu);
    } catch (Exception ex) {
      RestaurantApplication.LOG.error(ex.getMessage(), ex);
      throw new BadRequestResponse(ex.getMessage());
    }
  }

  public void delete(@NotNull Integer id) {
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

  public MenuModel find(@NotNull Integer id) {
    Optional<MenuModel> menu = this.menuRepository
        .findById(id);
    return menu.orElseThrow(() -> new NotFoundResponse("Menu " + id + " not found."));
  }

  public void update(MenuModel menu) {
    menuRepository.save(menu);
  }

  @Transactional
  public Page<MenuModel> findAll(Pageable pageable) {
    return menuRepository.findAll(pageable);
  }

  public Page<MenuModel> search(String keyword, Pageable pageable) {
    return menuRepository.findByNameContainingOrTagsContainingOrDescriptionContaining(keyword, keyword, keyword, pageable);
  }

  @Transactional
  public Set<MenuModel> getMenusByName(Set<String> names) {
    return menuRepository.getAllMenuByName(names);
  }
}
