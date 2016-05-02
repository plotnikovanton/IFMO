#include <stdio.h> // for printf()
#include <fcntl.h>
// #include <stdlib.h>

void rewrite(char *file_input_name, int output)
{
    int input = open(file_input_name, O_RDONLY);
    if (input == -1)
    {
        printf("[ERROR] Cannot open file %s for reading\n", file_input_name);
        exit(0);
    }
    char character;
    while (read(input, &character, 1) > 0)
        write(output, &character, 1);

    close(input);
}

int main(int argc, char* argv[])
{
    if (argc != 4)
    {
        printf("[ERROR] Usage arguments: first_input_file second_input_file output_file\n");
        return -1;
    }

    int output = open(argv[3], O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (output == -1)
    {
        printf("[ERROR] Cannot open file %s for writing\n", argv[3]);
        return -1;
    }
    rewrite(argv[1], output);
    rewrite(argv[2], output);
    close(output);
    return 0;
}
