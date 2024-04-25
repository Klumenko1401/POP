#include "pch.h"
#include <iostream>
#include <random>
#include "omp.h"

using namespace std;

struct sum_idx
{
    int index;
    long long sum;
};


const int arrLength = 10000;
const int matrixLength = 10000;

int matrix[matrixLength][arrLength];

void matrix_init(); // ініціалізує матрицю
long long part_sum(int, int, int); //частини сум
sum_idx part_min(int, int, int); //частини мінімальних сум


int main() {
    matrix_init();
    int threads_amount = 3; //кількість потоків
    omp_set_nested(1); // встановлює вкладеність паралельних розділів
    double t1 = omp_get_wtime(); //Вимірюється початковий час виконання програми

#pragma omp parallel sections //розділи коду, які виконуються паралельно
    {
#pragma omp section
        {
            long long min = INT64_MAX;
            int idx = -1;
            for (int i = 0; i < threads_amount; i++) {
                sum_idx part = part_min(i * arrLength / threads_amount + 1, (i + 1) * arrLength / threads_amount, threads_amount);
                //мінімальної суми в рядках матриці для певного діапазону рядків
                if (min > part.sum) {
                    min = part.sum;
                    idx = part.index;
                }
            }
            printf("minimal sum: %lld in index: %d\n", min, idx);
        }
#pragma omp section
        {

            long long sum = 0;
            for (int i = 0; i < threads_amount; i++) {
                sum += part_sum(i * arrLength / threads_amount + 1, (i + 1) * arrLength / threads_amount, threads_amount);
            }//: До змінної sum додається частина суми для певного діапазону рядків матриці
            printf("sum: %lld\n", sum);
        }
    }

    double t2 = omp_get_wtime(); //Вимірюється час завершення виконання програми

    printf("time: %f\n", t2 - t1);
    return 0;
}

void  matrix_init() {
    for (int i = 0; i < matrixLength; i++) {
        for (int j = 0; j < arrLength; j++)
            matrix[i][j] = rand();
    }
}
//обчислює частину суми елементів матриці для певного діапазону рядків
long long part_sum(int start_idx, int end_idx, int thread_amount) {
    long long sum = 0;
    double t1 = omp_get_wtime();
   // використовується директива OpenMP, яка розподіляє ітерації циклу між потоками
#pragma omp parallel for reduction(+:sum) num_threads(thread_amount)
    for (int i = start_idx; i < end_idx; i++) {
        for (int j = 0; j < arrLength; j++)
            sum += matrix[i][j];
    }

    double t2 = omp_get_wtime();
    //printf("sum: %lld, time: %f\n", sum, t2-t1);
    //cout << "sum of " << thread_amount << " threads is " << sum << " worked seconds: " << t2-t1<<"\n";
    return sum;
}

sum_idx part_min(int start_idx, int end_idx, int thread_amount) {
    sum_idx result;
    long long min_sum = INT64_MAX;
    int row_min_sum = -1;
    double t1 = omp_get_wtime();
#pragma omp parallel for num_threads(thread_amount)
    for (int i = start_idx; i < end_idx; i++) {
        long long sum = 0;

        for (int j = 0; j < arrLength; j++)
            sum += matrix[i][j];

        if (min_sum > sum)
//критичний розділ, що забезпечує атомарність виконання коду всередині блоку
#pragma omp critical
            if (min_sum > sum) {
                min_sum = sum;
                row_min_sum = i;
            }
    }
    double t2 = omp_get_wtime();
   // printf("min row: %d, time: %f\n", row_min_sum, t2-t1);
    result.sum = min_sum;
    result.index = row_min_sum;
    return result;
}