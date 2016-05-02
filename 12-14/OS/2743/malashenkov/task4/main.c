#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define V 7
#define E 11

int graph[E][2] = {
    {0, 1}, // a
    {0, 1}, // b
    {1, 4}, // c
    {1, 5}, // d
    {1, 2}, // e
    {2, 6}, // f
    {2, 3}, // g
    {3, 4}, // h
    {4, 5}, // i
    {5, 6}, // j
    {5, 6}  // k
};

typedef struct T {
    char id;
    int delay;
} param;

pthread_t threads[E];
param params[E];
int v, i;

void *thread_function(void *args)
{
    param *p = (param *) args;
    printf("Start thread %c\n", p->id);
    sleep(p->delay);
    printf("Stop thread %c\n", p->id);
    pthread_exit(NULL);
}

int main()
{
    for (v = 0; v < V; v++)
    {
        for (i = 0; i < E; i++)
            if (graph[i][1] == v)
            {
                pthread_join(threads[i], NULL);
                printf("Join thread %c\n", (char) (i + 65));
            }

        for (i = 0; i < E; i++)
            if (graph[i][0] == v)
            {
                params[i].id = (char) (i + 65);
                params[i].delay = graph[i][1] - graph[i][0];
                pthread_create(&threads[i], NULL, thread_function, (void *) &params[i]);
            }
    }
    return 0;
}
