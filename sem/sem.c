#include<stdio.h>
#include<sys/types.h>
#include<sys/ipc.h>
#include<unistd.h>
//#include <semaphore.h>
#include <sys/sem.h>
#include"./sem.h"
int getsem(int proj_id ,int n){
    key_t key = ftok(".",proj_id);
    int semid = semget(key,1,IPC_CREAT|0666);
    union semun semopts;
    semopts.val = n;
    semctl(semid,0,SETVAL,semopts);
    return semid;
}

void wait(int semid){
     struct sembuf buf = {0,-1,0};
     semop(semid,&buf,1);
}
void signal(int semid){
    struct sembuf buf = {0,1,0};
    semop(semid,&buf,1);
}
