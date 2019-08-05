package tma;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import tma.menu.MenuModel;
import tma.menu.MenuService;
import tma.components.DBInitializer;
import tma.components.JavalinServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@ComponentScan("tma")
@DataJpaTest
public class MenuServiceTest {
  @MockBean
  private JavalinServer javalinServer;

  @MockBean
  private DBInitializer dbInitializer;

  @Autowired
  private
  MenuService menuService;

  @Test
  public void whenCreateMenu_thenReturnMenuWithId() {
    // given
    MenuModel menuModel = new MenuModel( "Test A", "Description", "url", 1, "tag1, Tag2");

    // when
    MenuModel created = menuService.create(menuModel);

    // then
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Test A");
  }

  @Test
  public void whenCreateExistedMenu_thenReturnBadRequestResponse() {
    // given
    MenuModel menuModel = new MenuModel( "Xiaolongbao", "Description", "url", 1, "tag1, Tag2");
    menuService.create(menuModel);

    // then
    assertThatThrownBy(() -> {
      MenuModel newModel = new MenuModel( "Xiaolongbao", "Description", "url", 1, "tag1, Tag2");
      menuService.create(newModel); // create again for test insert existed
    }).isInstanceOf(BadRequestResponse.class)
    .hasMessageContaining("Menu Xiaolongbao already existed.");
  }

  @Test
  public void whenDeletedMenu() {
    // given
    MenuModel menuModel = new MenuModel( "Xiaolongbao", "Description", "url", 1, "tag1, Tag2");
    menuService.create(menuModel);

    // when
    menuService.delete(menuModel.getId());

    // should not have any exception thrown
    Assert.assertTrue(true);
  }

  @Test
  public void whenDeleteNotExistingMenu_thenReturnNotFoundResponse() {
    // when
    assertThatThrownBy(() -> {
      menuService.delete(1); // create again for test insert existed
    }).isInstanceOf(NotFoundResponse.class)
      .hasMessageContaining("Menu 1 not existed.");
  }

  @Test
  public void whenFindMenuById() {
    // given
    MenuModel menuModel = new MenuModel( "Xiaolongbao", "Description", "url", 1, "tag1, Tag2");
    menuModel = menuService.create(menuModel);

    // when
    MenuModel found = menuService.find(menuModel.getId());

    // then
    assertThat(found.getId()).isEqualTo(menuModel.getId());
    assertThat(found.getName()).isEqualTo("Xiaolongbao");
  }

  @Test
  public void whenFindNotExistingMenuById_thenReturnNotFoundResponse() {
    // when
    assertThatThrownBy(() -> {
      menuService.find(1); // create again for test insert existed
    }).isInstanceOf(NotFoundResponse.class)
      .hasMessageContaining("Menu 1 not found.");
  }

  @Test
  public void whenUpdateMenu() {
    // given
    MenuModel menuModel = new MenuModel( "Xiaolongbao", "Description", "url", 1, "tag1, Tag2");
    menuModel = menuService.create(menuModel);

    // when
    menuModel.setName("A Xiaolongbao");
    menuService.update(menuModel);
    MenuModel updated = menuService.find(menuModel.getId());

    // then
    assertThat(updated.getName()).isEqualTo("A Xiaolongbao");
  }
}
