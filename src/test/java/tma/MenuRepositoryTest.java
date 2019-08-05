package tma;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import tma.menu.MenuModel;
import tma.menu.MenuRepository;
import tma.components.DBInitializer;
import tma.components.JavalinServer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MenuRepositoryTest {
  @MockBean
  JavalinServer javalinServer;

  @MockBean
  DBInitializer dbInitializer;

  @Autowired
  private TestEntityManager em;

  @Autowired
  private MenuRepository menuRepo;

  @Test
  public void whenFindByName_thenReturnMenu() {
    // given
    MenuModel menuModel = new MenuModel( "Test A", "Description", "url", 1, "tag1, Tag2");
    em.persist(menuModel);
    em.flush();

    // when
    MenuModel found = menuRepo.findByName(menuModel.getName());

    // then
    assertThat(found.getId()).isNotNull();
    assertThat(found.getName()).as("check %s's age", menuModel.getName()).isEqualTo(menuModel.getName());
  }
}
