/**
 * Created by Andrew_2 on 10/22/2015.
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
public class Dining_Philosophers {
    static Philosopher[] arrayOfPhilosopher = new Philosopher[5];
    static Chopstick[] chopstickArray = new Chopstick[5];
    static int count = 1;
public static void main(String args[]) {
    for (int i = 0; i < chopstickArray.length; i++) {
        chopstickArray[i] = new Chopstick();
    }
    arrayOfPhilosopher[0] = new Philosopher(chopstickArray[0],chopstickArray[4]);
    arrayOfPhilosopher[1] = new Philosopher(chopstickArray[1],chopstickArray[0]);
    arrayOfPhilosopher[2] = new Philosopher(chopstickArray[2],chopstickArray[1]);
    arrayOfPhilosopher[3] = new Philosopher(chopstickArray[3],chopstickArray[2]);
    arrayOfPhilosopher[4] = new Philosopher(chopstickArray[4],chopstickArray[3]);
    Thread p1 = new Thread(arrayOfPhilosopher[0],"Philosopher 1");
    Thread p2 = new Thread(arrayOfPhilosopher[1],"Philosopher 2");
    Thread p3 = new Thread(arrayOfPhilosopher[2],"Philosopher 3");
    Thread p4 = new Thread(arrayOfPhilosopher[3],"Philosopher 4");
    Thread p5 = new Thread(arrayOfPhilosopher[4],"Philosopher 5");
    p1.start();
    p2.start();
    p3.start();
    p4.start();
    p5.start();


}
}
class Philosopher implements Runnable{
    boolean isHungry = true;
    boolean isEating = false;
    boolean hasEaten = false;
    Chopstick left;
    Chopstick right;
    Philosopher(Chopstick lStick, Chopstick rStick) {
        left = lStick;
        right = rStick;
    }
    void tryToEat() {
        while(!hasEaten) {
            synchronized (this) {
                while (leftChopstickUsed() || rightChopstickUsed()) {
                    System.out.println(Thread.currentThread().getName() + " was notified, but can't get both Chopsticks!");
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
                left.setState(true);
                right.setState(true);
                System.out.println(Thread.currentThread().getName() + " has acquired both chopsticks, and will eat for 10 seconds.");
            }
            try {Thread.sleep(10000);} catch (InterruptedException e) {} // Philosopher is eating.
            synchronized (this) {
                left.setState(false);
                right.setState(false);
                hasEaten = true;
                notifyAll();
            }
        }

        System.out.println(Thread.currentThread().getName() + " is finished eating, and has notified other threads.");

      /*  while(!hasEaten) {
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " is hungry!");
                if (!leftChopstickUsed() && !rightChopstickUsed()) {
                    left.setState(true);
                    right.setState(true);
                    isEating = true;
                } else {
                    System.out.println(Thread.currentThread().getName() + " couldn't get both chopsticks!");
                }
            }
            if (isEating) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is eating for the next 10 seconds.");
                    Thread.sleep(10000);
                    System.out.println(Thread.currentThread().getName() + " has finished eating.");
                } catch (InterruptedException e) {
                }
                synchronized (this) {
                    left.setState(false);
                    right.setState(false);
                    System.out.println(Thread.currentThread().getName() + " has given up both chopsticks");
                    hasEaten = true;
                }
            }
            try{Thread.sleep(1000); }
            catch (InterruptedException e) {}
        }*/
        }



    public boolean leftChopstickUsed() {
        return left.getState();
    }
    public boolean rightChopstickUsed() {
        return right.getState();
    }
    @Override
    public void run() {
        tryToEat();
    }
}
class Chopstick {
    boolean inUse = false;

    boolean getState() {
        return inUse;
    }
    void setState(boolean newState) {
        inUse = newState;
    }
}

