using System;
using System.Threading;

namespace thread_sum
{   
    class Program
    {
      
        static void Main(string[] args)
        {
            Console.WriteLine("Крок: ");
            int step = int.Parse(Console.ReadLine());   //крок
            Console.WriteLine("Кiлькiсть потокiв: ");
            byte numThreads = byte.Parse(Console.ReadLine());     //кількість потоків
            double [] sums = new double[numThreads];   // сума кожного з потоків
            double[] numver_of_counts = new double[numThreads];   //кількість доданків
            bool canStop = false;
            
            Thread[] threads = new Thread[numThreads]; //масив потоків розміром заданої їх кількості
            for (int i = 0; i < numThreads; i++)
            {
                int index = i;  //позиція потоку в масиві       //лямда вираз для передачі параметрів
                threads[i] = new Thread(() => Calculator(index, step, sums, numver_of_counts, ref canStop));
                threads[i].Start();
            }
            //потік  для прапорця canStop
            Thread stopperThread = new Thread(() => Stopper(ref canStop));
            stopperThread.Start();

            
            stopperThread.Join(); //головний потік чекає завершення stopperThread 
            for (int i = 0; i < numThreads; i++)
            {
                Console.WriteLine($"Потiк {i + 1}: сума = {sums[i]} кiлькiсть доданкiв = {numver_of_counts[i]}");
            }
        }

        static void Calculator(int index, int step, double[] sums, double[] counts, ref bool canStop)
        {
            double sum = 0;
            double count = 0;

            for (int i = 0; !canStop; i += step)
            {
                sum += i;
                count++;
            }

            sums[index] = sum;
            counts[index] = count;
        }
        
        static void Stopper(ref bool canStop)
        {
            Thread.Sleep(10000);
            canStop = true;
        }
    }
}