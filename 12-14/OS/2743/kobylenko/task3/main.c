#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/types.h>

void start_process(pid_t *pid, char c, int delay) {
	*pid = fork();
	if (*pid == 0) {
		printf("[Start process] %c\n", c);
		sleep(delay);
		printf("[Stop process] %c\n", c);
		exit(0);
	}
}

void wait_process(pid_t pid, char c) {
	waitpid(pid, NULL, 0);
	printf("[Sync process] %c\n", c);
}

pid_t a, b, c, d, e, f, g, h, i, j, k;
int delay_process[11] = {1, 1, 3, 4, 1, 4, 1, 1, 1, 1, 1};

int main() {
	start_process(&a, 'a', delay_process[0]);
	start_process(&b, 'b', delay_process[1]);

	wait_process(a, 'a');
	wait_process(b, 'b');

	start_process(&c, 'c', delay_process[2]);
	start_process(&d, 'd', delay_process[3]);
	start_process(&e, 'e', delay_process[4]);

	wait_process(e, 'e');

	start_process(&f, 'f', delay_process[5]);
	start_process(&g, 'g', delay_process[6]);

	wait_process(g, 'g');

	start_process(&h, 'h', delay_process[7]);

	wait_process(c, 'c');
	wait_process(h, 'h');

	start_process(&i, 'i', delay_process[8]);

	wait_process(d, 'd');
	wait_process(i, 'i');

	start_process(&j, 'j', delay_process[9]);
	start_process(&k, 'k', delay_process[10]);

	wait_process(f, 'f');
	wait_process(j, 'j');
	wait_process(k, 'k');
	return 0;
}
