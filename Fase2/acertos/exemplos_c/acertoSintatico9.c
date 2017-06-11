#include <stdio.h>

int main(){

	int alist[10], passnum, i, iPlus, temp;
	short exchanges;

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

	exchanges = 0;
	passnum = 9;

	while( passnum > 0 && exchanges == 1 ){
		exchanges = 0;
		for( i = 0; i < passnum; i++ ){
			iPlus = i + 1;
			if( alist[i] > alist[iPlus] ){
				exchanges = 1;
				temp = alist[i];
				alist[i] = alist[iPlus];
				alist[iPlus] = temp;
			}
		}
		passnum = passnum - 1;
	}

	// Printa a lista alist inteira
	for(i = 0; i < (sizeof(alist) / sizeof(alist[0])); i++){
		printf("%d ", alist[i]);
	}
	printf("\n");

	return 0;
}