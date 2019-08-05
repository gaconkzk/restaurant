package tma.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tma.RestaurantApplication;
import tma.bill.OrderModel;
import tma.bill.OrderMenu;
import tma.bill.BillRepository;
import tma.menu.MenuModel;
import tma.menu.MenuRepository;

@Component
public class DBInitializer {
  private MenuRepository menuRepo;

  private BillRepository billRepo;

  @Autowired
  public void DBInitializer(MenuRepository menuRepository, BillRepository billRepository) {
      this.menuRepo = menuRepository;
      this.billRepo = billRepository;
  }

  public void init() {
    MenuModel menu1 = new MenuModel(
        1,
        "Hawaiian Pizza",
        "All-time favourite toppings, Hawaiian pizza in Tropical Hawaii style.",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu1.jpg",
        300,
        "Italian,Ham,Pineapple"
    );
    MenuModel menu2 = new MenuModel(
        2,
        "Chicken Tom Yum Pizza",
        "Best marinated chicken with pineapple and mushroom on Spicy Lemon sauce. Enjoy our tasty Thai style pizza.",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu2.jpg",
        350,
        "Italian,Thai,Chicken,Mushroom,Hot"
    );
    MenuModel menu3 = new MenuModel(
        3,
        "Xiaolongbao",
        "Chinese steamed bun",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu3.jpg",
        200,
        "Chinese,Pork,Recommended"
    );
    MenuModel menu4 = new MenuModel(
        4,
        "Kimchi",
        "Traditional side dish made from salted and fermented vegetables",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu4.jpg",
        50,
        "Korean,Radish,Cabbage"
    );
    MenuModel menu5 = new MenuModel(
        5,
        "Oolong tea",
        "Partially fermented tea grown in the Alishan area",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu5.jpg",
        30,
        "Hot,Non-alcohol"
    );

    MenuModel menu6 = new MenuModel(
        6,
        "Beer",
        "Fantastic flavors and authentic regional appeal beer",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu6.jpg",
        60,
        "Alcohol"
    );

    menuRepo.saveAll(Arrays.asList(menu1, menu2, menu3, menu4, menu5, menu6));

    SimpleDateFormat sdf = new SimpleDateFormat("M/D/Y hh:mm:ss a");
    try {
      OrderMenu orderMenu11 = new OrderMenu(menu1, 1, sdf.parse("1/1/2017 10:00:00 AM"));
      OrderMenu orderMenu12 = new OrderMenu(menu4, 2, sdf.parse("1/1/2017 10:00:00 AM"));
      OrderMenu orderMenu13 = new OrderMenu(menu4, 1, sdf.parse("1/1/2017 11:00:00 AM"));

      OrderMenu orderMenu21 = new OrderMenu(menu3, 1, sdf.parse("1/1/2017 12:00:00 PM"));
      OrderMenu orderMenu22 = new OrderMenu(menu6, 1, sdf.parse("1/1/2017 12:00:00 PM"));

      OrderMenu orderMenu31 = new OrderMenu(menu5, 1, sdf.parse("1/1/2017 03:00:00 PM"));
      OrderMenu orderMenu32 = new OrderMenu(menu6, 3, sdf.parse("1/1/2017 03:00:00 PM"));

      OrderModel order1 = new OrderModel(1, orderMenu11, orderMenu12, orderMenu13);
      OrderModel order2 = new OrderModel(2, orderMenu21, orderMenu22);
      OrderModel order3 = new OrderModel(3, orderMenu31, orderMenu32);

      billRepo.saveAll(Arrays.asList(order1, order2, order3));
    } catch (ParseException ex) {
      RestaurantApplication.LOG.error("Parsing error", ex);
    }
  }
}
