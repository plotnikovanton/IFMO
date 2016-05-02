#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define E 11

int g[E][2] = { {0, 1}, {0, 1}, {1, 4}, {1, 5}, {1, 2}, {2, 6}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {5, 6} };

pthread_t threads[E];
int v, i;

void *thread_function(int i)
{
    printf("Start thread %c\n", (char) i + 65);
    sleep(g[i][1] - g[i][0]);
    printf("Stop thread %c\n", (char) i + 65);
    pthread_exit(NULL);
}

int main()
{
    for (v = 0; v < 7; v++)
    {
        for (i = 0; i < E; i++)
            if (g[i][1] == v)
            {
                pthread_join(threads[i], NULL);
                printf("Join thread %c\n", (char) (i + 65));
            }

        for (i = 0; i < E; i++)
            if (g[i][0] == v)
                pthread_create(&threads[i], NULL, thread_function, i);
    }
    return 0;
}
