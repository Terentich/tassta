package com.tassta.test;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
Необходимо реализовать следующий функционал:
1) Реализуйте объект, который хранит в себе строку или ее представление. Объекту
необходимо:
- содержать метод, который принимает на вход значение типа int, конвертирует его в
строковое представление (например, 4 -> "4"), а затем добавляет к концу строки.
- по требованию возвращать эту строку.
2) Реализуйте 2 потока, которые в цикле добавляют всегда одно и то же число (1-й поток число
1, второй поток число 2) в строку из пункта 1.
Работа потоков должна быть организована таким образом, чтобы числа добавлялись в строку
в следующем порядке: сначала 10 чисел из первого потока, затем 10 чисел из второго, затем
снова 10 чисел из первого и так далее.
3) Реализуйте графический интерфейс с использованием JavaFX, в котором можно будет
посмотреть текущее значение строки из пункта 1 и очистить ее.
4) Проект должен собираться с помощью Gradle. Версия Java: 8.
Код необходимо выгрузить на публичный git репозиторий, с коммитами по пунктам 1,2,3
 */
public class StringHolderTest {
    @Test
    public void test1_appendValue() {
        StringHolder stringHolder = new StringHolder("Hello");

        stringHolder.appendValue(4);
        assertEquals("Hello4", stringHolder.getValue());

        stringHolder.appendValue(5);
        assertEquals("Hello45", stringHolder.getValue());
    }

    @Test
    public void test2_concurrecy_appendValue() throws InterruptedException, ExecutionException {
        final int ITERATIONS_AMOUNT = 10_000_000;
        StringHolder stringHolder = new StringHolder("Hello");
        Semaphore sem1 = new Semaphore(0);
        Semaphore sem2 = new Semaphore(10);

        CompletableFuture completableFuture1 = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < ITERATIONS_AMOUNT; i++) {
                try {
                    sem2.acquire();
                    stringHolder.appendValue(1);

                    if (sem2.availablePermits() == 0) {
                        sem1.release(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            return stringHolder;
        });

        CompletableFuture completableFuture2 = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < ITERATIONS_AMOUNT; i++) {
                try {
                    sem1.acquire();
                    stringHolder.appendValue(2);

                    if (sem1.availablePermits() == 0) {
                        sem2.release(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            return stringHolder;
        });

        CompletableFuture.allOf(completableFuture1, completableFuture2).get();
        assertTrue("String doesn't match RegExp", stringHolder.getValue().matches("Hello(11111111112222222222){" + (ITERATIONS_AMOUNT / 10) + "}"));
    }
}
