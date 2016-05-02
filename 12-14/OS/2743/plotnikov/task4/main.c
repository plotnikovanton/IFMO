#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define V 7
#define E 11
int e,v,g[E][2] = {
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
pthread_t threads[E];
 
void *start(int *params)
{
    printf("Start thread #%i %i\n",params[0],params[1]);
    sleep(params[1]);
    printf("Stop thread #%i\n",params[0]);
    pthread_exit(NULL);
}

int main()
{
    for (v=0;v<V;v++)
    {
        for (e=0;e<E;e++)
            if(g[e][1]==v)
            {
                pthread_join(threads[e], NULL);
                printf("Join thread #%i\n",e);            
            }
        for (e=0;e<E;e++)
            if (g[e][0]==v)
            {   
                int p[2];
                p[0]=v;
                p[1]=g[e][1]-g[e][0];
                //printf("%i\n",p[1]);
                pthread_create(&threads[e], NULL, start, p);
            }

    }
    return 0;
}
