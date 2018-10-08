#include<stdio.h>
#include"./sem.h"
#include<unistd.h>
int main(){
    int used_sem = getsem('z',0);
    int free_sem = getsem('q',3);
    int mutex = getsem('n',1);
    while(1){
        wait(used_sem);
        wait(mutex);
//        sleep(1);
        printf("消费一个资源,花费0.001秒\n");
        signal(mutex);
        signal(free_sem);
    }

}
