#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#define V 7
#define E 11

int g[E][2] = {
    {0,1},
    {0,4},
    {0,5},
    {1,2},
    {1,3},
    {2,3},
    {2,4},
    {3,4},
    {4,5},
    {5,6},
    {5,6}
};

int v,e;

int main()
{
    for (v=0;v<V;v++)
    {
        for(e=0;e<E;e++)
            if(g[e][1]==v)//Находим процесс который должен нас подождать
            {
                waitpid(pids[e], NULL, 0);
                printf("Join process #%d, pid=%d\n",e,pids[e]);
            }
        for(e=0;e<E;e++)
            if(g[e][0]==v)//Находим процессы которые надо запустить
            {
                pids[e]=fork();
                if (pids[e]==0)//Если мы дочерний процеыы
                {
                    printf("Run #%d, pid=%d\n",e,getpid());
                    sleep((g[e][1]-g[e][0])*0.001);//Ждем момента завершения
                    printf("Stop #%d, pid=%d\n",e,getpid());
                    return 0;                
                }
            }
   } 
    return 0;
}
