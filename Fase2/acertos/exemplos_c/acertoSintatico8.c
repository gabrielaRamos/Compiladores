#include <stdio.h>

int main(){

	int i, counter, alist[10];
	i = 10;
	counter = 0;

	alist[0] = 20;
	alist[1] = 30;
	alist[2] = 40;
	alist[3] = 90;
	alist[4] = 50;
	alist[5] = 60;
	alist[6] = 70;
	alist[7] = 80;
	alist[8] = 100;
	alist[9] = 110;

	
	while( i <= 110){
		if(alist[counter] == i){
			printf("i is in the list: %d\n", i);
		}
		else {
			printf("i is not in the list: %d\n", i);
		}
		i = i * 3 - 10;
		counter = counter + 1;
	}
	return 0;
}