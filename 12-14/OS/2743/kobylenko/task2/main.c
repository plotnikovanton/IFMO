#include <fcntl.h>
#include <stdio.h>

int main(int argc, char* argv[])
{
    if (argc != 4)
    {
        printf("Error: incorrect number of arguments\n");
        return -1;
    }
    int out = open(argv[3], O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (out == -1)
    {
        printf("Error: Cannot open file %s\n", argv[3]);
        return -1;
    }
    int in = open(argv[1], O_RDONLY);
    if (in == -1) {
        printf("Error: Cannot open file %s\n", argv[1]);
        return -1;
    }
    char x;
    while (read(in, &x, 1) > 0) {
        write(out, &x, 1);
    }
    close(in);
    in = open(argv[2], O_RDONLY);
    if (in == -1) {
        printf("Error: Cannot open file %s\n", argv[2]);
        return -1;
    }
    while (read(in, &x, 1) > 0) {
        write(out, &x, 1);
    }
    close(in);
    close(out);
    return 0;
}
