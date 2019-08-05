package tma.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import tma.bill.OrderMenu;
import tma.menu.MenuModel;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CheckMenusResponse {
  Integer total;
  Set<MenuCheckResponse> menus;

  @AllArgsConstructor
  @Data
  private static class MenuCheckResponse {
    String name;
    Integer quantity;
    Integer total;
  }

  public CheckMenusResponse(Page<MenuModel> menuModels) {
    this.menus = menuModels.stream().map(mm -> {
      // This is not a best practice, but it
      // should be enough in this simple case
      // for dealing with famous lazy initialization exception
      Integer totalQuantities = mm.getOrdersMenus().stream().filter(om -> om.getQuantity() > 0).mapToInt(OrderMenu::getQuantity).sum();
      Integer totalPrice = mm.getPrice() * totalQuantities;
      return new MenuCheckResponse(
        mm.getName(),
        totalQuantities,
        totalPrice
      );
    }).filter(checkedMenu -> checkedMenu.quantity > 0).collect(Collectors.toSet());

    this.total = this.menus.stream().mapToInt(m -> m.total).sum();
  }
}
