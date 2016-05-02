#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/wait.h>
#include <stdio.h>
#include <unistd.h>

int main() {
  int n,m,i,j;
  printf("Input size of matrix:\n");
  scanf("%d %d",&n, &m);

  int *array;
  int shmid;
  char pathname[] = "part1.c";
  key_t key;

  key = ftok(pathname, 0);
  if ((shmid = shmget(key,(3*n*m)*sizeof(int),0666|IPC_CREAT|IPC_EXCL)) < 0) {
    shmid = shmget(key, (3*n*m)*sizeof(int), 0); 
  };
  array = (int*)shmat(shmid, NULL, 0);
  pid_t process;
  
  process = fork();
  while(1){
  if(process == 0) // read
  {
    printf("---!!! START READ !!!---\n");
    for(i=0; i<n*m*2; i++)
      scanf("%d", &array[i]);
    printf("---!!! END READ !!!---\n");
    break;
  } else {
    waitpid(process, NULL, 0);
    process = fork();
    if(process == 0)
    { //SUM
      printf("--!!! SUM START !!!---\n");
      for(i=0; i<n; i++)
        for(j=0; j<m; j++)
          array[2*m*n+i*m+j] = array[i*m+j] + array[m*n+i*m+j];
      printf("---!!! SUM END !!!---\n");
      break;
    } else {
      waitpid(process, NULL, 0);
      process = fork();
      if (process ==0)
      {//PRINT
        printf("--!!! PRINT START !!!---\n");
          for (i=0; i<n; i++)
          {
            for (j=0; j<m; j++)
              printf("%d ", array[2*m*n + i*m+j]);
            printf("\n");
          }
        printf("--!!! PRINT END !!!---\n");
      } else {
        waitpid(process, NULL, 0);
      }
    }
  }
  }

  shmdt(array); //Free memory

  return 0;
}
