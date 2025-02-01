package org.example.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class Concurrency {

    // Запись должна вестись синхронизированно.
    public class RaceConditionExample {

        private static final AtomicInteger counter = new AtomicInteger();
        public static final int numberOfThreads = 10;
        public static final int numberOfIncrements = 100000;

        public static int run() {
            counter.set(0);
            Thread[] threads = new Thread[numberOfThreads];

            for (int i = 0; i < numberOfThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < numberOfIncrements; j++) {
                        counter.incrementAndGet();
                    }
                });
                threads[i].start();
            }

            for (int i = 0; i < numberOfThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Final counter value: " + counter);
            return counter.intValue();
        }
    }

    // Я предпочитаю избегать вложенных локов в принципе + уменьшать скоуп локов насколько возможно.
    // Если для завершения операции необходимо обратиться к данным, синхронизированным с помощью
    // двух разных мьютексов, как правило, эти обращения можно разделить.
    // Например, если есть такая ситуация, что нужно обновлять объект, синхронизированный мьютексом 1,
    // а для этого требуются какие то данные, синхронизированные мьютексом 2, лучше заранее получить
    // "снапшот" данных, которые будут читаться из под лока 2, создать новую копию записываемых данных,
    // и обновить данные новой копией под локом 1.
    public class DeadlockExample {

        private static final Object lock1 = new Object();
        private static final Object lock2 = new Object();

        public static void run() {
            Thread thread1 = new Thread(() -> {
                // получаем копию данных, необходимых для операции
                synchronized (lock1) {
                    System.out.println("Thread 1 acquired lock1");
                }

                // Производим длительную операцию.
                try { Thread.sleep(50); }
                catch (InterruptedException e) { e.printStackTrace(); }

                // Перезаписываем новой копией
                synchronized (lock2) {
                    System.out.println("Thread 1 acquired lock2");
                }
            });

            Thread thread2 = new Thread(() -> {
                // Такие же изменения как в первом треде
                synchronized (lock2) {
                    System.out.println("Thread 2 acquired lock2");
                }

                try { Thread.sleep(50); }
                catch (InterruptedException e) { e.printStackTrace(); }

                synchronized (lock1) {
                    System.out.println("Thread 2 acquired lock1");
                }
            });

            thread1.start();
            thread2.start();

            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Finished");
        }
    }

}
