#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#define POINTS 7
#define PROCESSES 11

int processes[11][2] = {{0, 1}, {0, 4}, {0, 5}, {1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 4}, {4, 5}, {5, 6}, {5, 6}};
char names[11] = {"abcdgefhikj"};
pid_t pids[PROCESSES];

int main(){	
	int i;
	int j;
    for (i=0;i<POINTS;i++)
    {
        for(j=0;j<PROCESSES;j++)
	    if(processes[j][1]==i)
            {
                waitpid(pids[j], NULL, 0);
                printf("	Join process %c\n", names[j]);
            }
        for(j=0;j<PROCESSES;j++)
	    if(processes[j][0]==i)
	    {
                pids[j]=fork();
                if (pids[j]==0)
                {
                    printf("Run process %c\n ",names[j]);
                    sleep(processes[j][1]-processes[j][0]);
                    printf("  Stop process %c\n",names[j]);
                    return 0;                
                }
            }	
   } 
   return 0;
}
