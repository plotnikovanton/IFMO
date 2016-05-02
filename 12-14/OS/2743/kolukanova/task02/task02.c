/* read&write.c */
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>

#define BUFFER_SIZE	64

int main (int argc, char ** argv)
{
	int fd1;
	int fd2;
	int fh;
	ssize_t read_bytes;
	ssize_t written_bytes;
	char buffer[BUFFER_SIZE+1];
	if (argc < 4)
	{
		fprintf (stderr, "Too few arguments\n");
		exit (1);
	}

	fd1 = open (argv[1], O_RDONLY);
	if (fd1 < 0)
	{
		fprintf (stderr, "Cannot open first file\n");
		exit (1);
	}
	fd2 = open (argv[2], O_RDONLY);
	if (fd2 < 0)
	{
		fprintf (stderr, "Cannot open second file\n");
		exit (1);
	}	
	fh = creat(argv[3], S_IREAD|S_IWRITE);
	if (fh == -1)
	{
		fprintf (stderr, "Couldn't create output file\n");
		exit (1);
	}

	while ((read_bytes = read (fd1, buffer, BUFFER_SIZE)) > 0)
	{		
		written_bytes = write (fh, buffer, read_bytes);
		if (written_bytes != read_bytes)
		{
			fprintf (stderr, "Cannot write\n");
			exit (1);
		}
	}
	if (read_bytes < 0)
	{
		fprintf (stderr, "myread: Cannot read first file\n");
		exit (1);
	}

	while ((read_bytes = read (fd2, buffer, BUFFER_SIZE)) > 0)
	{		
		written_bytes = write (fh, buffer, read_bytes);
		if (written_bytes != read_bytes)
		{
			fprintf (stderr, "Cannot write\n");
			exit (1);
		}
	}
	if (read_bytes < 0)
	{
		fprintf (stderr, "myread: Cannot read second file\n");
		exit (1);
	}
	close (fd1);
	close (fd2);
	close(fh);
	exit (0);
}
