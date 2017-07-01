#include <stdio.h>
#include <string.h>
#include <math.h>
void printValues(int t, int b, int d[3]){
   printf("sum =");   
   printf("%d ",  + "%d ", t + d   [1   ]);   
   printf("\n");   
   printf("%d ",  * "%d ", d   [0   ] * b);   
   printf("\n");   
   return    ;   
}void main(){
   
   int a   ,b   ,c   ,d   [3   ]   ,t   ;   
   
   a = 9;   
   b = 5;   
   c = 4;   
   d =    [1   ,2   ,3   ];   
   t = a + b;   
   t = t * c / 2;   
   t = t + 2;   
printValues   (   [t   ,b   ,d   ]   );   return    ;   
}