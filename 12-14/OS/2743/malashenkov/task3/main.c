// http://www.opennet.ru/docs/RUS/glibc/glibc-23.html
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

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

int v, i;
pid_t pids[E];

int main()
{
    for (v = 0; v < V; v++)
    {
        for (i = 0; i < E; i++)
            if (graph[i][1] == v)
            {
                waitpid(pids[i], NULL, 0);
                printf("Join process %c\n", (char) (i + 65));
            }

        for (i = 0; i < E; i++)
            if (graph[i][0] == v)
            {
                pids[i] = fork();
                if (pids[i] == 0)
                {
                    printf("Start process %c, pid = %d\n", (char) (i + 65), getpid());
                    sleep(graph[i][1] - graph[i][0]);
                    printf("Stop process %c\n", (char) (i + 65));
                    return 0;
                }
            }
    }
    return 0;
}
