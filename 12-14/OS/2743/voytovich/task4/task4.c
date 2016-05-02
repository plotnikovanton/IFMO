#include <sys/wait.h>
#include <sys/types.h>

int main(){
	
	const int vertexes = 7;
	const int edge = 11;
	
	int edges[11][2] = { {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, 
			{1, 3}, {1, 4}, {1, 5}, {2, 6}, {5, 6} };
	pid_t pids[11];
	int i;	
	for(i = 0;i < vertexes; i++){
		
		int k;
		for(k = 0; k < edge; k++){
			waitpid(pids[k], 0, 0);//this process will wait
		}
		k = 0;
		for(k = 0; k < edge; k++){
			pids[k] = fork();//start proceses
			if(pids[k] == 0){
				sleep(edges[k][1] - edges[k][0]);//if this proces is a childred - wait
				return 0;
			}
		}	
	}
	return 0;
} 
