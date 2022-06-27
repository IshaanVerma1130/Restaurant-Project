import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Restaurant {
  public static List<Customer> waitingQueue;
  public static List<Chef> chefs;

  public static void log(Events event) {
    System.out.println(event);
  }

  public static void runRestaurant(
      int numberOfCustomers,
      int numberOfWaiters,
      int numberOfTables,
      int capacityOfChef,
      int maxOrderItemsPerCustomer) {

    // Start Restaurant
    log(Events.startRestaurant(numberOfCustomers, numberOfWaiters, numberOfTables, capacityOfChef,
        maxOrderItemsPerCustomer));

    waitingQueue = new ArrayList<>();
    chefs = new ArrayList<>();

    Customer.maxCustomers = numberOfTables;

    // Add chefs
    chefs.add(new Chef("Chef 1", FoodType.indian, capacityOfChef));
    chefs.add(new Chef("Chef 2", FoodType.continental, capacityOfChef));
    chefs.add(new Chef("Chef 3", FoodType.chinese, capacityOfChef));

    // Add waiters adn start their work
    Thread[] waiters = new Thread[numberOfWaiters];
    for (int i = 0; i < waiters.length; i++) {
      waiters[i] = new Thread(new Waiter("Waiter " + (i + 1)));
      waiters[i].start();
    }

    // Add customers and setup their orders
    Thread[] customers = new Thread[numberOfCustomers];
    LinkedList<Food> orderItems;
    for (int i = 0; i < customers.length; i++) {
      Random random = new Random();
      int indian = random.nextInt(maxOrderItemsPerCustomer / 3);
      int chinese = random.nextInt(maxOrderItemsPerCustomer / 3);
      int continental = random.nextInt(maxOrderItemsPerCustomer / 3);

      orderItems = new LinkedList<Food>();

      for (int itr = 0; itr <= indian; itr++) {
        orderItems.add(FoodType.indian);
      }

      for (int itr = 0; itr <= continental; itr++) {
        orderItems.add(FoodType.continental);
      }

      for (int itr = 0; itr <= chinese; itr++) {
        orderItems.add(FoodType.chinese);
      }

      customers[i] = new Thread(new Customer("Customer " + (i + 1), orderItems));
    }

    // Make each customer order their food
    for (int i = 0; i < customers.length; i++) {
      customers[i].start();
    }

    try {
      // Send customers home
      for (int i = 0; i < customers.length; i++)
        customers[i].join();

      System.out.println("\nAll customers have left the restaurant.\n");

      // Send waiters home
      for (int i = 0; i < waiters.length; i++)
        waiters[i].interrupt();
      for (int i = 0; i < waiters.length; i++)
        waiters[i].join();

      // Send chefs home
      for (Chef chef : chefs) {
        log(Events.chefLeavingRestaurant(chef));
      }
    } catch (InterruptedException e) {
      System.out.println("Restaurant shutdown unexpectedly.");
    }

    log(Events.stopRestaurant());
  }

  public static void main(String[] args) throws InterruptedException {
    int numberOfCustomers = 15;
    int numberOfWaiters = 4;
    int numberOfTables = 10;
    int cookingCapacityPerChef = 4;
    int maxOrderItemsPerCustomer = 9;

    runRestaurant(numberOfCustomers, numberOfWaiters, numberOfTables, cookingCapacityPerChef, maxOrderItemsPerCustomer);

  }
}