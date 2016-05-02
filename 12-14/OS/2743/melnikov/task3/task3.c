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

void start_process(pid_t *pid, char ch, int delay) {
        *pid = fork();
        if (*pid == 0) {
                printf("Start %c\n", ch);
                sleep(delay);
                printf("Stop %c\n", ch);
                exit(0);
        }
}

void wait_process(pid_t pid, char ch) {
        waitpid(pid, NULL, 0);
        printf("Join %c\n", ch);
}

int main()
{
    for (v = 0; v < V; v++)
    {
        for (i = 0; i < E; i++)
            if (graph[i][1] == v)
            {
		wait_process(pids[i],(char)(i + 97));
 		}

        for (i = 0; i < E; i++)
            if (graph[i][0] == v)
            {
		start_process(&pids[i], (char) (i + 97), graph[i][1] - graph[i][0]);
         }
    }
    return 0;
}
