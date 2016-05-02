#include <stdio.h>
#include <semaphore.h>
#include <pthread.h>

#define MAX_N 32
#define MAX_M 32

int i,j;
int matrixa[MAX_N][MAX_M];
int matrixb[MAX_N][MAX_M];
int matrixres[MAX_N][MAX_N];
int n, m;
sem_t mutex;

void read_matrix(int mtrx[MAX_N][MAX_M])
{
  printf ("Input matrix %d x %d please:\n", n, m);
  for (i=0; i<n; i++)
    for (j=0; j<m; j++)
      scanf("%d", &mtrx[i][j]); 
  printf ("Done.\n");
}

void *read_all()
{
  printf("---!!! READ ALL THREAD START !!!---\n");
  read_matrix(matrixa);
  read_matrix(matrixb);
  printf("---!!! READ ALL THREAD END !!!---\n");
  sem_post(&mutex);
  return 0;
}

void *sum_matrix() {
  printf("---!!! SUM MATRIX THREAD START !!!---\n");
  for (i=0; i<n; i++)
    for (j=0; j<m; j++)
      matrixres[i][j] = matrixa[i][j] + matrixb[i][j];
  printf("---!!! SUM MATRIX THREAD END !!!---\n");
  sem_post(&mutex);
  return 0;
}

void *print_marix() {
  printf("---!!! PRINT MATRIX THREAD START !!!---\n");
  for (i=0; i<n; i++)
  {
    for (j=0; j<m; j++)
      printf("%d ", matrixres[i][j]);
    printf("\n");
  }
  printf("---!!! PRINT MATRIX THREAD END !!!---\n");
  sem_post(&mutex);
  return 0;
}

int main(int argc, char **argv) 
{
  //freopen("input", "r", stdin);

  printf ("Input matrix size please:\n");
  scanf ("%d %d",&m ,&n);
  
  pthread_t threads[3];
  pthread_create (&threads[0], NULL, read_all, NULL);
  sem_wait(&mutex);
  pthread_create(&threads[1], NULL, sum_matrix, NULL);
  sem_wait(&mutex);
  pthread_create(&threads[2], NULL, print_marix, NULL);
  sem_wait(&mutex);

  return 0;
}
