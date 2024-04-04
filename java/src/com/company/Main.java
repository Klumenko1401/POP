package com.company;
public class Main {
    public static void main(String[] args) {
        int number_of_cells = 100000000;
        int threadNum = 4;                                                      //Кількість потоків
        long time = System.nanoTime();                                          //Засікаємо час
        ArayClass arrClass = new ArayClass(number_of_cells, threadNum);         //Клас з усіма обраховуваннями
        long minIndex = arrClass.OneThreadMin(0, number_of_cells);      //Шукаємо мінімальний елемент у масиві за допомогою одного потоку
        time = System.nanoTime() - time;                                        //Обчислюємо час роботи програми
        System.out.println(minIndex + " time:" + time);                         //Виводимо на екран мінімальний елемент і час роботи програми з одним потоком
        time = System.nanoTime();                                               //Засікаємо час
        minIndex = arrClass.threadMin();                                        //Знаходимо minIndex в n-кількостях потоків
        time = System.nanoTime() - time;                                        //Обчислюємо час роботи програми
        System.out.println(minIndex + " time:" + time);                         //Виводимо на екран мінімальний елемент і час роботи програми в n-потоках
    }
}