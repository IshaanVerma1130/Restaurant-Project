import java.util.List;

public class Events {
  public enum EventType {
    /* General events */
    restaurantStarting,
    restaurantEnded,
    /* Customer events */
    customerLeftHome,
    customerEnteredRestaurant,
    customerPlacedOrder,
    customerReceivedOrder,
    customerLeavingRestaurant,
    /* Waiter Events */
    waiterArrived,
    waiterTookOrder,
    waiterCommunicatedOrder,
    waiterServedOrder,
    waiterCompletedAllOrders,
    waiterLeavingRestaurant,
    /* Chef events */
    chefArrived,
    chefStartedCooking,
    chefFinishedCooking,
    chefLeavingRestaurant
  };

  public final EventType event;

  public final Customer customer;
  public final List<Food> orderItems;
  public final Food food;
  public final int orderNum;
  public final Waiter waiter;
  public final Chef chef;
  public final int[] eventParams;

  public Events(
      EventType event,
      Waiter waiter,
      Customer customer,
      Chef chef,
      Food food,
      List<Food> orderItems,
      int orderNum,
      int[] eventParams) {
    this.event = event;
    this.waiter = waiter;
    this.customer = customer;
    this.chef = chef;
    this.food = food;
    this.orderItems = orderItems;
    this.orderNum = orderNum;
    this.eventParams = eventParams;
  }

  // Restaurant Events
  public static Events startRestaurant(
      int numberOfCustomers,
      int numberOfWaiters,
      int numberOfTables,
      int chefCookingCapacity,
      int maxOrderItemsPerCustomer) {
    int[] params = new int[5];
    params[0] = numberOfCustomers;
    params[1] = numberOfWaiters;
    params[2] = numberOfTables;
    params[3] = chefCookingCapacity;
    params[4] = maxOrderItemsPerCustomer;

    return new Events(EventType.restaurantStarting, null, null, null, null, null, 0, params);
  }

  public static Events stopRestaurant() {
    return new Events(EventType.restaurantEnded, null, null, null, null, null, 0, null);
  }

  // Customer Events
  public static Events customerLeftHome(Customer customer) {
    return new Events(EventType.customerLeftHome, null, customer, null, null, null, 0, null);
  }

  public static Events customerEnteredRestaurant(Customer customer) {
    return new Events(EventType.customerEnteredRestaurant, null, customer, null, null, null, 0, null);
  }

  public static Events customerPlacedOrder(Customer customer, List<Food> orderItems, int orderNum) {
    return new Events(EventType.customerPlacedOrder, null, customer, null, null, orderItems, orderNum, null);
  }

  public static Events customerRecievedOrder(Customer customer, List<Food> orderItems, int orderNum) {
    return new Events(EventType.customerReceivedOrder, null, customer, null, null, orderItems, orderNum, null);
  }

  public static Events customerLeavingRestaurant(Customer customer) {
    return new Events(EventType.customerLeavingRestaurant, null, customer, null, null, null, 0, null);
  }

  // Waiter Events
  public static Events waiterArrived(Waiter waiter) {
    return new Events(EventType.waiterArrived, waiter, null, null, null, null, 0, null);
  }

  public static Events waiterTookOrder(Waiter waiter, List<Food> orderItems, int orderNum) {
    return new Events(EventType.waiterTookOrder, waiter, null, null, null, orderItems, orderNum, null);
  }

  public static Events waiterCommunicatedOrder(Waiter waiter, Food food, int orderNum) {
    return new Events(EventType.waiterCommunicatedOrder, waiter, null, null, food, null, orderNum, null);
  }

  public static Events waiterServedOrder(Waiter waiter, Food food, int orderNum) {
    return new Events(EventType.waiterServedOrder, waiter, null, null, food, null, orderNum, null);
  }

  public static Events waiterCompletedAllOrders(Waiter waiter, int orderNum) {
    return new Events(EventType.waiterCompletedAllOrders, waiter, null, null, null, null, orderNum, null);
  }

  public static Events waiterLeavingRestaurant(Waiter waiter) {
    return new Events(EventType.waiterLeavingRestaurant, waiter, null, null, null, null, 0, null);
  }

  // Chef Events
  public static Events chefArrived(Chef chef, Food food, int chefCookingCapacity) {
    int[] params = new int[1];
    params[0] = chefCookingCapacity;

    return new Events(EventType.chefArrived, null, null, chef, food, null, 0, params);
  }

  public static Events chefStartedCooking(Chef chef, Food food, int orderNum) {
    return new Events(EventType.chefStartedCooking, null, null, chef, food, null, orderNum, null);
  }

  public static Events chefFinishedCooking(Chef chef, Food food, int orderNum) {
    return new Events(EventType.chefFinishedCooking, null, null, chef, food, null, orderNum, null);
  }

  public static Events chefLeavingRestaurant(Chef chef) {
    return new Events(EventType.chefLeavingRestaurant, null, null, chef, null, null, 0, null);
  }

  public String toString() {
    switch (event) {
      // Restaurant Events
      case restaurantStarting:
        int numberOfCustomers = eventParams[0];
        int numberOfWaiters = eventParams[1];
        int numberOfTables = eventParams[2];
        int chefCookingCapacity = eventParams[3];
        int maxOrderItemsPerCustomer = eventParams[4];
        return "\nRestaurant Opening. \nCustomers: " + numberOfCustomers + "\nWaiters: " + numberOfWaiters
            + "\nTables: " + numberOfTables + "\nCooking capacity/chef: " + chefCookingCapacity
            + "\nMax order items per customer: " + maxOrderItemsPerCustomer + "\n";

      case restaurantEnded:
        return "Restaurant Closed";

      // Customer Events
      case customerLeftHome:
        return customer + " is going to the restaurant";

      case customerEnteredRestaurant:
        return customer + " has entered the restaurant";

      case customerPlacedOrder:
        return customer + " has ordered " + orderItems + ", Order Num: " + orderNum;

      case customerReceivedOrder:
        return customer + " has recieved " + orderItems + ", Order Num: " + orderNum;

      case customerLeavingRestaurant:
        return customer + " paid a bill of $" + customer.getBill() + " and left the restaurant";

      // Waiter Events
      case waiterArrived:
        return waiter + " reported for work.";

      case waiterTookOrder:
        return waiter + " took order for " + orderItems + ". Order Num: " + orderNum;

      case waiterCommunicatedOrder:
        return waiter + " passed Order Num: " + orderNum + " to kitchen. Item: " + food;

      case waiterServedOrder:
        return waiter + " served Order Num: " + orderNum + ". Item: " + food;

      case waiterCompletedAllOrders:
        return waiter + " served all items for Order Num: " + orderNum;

      case waiterLeavingRestaurant:
        return waiter + " going home.";

      // Chef Events
      case chefArrived:
        return chef + " reported for work. Cuisine: " + food.name + "."; // Cooking capacity: " + eventParams[0];

      case chefStartedCooking:
        return chef + " cooking " + food + " for Order Num: " + orderNum;

      case chefFinishedCooking:
        return chef + " completed cooking " + food + " for Order Num: " + orderNum;

      case chefLeavingRestaurant:
        return chef + " going home.";

      default:
        throw new Error("Unrecognised event activity.");
    }
  }
}
