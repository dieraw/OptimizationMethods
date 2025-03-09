package org.dieraw;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class OptimizationMethodsTask1 {

    private static final double E = Math.E;
    private static final double EPSILON = 0.001;
    private static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;
    private static int functionEvaluations;

    public static void main(String[] args) {
        System.out.println("Задача 17.45:");
        System.out.println("f(x) = x^2 + 2 * (x * lg(x/e) - 2)");
        System.out.println("Интервал: [1.5, 2], Эпсилон: " + EPSILON);

        double a = 1.5;
        double b = 2.0;

//        double a = 3;
//        double b = 3.5;

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
        return (Math.pow(x, 2) + 2 * (x * (Math.log10((x / E))) - 2));
//        return (5 * Math.pow(x, 2) - 8 * Math.pow(x, 1.25) - 20 * x);
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
        while (b - a >= 2 * epsilon) {
            double c = (a + b) / 2.0 -  delta / 2.0;
            double d = (a + b) / 2.0 +  delta / 2.0;
            if (f(c) <= f(d)) {
                b = d;
            } else {
                a = c;
            }
        }
        return (a + b) / 2.0;
    }

    // 3. Метод золотого сечения
    private static double goldenSectionSearch(double a, double b, double epsilon) {
        double x1 = b - (b - a) / GOLDEN_RATIO;
        double x2 = a + (b - a) / GOLDEN_RATIO;
        double f1 = f(x1);
        double f2 = f(x2);

        while (b - a > epsilon) {
            if (f1 <= f2) {
                b = x2;
                x2 = x1;
                f2 = f1;
                x1 = b - (b - a) / GOLDEN_RATIO;
                f1 = f(x1);
            } else {
                a = x1;
                x1 = x2;
                f1 = f2;
                x2 = a + (b - a) / GOLDEN_RATIO;
                f2 = f(x2);
            }
        }
        return (a + b) / 2.0;
    }

    private static double fibonacciSearch(double a, double b, double epsilon) {
        int n = calculateFibonacciIterations(a, b, epsilon);
        if (n <= 2) return (a + b) / 2.0;

        double[] fib = fibonacciSequence(n + 3); // Увеличиваем длину массива до n+3

        double c = a + (b - a) * fib[n] / fib[n + 2]; // Индексы n и n+2 (Строка 111 - Исправлено)
        double d = a + (b - a) * fib[n + 1] / fib[n + 2]; // Индексы n+1 и n+2 (Строка 112 - Исправлено)


        double fc = f(c);
        double fd = f(d);

        for (int k = 1; k <= n; k++) { // Цикл до n (без изменений)
            if (fc <= fd) {
                b = d;
                d = c;
                fd = fc;
                c = a + (b - a) * fib[n - k] / fib[n + 2 - k]; // Индексы n-k и n+2-k (Строка 119 - Исправлено)
                fc = f(c);


            } else {
                a = c;
                c = d;
                fc = fd;
                d = a + (b - a) * fib[n + 1 - k] / fib[n + 2 - k]; // Индексы n+1-k и n+2-k (Строка 126 - Исправлено)
                fd = f(d);
            }
        }
        return (a + b) / 2.0;
    }


    private static int calculateFibonacciIterations(double a, double b, double epsilon) {
        int n = 1;
        double fn = 1;
        while ((b - a) / fn > epsilon) {
            n++;
            fn = getFibonacci(n);
        }
        return n;
    }

    private static double getFibonacci(int n) {
        if (n <= 1) return 1;
        double[] fib = fibonacciSequence(n+2);
        return fib[n-1];
    }

    private static double[] fibonacciSequence(int n) {
        if (n <= 0) return new double[0];
        if (n == 1) return new double[]{1};
        if (n == 2) return new double[]{1, 1};
        double[] fib = new double[n];
        fib[0] = 1;
        fib[1] = 1;
        for (int i = 2; i < n; i++) {
            fib[i] = fib[i - 1] + fib[i - 2];
        }
        return fib;
    }


    private static String formatDouble(double d) {
        BigDecimal bd = BigDecimal.valueOf(d);
        bd = bd.round(new MathContext(7, RoundingMode.HALF_UP));
        return bd.toString();
    }
}