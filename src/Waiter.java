import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Waiter implements Runnable {
  private final String name;

  public Waiter(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

  public void run() {
    Restaurant.log(Events.waiterArrived(this));

    Customer customer;

    try {
      while (true) {

        // Set waiting queue as shred memory
        synchronized (Restaurant.waitingQueue) {

          // If restaurant waiting queue is empty then wait
          while (Restaurant.waitingQueue.isEmpty()) {
            Restaurant.waitingQueue.wait();
          }

          // Else pick the first customer and start the process
          customer = Restaurant.waitingQueue.remove(0);
          Restaurant.waitingQueue.notifyAll();
        }

        Restaurant.log(Events.waiterTookOrder(this, customer.orderItems, customer.orderNum));

        // if (customer != null) {

        // Set customrt as shared memory
        synchronized (customer) {
          List<Food> orderItems = customer.orderItems;
          List<Chef> chefs = Restaurant.chefs;
          Map<String, Integer> foodItems = new HashMap<>();

          // Map food item type to its quantity
          for (Food foodItem : orderItems) {
            int count = 0;

            if (foodItems.get(foodItem.name) != null) {
              count = foodItems.get(foodItem.name);
            }

            foodItems.put(foodItem.name, count + 1);
          }

          // Build threads for each order type
          Thread[] orderItemThread = new Thread[orderItems.size()];

          int i = 0;

          while (true) {

            // Give each type of food to its appropriate chef fro cooking
            for (Chef chef : chefs) {
              String foodType = chef.foodType.name;

              if (!chef.full && foodItems.get(foodType) != null) {
                Thread[] chefThreadForEachFoodType = chef.cookFood(this, customer, foodItems.get(foodType));

                // Chef completed cooking he food items for this order
                for (int j = 0; j < chefThreadForEachFoodType.length; j++) {
                  orderItemThread[i++] = chefThreadForEachFoodType[j];
                }
                foodItems.remove(foodType);
              }
            }

            if (foodItems.isEmpty())
              break;
          }

          // All order items have been cooked for this order
          for (int j = 0; j < orderItemThread.length; j++) {
            orderItemThread[j].join();
          }

          Restaurant.log(Events.waiterCompletedAllOrders(this, customer.orderNum));
          customer.notifyAll();
        }
        // }
      }
    } catch (InterruptedException e) {
      Restaurant.log(Events.waiterLeavingRestaurant(this));
    }
  }
}
