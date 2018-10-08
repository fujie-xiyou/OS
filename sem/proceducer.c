#include<stdio.h>
#include"./sem.h"
#include<unistd.h>
int main(){
    int used_sem = getsem('z',0);
    int free_sem = getsem('q',3);
    int mutex = getsem('n',1);
    while(1){
        wait(free_sem);
        wait(mutex);
        sleep(2);
        printf("生产一个资源,花费2秒\n");
        signal(mutex);
        signal(used_sem);
    }
}
