BIN = mm

OBJ = hello.o

CC = gcc

CFLAGS = -g -Wall -MMD -D_REENTRANT

all: $(BIN) 

$(BIN): $(OBJ)
	$(CC) $(CFLAGS) -lm -lpthread -o $(BIN)   $(OBJ)

include $(wildcard *.d)

.PHONY : clean

clean:
	rm -f $(BIN) *.o *.d
	#rm -f *~ *.dat *.log \#*\# *.cache 
