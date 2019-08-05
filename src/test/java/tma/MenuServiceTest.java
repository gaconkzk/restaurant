package tma;

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
import tma.components.HttpServer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan("tma")
@DataJpaTest
public class MenuServiceTest {
  @MockBean
  HttpServer httpServer;

  @MockBean
  DBInitializer dbInitializer;

  @Autowired
  MenuService menuService;

  @Test
  public void whenCreateMenu_thenReturnMenuWithId() {
    // given
    MenuModel menuModel = new MenuModel( "Test A", "Description", "url", 1, "tag1, Tag2");

    // when
    MenuModel found = menuService.create(menuModel);

    // then
    assertThat(found.getId()).isNotNull();
    assertThat(found.getName()).as("check %s's age", menuModel.getName()).isEqualTo(menuModel.getName());
  }
}
