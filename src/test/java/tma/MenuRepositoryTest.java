package tma;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import tma.bill.menu.MenuItem;
import tma.bill.menu.MenuRepository;
import tma.components.DBInitializer;
import tma.components.HttpServer;
import tma.conf.RestaurantProperties;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MenuRepositoryTest {
  @MockBean
  HttpServer httpServer;

  @MockBean
  DBInitializer dbInitializer;

  @Autowired
  private TestEntityManager em;

  @Autowired
  private MenuRepository menuRepo;

  @Test
  public void whenFindByName_thenReturnMenu() {
    // given
    MenuItem menuItem = new MenuItem( "Test A", "Description", "url", 1, "tag1, Tag2");
    em.persist(menuItem);
    em.flush();

    // when
    MenuItem found = menuRepo.findByName(menuItem.getName());

    // then
    assertThat(found.getId()).isNotNull();
    assertThat(found.getName()).as("check %s's age", menuItem.getName()).isEqualTo(menuItem.getName());
  }
}
