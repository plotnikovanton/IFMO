#include <stdio.h>
#include <fcntl.h>

int main(int argc, char* argv[])
{
    if (argc != 4)
    {
        printf("ERROR - Usage arguments: input_file1 input_file2 output_file\n");
        return -1;
    }

    int file1 = open(argv[1], O_RDONLY);
    int file2 = open(argv[2], O_RDONLY);  
    
    if(file1 == -1 || file2 == -1)
    {
        printf("ERROR - input files not opened \n");
        return -1;
    }
    
    int output = open(argv[3], O_WRONLY | O_CREAT | O_TRUNC, 0644);

    if (output == -1)
    {
        printf("ERROR - output file not opened \n");
        return -1;
    }
    char ch;
    while (read(file1, &ch, 1)) 
   	    write(output, &ch, 1);
    close(file1);
    while (read(file2, &ch, 1))
    	write(output, &ch, 1);
    close(file2);
    close(output);
    return 0;
}
