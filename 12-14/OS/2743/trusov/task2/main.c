#include <stdio.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    if (argc < 4)
    {
        printf("%d arguments given: 3 expected\n", (argc - 1));
        return -1;
    }

    //source files
    int first = open(argv[1], O_RDONLY);
    int second = open(argv[2], O_RDONLY);

    //output file
    int output = open(argv[3], O_WRONLY|O_CREAT|O_TRUNC, 0644);

    //validation
    if (first == -1)
    {
        printf("%s is not a valid file\n", argv[1]);
        return -1;
    }
    if (second == -1)
    {
        printf("%s is not a valid file\n", argv[2]);
        return -1;
    }
    if (output == -1)
    {
        printf("%s is not a valid file\n", argv[3]);
        return -1;
    }

    ssize_t size;
    char buf;
    while ((size = read(first, &buf, 1)) > 0) 
        write(output, &buf, 1);
    close(first);
    while ((size = read(second, &buf, 1)) > 0)
        write(output, &buf, 1);
    close(second);

    close(output);
    
    return 0;
         
}
