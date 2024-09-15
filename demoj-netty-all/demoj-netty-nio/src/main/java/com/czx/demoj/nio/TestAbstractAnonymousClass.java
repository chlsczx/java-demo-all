package com.czx.demoj.nio;

abstract class A {
    abstract void doSomething();
}

public class TestAbstractAnonymousClass {
    public static void main(String[] args) {
        A hello = new A() {
            @Override
            void doSomething() {
                System.out.println("hello");
            }
        };

        hello.doSomething();
        System.out.println(hello.getClass());
    }
}
