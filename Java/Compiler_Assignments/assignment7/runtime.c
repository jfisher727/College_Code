#include <stdio.h>
#include <stdlib.h>

int WORD_SIZE = 4;

void println (int n) {
   printf ("%d\n", n);
   /* flush?  A good idea for debugging */
}

void _alloc_object (int n) {
	malloc (n * WORD_SIZE);
}

void exit_program () {
	exit(0);
}
