package com.company;
import java.util.Random;
public class ArayClass {
    private final int number_of_cells;
    private final int threadNum;

    public final int[] arr;


    public ArayClass(int number_of_cells, int threadNum) {
        this.number_of_cells = number_of_cells;
        arr = new int[number_of_cells];
        this.threadNum = threadNum;
        for(int i = 0; i < number_of_cells; i++){
            arr[i] = i;
        }
        Random random = new Random();
        arr[random.nextInt(number_of_cells)]*=-1;
    }

    public long OneThreadMin(int startIndex, int finishIndex){ //Знаходження мінімального в цих межах
        long min =Long.MAX_VALUE;
        for(int i = startIndex; i < finishIndex; i++){
            if(min>arr[i]){
                min=arr[i];
            }
        }
        return min;
    }

    private long min = 0;

    synchronized private long getMin() {//Чекаємо поки виведуться всі потоки(після чого виведем min)
        while (getThreadCount()<threadNum){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return min;
    }

    synchronized public void collectMin(long min){//Знаходимо min
        if(this.min>min){
            this.min = min;
        }
    }

    private int threadCount = 0;//Кількість завершених потоків
    synchronized public void incThreadCount(){
        threadCount++;
        notify();//завершуємо while в getmin()
    }

    private int getThreadCount() {
        return threadCount;
    }

    public long threadMin(){
        ThreadMin[] threadMins = new ThreadMin[threadNum];                                                   //Створюємо масив для потоків
        int len = number_of_cells /threadNum;                                                                //Знаходимо крок між межами масиву
        for (int i=0;i<threadNum-1;i++) {
            threadMins[i] = new ThreadMin(len*i, len*(i+1), this);                  //Створюємо потік в масиві
            threadMins[i].start();                                                                          //Запускаємо поток
        }
        threadMins[threadNum-1]= new ThreadMin(len*(threadNum-1), number_of_cells, this);   //Створюємо поток в масиві, щоб останні елементи не втрачались
        threadMins[threadNum-1].start();                                                                    //Запускаємо поток
        return getMin();
    }
}
