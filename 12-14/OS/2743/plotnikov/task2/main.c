#include <stdio.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    if (argc < 4)
    {
        printf("too few arguments\n");
        return -1;
    }
    int in1 = open(argv[1], O_RDONLY);
    if (in1 == -1)
    {
        printf("error with open 1st source file\n");
        return -1;
    }
    int in2 = open(argv[2], O_RDONLY);
    if (in2 == -1)
    {
        printf("error with open 2nd source file\n");
        return -1;
    }
    int out = open(argv[3], O_WRONLY|O_CREAT|O_TRUNC, 0644);
    if (out == -1)
    {
        printf("error with open file to record\n");
        return -1;
    }

    ssize_t ret;
    char ch;
    while ((ret = read (in1, &ch, 1)) > 0)
        write(out, &ch, 1);
    close(in1);
    while ((ret = read (in2, &ch, 1)) > 0)
        write(out, &ch, 1);
    close(in2);
    close(out);

    return 0;
}
