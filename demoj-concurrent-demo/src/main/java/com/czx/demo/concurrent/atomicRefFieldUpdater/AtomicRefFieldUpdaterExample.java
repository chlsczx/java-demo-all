package com.czx.demo.concurrent.atomicRefFieldUpdater;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;


public class AtomicRefFieldUpdaterExample {
    private static final AtomicReferenceFieldUpdater<Person, String> nameUpdater
            = AtomicReferenceFieldUpdater.newUpdater(Person.class, String.class, "name");

    public static void main(String[] args) throws InterruptedException {
        Person john = new Person("John");

        Thread thread1 = new Thread(() -> {
            boolean update = nameUpdater.compareAndSet(john, "John", "Alice");
            System.out.println("Thread " + Thread.currentThread() + " update: " + update);
        });


        Thread thread2 = new Thread(() -> {
            boolean update = nameUpdater.compareAndSet(john, "John", "Bob");
            System.out.println("Thread " + Thread.currentThread() + " update: " + update);
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("Final name: " + nameUpdater.get(john));
    }
}
