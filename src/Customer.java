import java.util.List;

public class Customer implements Runnable {
  private final String name;
  public final List<Food> orderItems;
  public final int orderNum;
  public final int bill;

  private static int customerCount = 0;
  public static int maxCustomers = 20;
  public static Object lock = new Object();

  private static int runningCounter = 0;

  public Customer(String name, List<Food> orderItems) {
    this.orderItems = orderItems;
    this.name = name;
    this.orderNum = ++runningCounter;

    int orderTotal = 0;

    for (Food food : orderItems) {
      orderTotal += food.foodPrice();
    }

    this.bill = orderTotal;
  }

  public String toString() {
    return name;
  }

  public int getBill() {
    return bill;
  }

  public void run() {
    Restaurant.log(Events.customerLeftHome(this));

    synchronized (lock) {
      while (maxCustomers <= customerCount) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      customerCount++;
    }

    Restaurant.log(Events.customerEnteredRestaurant(this));

    synchronized (Restaurant.waitingQueue) {
      Restaurant.log(Events.customerPlacedOrder(this, this.orderItems, this.orderNum));
      Restaurant.waitingQueue.add(this);
      Restaurant.waitingQueue.notify();
    }

    synchronized (this) {
      try {
        this.wait();

        Restaurant.log(Events.customerRecievedOrder(this, this.orderItems, this.orderNum));

        // Cutomer is eating order
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    Restaurant.log(Events.customerLeavingRestaurant(this));

    synchronized (lock) {
      customerCount--;
      lock.notifyAll();
    }
  }
}