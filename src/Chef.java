public class Chef {
  public final String name;
  public final Food foodType;
  private final int cookingCapacity;

  public volatile boolean full;

  private int inProgress;
  public final Object lock = new Object();

  public Chef(String name, Food foodType, int cookingCapacity) {
    this.name = name;
    this.foodType = foodType;
    this.cookingCapacity = cookingCapacity;
    this.full = cookingCapacity <= 0;
    this.inProgress = 0;

    Restaurant.log(Events.chefArrived(this, foodType, cookingCapacity));
  }

  private class CookItem implements Runnable {
    private Waiter waiter;
    private Customer customer;

    public CookItem(Waiter waiter, Customer customer) {
      this.waiter = waiter;
      this.customer = customer;
    }

    public void run() {
      try {
        Restaurant.log(Events.waiterCommunicatedOrder(waiter, foodType, customer.orderNum));
        Restaurant.log(Events.chefStartedCooking(Chef.this, foodType, customer.orderNum));

        // Cooking food
        Thread.sleep(foodType.cookingTime);

        synchronized (lock) {
          inProgress--;
          if (full)
            full = false;
          lock.notifyAll();
        }

        Restaurant.log(Events.chefFinishedCooking(Chef.this, Chef.this.foodType, customer.orderNum));
        Restaurant.log(Events.waiterServedOrder(waiter, foodType, customer.orderNum));
      } catch (InterruptedException e) {
        Restaurant.log(Events.chefLeavingRestaurant(Chef.this));
      }
    }
  }

  public Thread[] cookFood(Waiter waiter, Customer customer, int numberOfItems) throws InterruptedException {

    Thread[] threads = new Thread[numberOfItems];

    // Thread array for each item of that particular cuisine
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new CookItem(waiter, customer));
    }

    synchronized (lock) {
      for (int i = 0; i < threads.length; i++) {

        // Wait for chef to be free
        while (full)
          lock.wait();

        if (++inProgress >= cookingCapacity)
          full = true;

        // Cook item
        threads[i].start();
      }
    }

    return threads;
  }

  public String toString() {
    return name;
  }
}
