package org.example.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Latch
class RocketLaunch {
    public static void launch() throws InterruptedException {
        int tasks = 3;
        CountDownLatch latch = new CountDownLatch(tasks);

        new Thread(new PreparationTask(latch, "Заправка топлива")).start();
        new Thread(new PreparationTask(latch, "Проверка систем")).start();
        new Thread(new PreparationTask(latch, "Наведение на цель")).start();

        latch.await(); // Ожидаем завершения всех подготовительных процессов
        System.out.println("🚀 Ракета запущена!");
    }
}

class PreparationTask implements Runnable {
    private final CountDownLatch latch;
    private final String taskName;

    public PreparationTask(CountDownLatch latch, String taskName) {
        this.latch = latch;
        this.taskName = taskName;
    }

    @Override
    public void run() {
        try {
            System.out.println(taskName + " выполняется...");
            Thread.sleep((long) (Math.random() * 3000)); // Симуляция выполнения задачи
            System.out.println(taskName + " завершена.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }
}

// Observer
class NewsPublisher extends Observable {
    public void publishNews(String news) {
        setChanged(); // Указываем, что состояние изменилось
        notifyObservers(news); // Оповещаем подписчиков
    }
}

class NewsSubscriber implements Observer {
    private final String name;

    public NewsSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(name + " получил новость: " + arg);
    }
}

class ObserverExample {
    public static void launch() {
        NewsPublisher publisher = new NewsPublisher();

        NewsSubscriber sub1 = new NewsSubscriber("Алексей");
        NewsSubscriber sub2 = new NewsSubscriber("Мария");

        publisher.addObserver(sub1);
        publisher.addObserver(sub2);

        publisher.publishNews("Вышла новая версия Java!");
    }
}

// Flow
class TemperatureProcessor implements Flow.Subscriber<Integer> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // Запрашиваем первый элемент
    }

    @Override
    public void onNext(Integer temperature) {
        System.out.println("Получена температура: " + temperature + "°C");
        subscription.request(1); // Запрашиваем следующий элемент
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Ошибка: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Все температуры обработаны.");
    }
}

class FlowExample {
    public static void launch() throws InterruptedException {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        TemperatureProcessor subscriber = new TemperatureProcessor();
        publisher.subscribe(subscriber);

        int[] temperatures = {22, 24, 19, 21, 23};
        for (int temp : temperatures) {
            publisher.submit(temp);
            Thread.sleep(500); // Симуляция задержки передачи данных
        }

        publisher.close();
        Thread.sleep(1000); // Ждём завершения обработки
    }
}

// Guava eventBus
// Класс события
class LogEvent {
    private final String message;

    public LogEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

// Обработчик событий
class LogListener {
    @Subscribe
    public void handleLogEvent(LogEvent event) {
        System.out.println("Лог: " + event.getMessage());
    }
}

class GuavaEventBusExample {
    public static void launch() {
        EventBus eventBus = new EventBus();
        LogListener listener = new LogListener();
        eventBus.register(listener);

        eventBus.post(new LogEvent("Система запущена"));
        eventBus.post(new LogEvent("Ошибка подключения к БД"));
        eventBus.post(new LogEvent("Система завершена"));
    }
}

// Класс события - цена акции
class StockPrice {
    private final String stockName;
    private final double price;

    public StockPrice(String stockName, double price) {
        this.stockName = stockName;
        this.price = price;
    }

    public String getStockName() {
        return stockName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Акция: " + stockName + " | Цена: $" + price;
    }
}

// Издатель - отправляет цены акций подписчикам
class StockPublisher extends SubmissionPublisher<StockPrice> {
    public void publishStockPrice(String stockName, double price) {
        submit(new StockPrice(stockName, price));
    }
}

// Подписчик - получает цены и анализирует их
class StockSubscriber implements Flow.Subscriber<StockPrice> {
    private Flow.Subscription subscription;
    private final double threshold; // Порог изменения цены

    public StockSubscriber(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1); // Запрашиваем первый элемент
    }

    @Override
    public void onNext(StockPrice stock) {
        System.out.println("📊 Получены данные: " + stock);
        if (stock.getPrice() > threshold) {
            System.out.println("🔥 ВНИМАНИЕ! Цена акции " + stock.getStockName() + " превысила $" + threshold);
        }
        subscription.request(1); // Запрашиваем следующий элемент
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Ошибка: " + throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("✅ Все данные обработаны.");
    }
}

// Главный класс - моделирует работу системы
class FlowStockMarket {
    public static void launch() throws InterruptedException {
        StockPublisher publisher = new StockPublisher();
        StockSubscriber subscriber = new StockSubscriber(100.0); // Уведомлять, если цена выше $100
        publisher.subscribe(subscriber);

        List<StockPrice> stockPrices = List.of(
                new StockPrice("AAPL", 95.5),
                new StockPrice("GOOGL", 102.3),
                new StockPrice("AMZN", 97.8),
                new StockPrice("TSLA", 110.5),
                new StockPrice("MSFT", 89.2)
        );

        for (StockPrice stock : stockPrices) {
            publisher.publishStockPrice(stock.getStockName(), stock.getPrice());
            TimeUnit.MILLISECONDS.sleep(500); // Симуляция задержки передачи данных
        }

        publisher.close();
        TimeUnit.SECONDS.sleep(1); // Ждем завершения обработки
    }
}
