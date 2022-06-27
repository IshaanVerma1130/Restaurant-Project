# A restaurant simulation using java

### Entities:

1. Restaurant - Main function
2. Customer
3. Chef
4. Waiter
5. Food

### Helper classes:

1. FoodType - To get a food item by its type
2. Events - To log the activities happening

## <ins>Approach</ins>

### **Restaurant**

- number of customers
- number of tables
- number of waiters
- cooking capacity per chef (can be changes in code for each type of chef)
- max number of orders per customer (for simulation purposes)

It has a waiting queue that keeps track of customers entering the restaurant. Its is used in Customers class to see for changes and respond to them. The Waiter class listens on this queue to take the order for the customer. It generates the orders for each customer. It initiates the list of chefs.

### **Customer**

- name of customer
- ordered items list

It stores the information about the customer. it has a list to store the ordered items and a maxCustomers integer to keep track of restaurant overflow. It has a runningCounter to keep track of orderNum. It has a customerCounter to keep track of the number of customers present in the restaurant. If the customerCounter is less thant he limit the thread adds the customer to the waiting queue and gives and order, otherwise it waits.

### **Waiter**

- name of waiter

It listens on the waiting queue and waits while it is empty. Once a customer is added to the waiting queue it picks out the first one in the queue and send the items according to their types to the respective chefs. After the cooking is done it delivers the orders and the customer leaves the restaurant after paying the bill. This is handeled by the waiter thread that takes the customer as the lock and after all functions are done it notifies the customer object.

### **Chef**

- name of chef
- cooking capacity of chef
- type of food they cook

The chef has a volatile flag to check if they are busy. When the waiter communicates the order to the chef, the chef makes a thread array that is equal to the number of the items ordered. The run() function checks if the chef is busy using the flag and if not, sleeps for the required amount of time (dependent on the type of food) to ape cooking. After the cooking is done it returns an array of items to the waiter (to tell the waiter the food has finished cooking).
