#include <stdio.h>
#include <sys/types.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include "mytypes.h"

int main(int argc, char *argv[]){
 printf("Sono il processo agente, il mio cliente e' %d, vuole %s, base asta %s, id shm %s, id sem %s, budget %s\n", getppid(), argv[1], argv[2], argv[3], argv[4], argv[5]);
 int shm_id, sem_id, base_asta, qta, budget;
 sscanf(argv[1], "%d", &qta);
 sscanf(argv[2], "%d", &base_asta);
 printf("%d\n", base_asta);
 sscanf(argv[3], "%d", &shm_id);
 sscanf(argv[4], "%d", &sem_id);
 sscanf(argv[5], "%d", &budget);
 TAO = (Rows*)shmat(shm_id, NULL, 0);
 int flag=1;
 int vuote=0, pid=0, temp_row, min=0, pos, offerta =1, flag_offerta =5, flag_budget = 0;
 int ret_sem;
 int row;
 
 while(flag){
	ret_sem=P(sem_id, 0);
	if(ret_sem != -1){
	 	for(row=0; row < ROWS; row++){
			if(TAO[row][0] == getppid())
				pid=1;	
	 	}
		if(!pid){
			for(row=0; row<ROWS && !vuote; row++){
				if(TAO[row][0] == -1){
					TAO[row][0]=getppid();
					TAO[row][1]=qta;
					TAO[row][2]=base_asta + row;
					vuote=1;
				}
			}
			if(!vuote){
				for(row=0; row<ROWS; row++){
					if((TAO[row][2]<=min ) || min==0){
						min=TAO[row][2];
						pos=row;
					}	
				}
				while(flag_offerta){
					flag_offerta =5;
					for(row=0; row<ROWS; row++){
						if((min +offerta) == TAO[row][2]){
							offerta++;
						}
						else
							flag_offerta--;
					}
				}
				if(min && TAO[pos][2]+ offerta < budget){
					TAO[pos][0]=getppid();
					TAO[pos][1]=qta;
					TAO[pos][2]=min + offerta;
				}
				else{
					flag =0;
				}
		       }

		}
		offerta = 1;		
		flag_offerta = 5;
		min = 0;
		pid=0;
		vuote=0;
		V(sem_id, 0);
	}
	else flag=0;
 }
 int shmdtch;
 if(shmdtch=shmdt(TAO) == -1) perror("shmdtch");
}
