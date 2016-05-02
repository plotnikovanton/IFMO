#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/shm.h>
#include <sys/ipc.h>

#define vertexes 7
#define edges 11

int graph[edges][3] = {
    {0, 1, 1}, // a
    {1, 2, 0}, // b
    {1, 4, 0}, // c
    {1, 3, 0}, // d
    {1, 5, 0}, // e
    {2, 6, 0}, // f
    {2, 3, 0}, // g
    {3, 4, 0}, // h
    {4, 5, 0}, // i
    {5, 6, 0}, // j
    {5, 6, 0}  // k
};

int creators[vertexes] = { -1, 0, 1, 3, 2, 4, 6};

int v, i;
pid_t pids[edges];

void startProcesses (int vertex, char ch, int shmid) {
	int parentsFinished = 0;
    int *ptr = shmat(shmid, NULL, 0);
	while (parentsFinished == 0) {
		parentsFinished = 1;
		for (i = 0; i < edges; i++) {
			if ((ptr[i * 3 + 1] == vertex) && (ptr[i * 3 + 2] == 0)){
				parentsFinished = 0;
			}
		}
		sleep(1);
	}
	for (i = 0; i < edges; i++) {
		if (ptr[i * 3] == vertex){
			start_process(ptr[i * 3 + 1], (char) (i + 97), 1, i, shmid);
		}
	}
}

void start_process(int targetVertex, char ch, int delay, int edgeNum, int shmid) {
        pid_t pid = fork();
        if (pid == 0) {
        	int *ptr = shmat(shmid, NULL, 0);
        	printf("Start %c\n", ch);
            sleep(delay);
            ptr[edgeNum * 3 + 2] = 1;
            if (creators[targetVertex] == edgeNum) {
                startProcesses(targetVertex, ch, shmid);
            }
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
	int shmid = shmget(IPC_PRIVATE, 1000, IPC_CREAT | 0666);
	int *ptr = shmat(shmid, NULL, 0);
	int j;

	for (i = 0; i < edges; i++) {
		for (j = 0; j < 3; j++) {
			ptr[i * 3 + j] = graph[i][j];
		}
	}

	startProcesses(0, 'a', shmid);
    return 0;
}
