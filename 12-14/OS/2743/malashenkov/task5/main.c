#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>

#define MAXN 13

int n, m, i, j;
int a[MAXN][MAXN];
int b[MAXN][MAXN];
int sum_ab[MAXN][MAXN];
sem_t sem_read, sem_sum, sem_write;

void *read_matrix()
{
    for (; i < n; i++)
        for (; j < m; j++)
            scanf("%d", &a[i][j]);

    for (i = 0; i < n; i++)
        for (j = 0; j < m; j++)
            scanf("%d", &b[i][j]);

    sem_post(&sem_read);
    printf("=== READ END ===\n");
    return 0;
}

void *sum_matrix()
{
    for (i = 0; i < n; i++)
        for (j = 0; j < m; j++)
            sum_ab[i][j] = a[i][j] + b[i][j];
    sem_post(&sem_sum);
    printf("=== SUM END ===\n");
    return 0;
}

void *write_matrix()
{
    for (i = 0; i < n; i++)
    {
        for (j = 0; j < m; j++)
            printf("%d ", sum_ab[i][j]);
        printf("\n");
    }
    sem_post(&sem_write);
    printf("=== WRITE END ===\n");
    return 0;
}

int main()
{
    freopen("in", "r", stdin);
/*
    freopen("out", "w", stdout);
//*/
    scanf("%d%d", &n, &m);

    pthread_t threads[3];
    pthread_create(&threads[0], NULL, read_matrix, NULL);
    sem_wait(&sem_read);
    pthread_create(&threads[1], NULL, sum_matrix, NULL);
    sem_wait(&sem_sum);
    pthread_create(&threads[2], NULL, write_matrix, NULL);
    sem_wait(&sem_write);

    sem_destroy(&sem_read);
    sem_destroy(&sem_sum);
    sem_destroy(&sem_write);
    return 0;
}
