package tma.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tma.bill.OrderMenu;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "menu")
@Data
public class MenuModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private String description;
  private String image;

  private Integer price;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private String tags;


  public List<String> getTags() {
    return Arrays.asList(tags.split(","));
  }
  public void setTags(List<String> tags) {
    this.tags = String.join(",", tags);
  }

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<OrderMenu> ordersMenus;

  public MenuModel(String name, String description, String image, Integer price, String tags) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.tags = tags;
  }

  public MenuModel(Integer id, String name, String description, String image, Integer price, String tags) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.tags = tags;
  }
}

