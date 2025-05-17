package org.dieraw;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class OptimizationMethodsTask1 {

    private static final double E = Math.E;
    private static final double EPSILON = 0.01;
    private static int functionEvaluations;

    public static void main(String[] args) {
        System.out.println("Задача 17.45:");
        System.out.println("f(x) = x^2 + 2 * (x * lg(x/e) - 2)");
        System.out.println("Интервал: [1.5, 2], Эпсилон: " + EPSILON);
//        double a = 1.5;
//        double b = 2.0;

        double a = 3.0;
        double b = 3.5;

        // 1. Метод пассивного поиска
        functionEvaluations = 0;
        double passiveSearchResult = passiveSearch(a, b, EPSILON);
        System.out.println("\nРезультат пассивного поиска: x = " + formatDouble(passiveSearchResult) + ", f(x) = " + formatDouble(f(passiveSearchResult)) + ", Количество вычислений функции = " + functionEvaluations);

        // 2. Метод дихотомии
        functionEvaluations = 0;
        double dichotomyResult = dichotomy(a, b, EPSILON);
        System.out.println("\nРезультат метода дихотомии: x = " + formatDouble(dichotomyResult) + ", f(x) = " + formatDouble(f(dichotomyResult)) + ", Количество вычислений функции = " + functionEvaluations);

        // 3. Метод золотого сечения
        functionEvaluations = 0;
        double goldenSectionResult = goldenSectionSearch(a, b, EPSILON);
        System.out.println("\nРезультат метода золотого сечения: x = " + formatDouble(goldenSectionResult) + ", f(x) = " + formatDouble(f(goldenSectionResult)) + ", Количество вычислений функции = " + functionEvaluations);

        // 4. Метод Фибоначчи
        functionEvaluations = 0;
        double fibonacciResult = fibonacciSearch(a, b, EPSILON);
        System.out.println("\nРезультат метода Фибоначчи: x = " + formatDouble(fibonacciResult) + ", f(x) = " + formatDouble(f(fibonacciResult)) + ", Количество вычислений функции = " + functionEvaluations);
    }

    private static double f(double x) {
        functionEvaluations++;
//        return (Math.pow(x, 2) + 2 * (x * (Math.log10(x / E)) - 2));
        return (5 * Math.pow(x, 2) - 8 * Math.pow(x, 1.25) - 20 * x); // другой вариант

    }

    // 1. Метод пассивного поиска
    private static double passiveSearch(double a, double b, double epsilon) {
        int n = (int) Math.ceil((b - a) / epsilon);
        double minF = Double.MAX_VALUE;
        double minX = 0;

        for (int i = 0; i <= n; i++) {
            double x = a + (b - a) * i / n;
            double fx = f(x);
            if (fx < minF) {
                minF = fx;
                minX = x;
            }
        }
        return minX;
    }

    // 2. Метод дихотомии
    private static double dichotomy(double a, double b, double epsilon) {
        double delta = epsilon / 2.0;
        while (b - a > 2 * epsilon) { // Критерий остановки: длина интервала > 2 * epsilon
            double c = (a + b) / 2.0 - delta;
            double d = (a + b) / 2.0 + delta;
            if (f(c) <= f(d)) {
                b = d; // Сужаем интервал справа, если f(c) <= f(d)
            } else {
                a = c; // Сужаем интервал слева, если f(c) > f(d)
            }
        }
        return (a + b) / 2.0;
    }

    // 3. Метод золотого сечения
    private static double goldenSectionSearch(double a, double b, double epsilon) {
        // Константа золотого сечения
        final double GOLDEN_RATIO = (Math.sqrt(5) - 1) / 2; // Примерно 0.618

        double x1 = b - GOLDEN_RATIO * (b - a);
        double x2 = a + GOLDEN_RATIO * (b - a);
        double f1 = f(x1);
        double f2 = f(x2);

        int iterations = 0;
        while (b - a > epsilon) { // Добавляем ограничение на итерации
            iterations++;
            if (f1 < f2) { // Если f(x1) < f(x2), то минимум находится в интервале [a, x2]
                b = x2;
                x2 = x1;
                f2 = f1;
                x1 = b - GOLDEN_RATIO * (b - a);
                f1 = f(x1);
            } else {  //Если f(x1) >= f(x2), то минимум находится в интервале [x1, b]
                a = x1;
                x1 = x2;
                f1 = f2;
                x2 = a + GOLDEN_RATIO * (b - a);
                f2 = f(x2);
            }
        }
        return (a + b) / 2.0;
    }

    private static double fibonacciSearch(double a, double b, double epsilon) {double[] fib = new double[100];
        fib[0] = 1;
        fib[1] = 1;

        int n = 1;
        for (int i = 2; i < 100; i++) {
            fib[i] = fib[i-1] + fib[i-2];
            if (fib[i] >= (b - a) / epsilon) {
                n = i;
                break;
            }
        }

        if (n < 3) n = 3;

        double a_n = a;
        double b_n = b;

        double x1 = a_n + (fib[n-2] / fib[n]) * (b_n - a_n);
        double x2 = a_n + (fib[n-1] / fib[n]) * (b_n - a_n);

        double f1 = f(x1);
        double f2 = f(x2);

        for (int k = 1; k <= n-3; k++) {
            if (f1 > f2) {
                a_n = x1;
                x1 = x2;
                f1 = f2;
                if (n-k-1 >= 0 && n-k >= 0) {
                    x2 = a_n + (fib[n-k-1] / fib[n-k]) * (b_n - a_n);
                    f2 = f(x2);
                }
            } else {
                b_n = x2;
                x2 = x1;
                f2 = f1;
                if (n-k-2 >= 0 && n-k >= 0) {
                    x1 = a_n + (fib[n-k-2] / fib[n-k]) * (b_n - a_n);
                    f1 = f(x1);
                }
            }
        }
    // Обработка финальных сравнений
    // Вместо того чтобы пытаться вычислить новую точку, делаем необольшой шаг
        double delta = epsilon / 10.0;
        x2 = x1 + delta;
        f2 = f(x2);

        if (f1 > f2) {
            a_n = x1;
        } else {
            b_n = x2;
        }

        return (a_n + b_n) / 2.0;
    }

    private static String formatDouble(double d) {
        BigDecimal bd = BigDecimal.valueOf(d);
        bd = bd.round(new MathContext(9, RoundingMode.HALF_UP));
        return bd.toString();
    }
}