#include <stdio.h>
#include <fcntl.h>
#include <string.h>

int main(int argc, char* argv[])
{
    if (argc < 4)
    {
        printf( "usage: %s input1, input2, output\n", argv[0] );
        return -1;
    }
    char buf[1024];
    int input1 = open(argv[1], O_RDONLY);
    int input2 = open(argv[2], O_RDONLY);  
    if(input1 == -1 || input2 == -1)
    {
        printf("input file open error\n");
        return -1;
    }
    int output = open(argv[3], O_CREAT|O_WRONLY, 0644);
    if(output == -1)
    {
        printf("output file open error\n");
        return -1;
    }
    int bytesread = read(input1, buf, sizeof(buf));
    //printf("%s\n", buf);
    write(output, buf, bytesread);
    close(input1);
    bytesread = read(input2, buf, sizeof(buf));
    //printf("%s\n", buf);
    write(output, buf, bytesread);
    close(input2);
    close(output);
    return 0;
}
