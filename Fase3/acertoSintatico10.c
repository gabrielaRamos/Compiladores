#include <stdio.h>
#include <string.h>
#include <math.h>
void main(){
   
   int a   [3   ]   ;   
   int x   ;   
   
   a = [1||0   , 1&&0   , !0];   

   for(x = 2; x > -1 ; x--){
      printf("%d ", a      [x      ]);      
      printf("\t");      
   }
   return    ;   
}