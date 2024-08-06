package com.czx.demo.concurrent.testThreadCpuOccupation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final int n = 16;
        System.out.printf("start %d threads\n", n);

        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    if (Thread.interrupted()) {
                        break;
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        System.in.read();
        threads.forEach(Thread::interrupt);
    }
}
