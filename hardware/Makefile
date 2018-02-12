CC = gcc
CFLAGS = -Wall -Werror -std=gnu11
OBJ_SERVER = server/util.o server/server.o server/websocket.o server/http.o server/sha1.o server/base64.o
SHARED = -lpthread -lwiringPi

main: server
	$(CC) $(OBJ_SERVER)  main.c $(CFLAGS) $(SHARED) $(SHARED_MOD) -o MemeMixer

server: $(OBJ_SERVER)

server/%.o: server/%.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:

	rm server/*.o
	rm MemeMixer

