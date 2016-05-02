#include <stdio.h>
#include <fcntl.h>

void main ( void )
{
  int f1 = open("file1", O_RDONLY);
  int f2 = open("file2", O_RDONLY);
  int out = open("out", O_WRONLY | O_CREAT);
  if (f1 == -1 || f2 == -1 || out == -1) {
    return;
  }
  char buf;
  while (read(f1, &buf, 1)) {
    write(out, &buf, 1);
  }
  while (read(f2, &buf, 1)) {
    write(out, &buf, 1);
  }
  close(f1);
  close(f2);
  close(out);
}
