package tma.utils;

import tma.RestaurantApplication;
import tma.bill.BillOrder;
import tma.bill.BillOrderMenu;
import tma.bill.BillRepository;
import tma.bill.menu.MenuItem;
import tma.bill.menu.MenuRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class DatabaseUtils {
  public static void initDB(BillRepository billRepo, MenuRepository menuRepo) {
    MenuItem menu1 = new MenuItem(
        1,
        "Hawaiian Pizza",
        "All-time favourite toppings, Hawaiian pizza in Tropical Hawaii style.",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu1.jpg",
        300,
        "Italian,Ham,Pineapple"
    );
    MenuItem menu2 = new MenuItem(
        2,
        "Chicken Tom Yum Pizza",
        "Best marinated chicken with pineapple and mushroom on Spicy Lemon sauce. Enjoy our tasty Thai style pizza.",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu2.jpg",
        350,
        "Italian,Thai,Chicken,Mushroom,Hot"
    );
    MenuItem menu3 = new MenuItem(
        3,
        "Xiaolongbao",
        "Chinese steamed bun",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu3.jpg",
        200,
        "Chinese,Pork,Recommended"
    );
    MenuItem menu4 = new MenuItem(
        4,
        "Kimchi",
        "Traditional side dish made from salted and fermented vegetables",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu4.jpg",
        50,
        "Korean,Radish,Cabbage"
    );
    MenuItem menu5 = new MenuItem(
        5,
        "Oolong tea",
        "Partially fermented tea grown in the Alishan area",
        "https://s3-ap-southeast-1.amazonaws.com/interview.ampostech.com/backend/restaurant/menu5.jpg",
        30,
        "Hot,Non-alcohol"
    );

    MenuItem menu6 = new MenuItem(
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
      BillOrderMenu orderMenu11 = new BillOrderMenu(menu1, 1, sdf.parse("1/1/2017 10:00:00 AM"));
      BillOrderMenu orderMenu12 = new BillOrderMenu(menu4, 2, sdf.parse("1/1/2017 10:00:00 AM"));
      BillOrderMenu orderMenu13 = new BillOrderMenu(menu4, 1, sdf.parse("1/1/2017 11:00:00 AM"));

      BillOrderMenu orderMenu21 = new BillOrderMenu(menu3, 1, sdf.parse("1/1/2017 12:00:00 PM"));
      BillOrderMenu orderMenu22 = new BillOrderMenu(menu6, 1, sdf.parse("1/1/2017 12:00:00 PM"));

      BillOrderMenu orderMenu31 = new BillOrderMenu(menu5, 1, sdf.parse("1/1/2017 03:00:00 PM"));
      BillOrderMenu orderMenu32 = new BillOrderMenu(menu6, 3, sdf.parse("1/1/2017 03:00:00 PM"));

      BillOrder order1 = new BillOrder(1, orderMenu11, orderMenu12, orderMenu13);
      BillOrder order2 = new BillOrder(2, orderMenu21, orderMenu22);
      BillOrder order3 = new BillOrder(3, orderMenu31, orderMenu32);

      billRepo.saveAll(Arrays.asList(order1, order2, order3));
    } catch (ParseException ex) {
      RestaurantApplication.LOG.error("Parsing error", ex);
    }
  }
}
