public class Food {
  public final String name;
  public final int cookingTime;
  public final int price;

  public Food(String name, int cookingTime, int price) {
    this.cookingTime = cookingTime;
    this.name = name;
    this.price = price;
  }

  public int foodPrice() {
    return price;
  }

  public String toString() {
    return name;
  }
}
