package tma.bill.menu;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import tma.bill.BillModel;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "menu")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class MenuModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;
  String name;
  String description;
  String image;
  Integer price;
//  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
//  @JoinTable(
//      name = "tagmenu",
//      joinColumns = {@JoinColumn(name = "menu_id")},         // The name of this table's id in joined table
//      inverseJoinColumns = {@JoinColumn(name = "tag_id")}    // The name of other table's id in joined table
//  )
//  @JsonManagedReference // I'm the one that show tags
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  String tags;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @JsonIgnore
  @OneToMany
  List<BillModel> bills;

  public List<String> getTags() {
    return Arrays.asList(tags.split(","));
  }

  public void setTags(List<String> tags) {
    this.tags = String.join(",", tags);
  }

  public MenuModel(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public MenuModel(Integer id, String name, String description, String image, Integer price, String tags) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.tags = tags;
  }

  public MenuModel(String name, String description, String image, Integer price, List<String> tags) {
    this.name = name;
    this.description = description;
    this.image = image;
    this.price = price;
    this.tags = String.join(",", tags);
  }
}
