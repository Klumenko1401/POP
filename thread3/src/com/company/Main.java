package com.company;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        int storageSize = 15;
        int itemNumbers = 30;
        main.starter(storageSize, itemNumbers);
    }

    private void starter(int storageSize, int itemNumbers) {
        Manager manager = new Manager(storageSize);
        int item =0;
        int i=0;
        while (item<itemNumbers){
            int col=(int)Math.round(Math.random()*storageSize);
            if (itemNumbers-item>col){
                new  Producer(col,manager,i);
                new  Consumer(col,manager,i);
                item+=col;
            }
            else {
                new  Producer(itemNumbers-item,manager,i);
                new  Consumer(itemNumbers-item,manager,i);
                break;
            }
            i+=1;
            if(i==20)
            {break;}
        }
    }
}