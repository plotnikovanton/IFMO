#include <fcntl.h>

void writeToTheFile(char *file_input_path, int output){
	int input = open(file_input_path, O_RDONLY);
	char str;
	while(read(input, &str, 1) != 0){
		write(output, &str, 1);
	}
	close(input);
}
		
int main(int argc, char *argv[]){
	//argv[1]:first input file
	//argv[2]:second input file
	//argv[3]:output file
	int output = open(argv[3], O_WRONLY);
	writeToTheFile(argv[1], output);
	writeToTheFile(argv[2], output);
	close(output);
}
