#include <stdio.h>
#include <dirent.h>
#include <errno.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <signal.h>
#include <unistd.h>
#include "mytypes.h"

#if defined(__GNU_LIBRARY__) && !defined(_SEM_SEMUN_UNDEFINED)
     /* union semun is defined by including <sys/sem.h> */
#else
     /* according to X/OPEN we have to define it ourselves */
        union semun {
                 int val;                  /* value for SETVAL */
                 struct semid_ds *buf;     /* buffer for IPC_STAT, IPC_SET */
                 unsigned short *array;    /* array for GETALL, SETALL */
                                           /* Linux specific part: */
                 struct seminfo *__buf;    /* buffer for IPC_INFO */
        };
#endif

extern int errno; //variabile esterna al programma per analizzare le situazioni di errore.
int asta_ctrl=0; //variabile globale che controlla il tempo di avvio dell'asta con l'ausilio del timer
int sigint=0;

void sigchld_handler(int s){
 int status;
 wait(&status);
}

void sigalrm_handler(int s){
 asta_ctrl=1;
}

void sigint_handler(int s){
 sigint=1;
}

int main(int argc, char *argv[]){
 if(argc <2){
    printf("Inserire il percorso del file Risorse_Banditore.ban\n");
    exit(EXIT_FAILURE);
 }
 signal(SIGCHLD, sigchld_handler);
 signal(SIGALRM, sigalrm_handler);
 signal(SIGINT, sigint_handler);
 union semun arg;
 union semun arg2;
 int row, col;
 char temp[100];
 const char s[2]=";";
 char *token;
 prod_ban prodotti[MAX_PRODOTTI];
 reg risorse[MAX_CLIENTI];
 dereg temp2;
 esito esit;
 FILE *f=fopen(argv[1], "r");
 int i=0,k=0, queue_id, msgkey, shmkey, semkey;
 if(f == NULL){
    printf("Errore nellÃ‚Â´aprire il file\n");
    exit(EXIT_FAILURE);
 }
 while((fgets(temp,100,f)) != NULL){
    token=strtok(temp, s);
    strcpy(prodotti[i].nome, token);
    if(token != NULL){
        token=strtok(NULL, s);
        sscanf(token,"%d",&(prodotti[i].qta));
    }
    if(token != NULL){
  	token=strtok(NULL, s);
        sscanf(token,"%d",&(prodotti[i].base_asta));
    }
    i++;
 }
 int j=0;
//RICEZIONE E INVIO MESSAGGI
 msgkey=ftok("banditore.c", 'x');
 queue_id=msgget(msgkey, 0642|IPC_CREAT);
 if(queue_id == -1){
        perror("msgget: ");
        exit(EXIT_FAILURE);
 }
 sleep(5); //Aspetto che i clienti mandino i messaggi di registrazione, quindi li registro.
 int a=1;
 while(a){
 	if(errno == ENOMSG)
 	      a=0;
        else{
 	      int byte=msgrcv(queue_id, &risorse[k], sizeof(reg)-sizeof(long), REGISTRAZIONE, IPC_NOWAIT);
 	      if(byte > 0) k++;
        }
        
 }
 int semcontid=semget(SEMCONTKEY, 1, IPC_CREAT|0642);
 if(semcontid == -1) perror("semget ");
 arg2.val=3;
 semctl(semcontid, 0, SETVAL, arg2);
 //ALLOCAZIONE TAO
 while(j<i){
	      a=1;
	      while(a){
 		     if(errno == ENOMSG)
 	      	      		a=0;
        	     else{
 	      	      		int byte=msgrcv(queue_id, &risorse[k], sizeof(reg)-sizeof(long), REGISTRAZIONE, IPC_NOWAIT);
 	      	      		if(byte > 0) k++;
                     }
        
              }
	      a=1;
	       while(a){
 		     if(errno == ENOMSG)
 	      	      		a=0;
        	     else{
 	      	      		int byte=msgrcv(queue_id, &temp2, sizeof(dereg)-sizeof(long), DEREGISTRAZIONE, IPC_NOWAIT);
 	      	    		int ind;
				for(ind=0; ind<k; ind++){
					if(temp2.pid == risorse[ind].pid)
						risorse[ind].status=DEREG;
				}
                     }
	      }
              if(sigint){
		     int sigcli;
		     for(sigcli=0; sigcli<k; sigcli++)
				risorse[k].status=DEREG;
		     msgctl(queue_id, IPC_RMID, NULL);
		     semctl(semcontid, 0, IPC_RMID, 0);
		     kill(0, SIGINT);
		     exit(EXIT_FAILURE); 
	      }
              P(semcontid, 0);
              int n=fork();
              if(n<0)
              	      perror("fork: ");
              if(!n){ //BANDITORE FIGLIO
                      shmkey=ftok("banditore.c", j);
                      if(shmkey == -1){
                      	      perror("ftok: ");
                      	      exit(EXIT_FAILURE);
                      }
                      int shmid=shmget(shmkey, ROWS*COLS*sizeof(int), IPC_CREAT|IPC_EXCL|0666);
		      if(shmid == -1){
                             perror("shmget: ");
                             exit(EXIT_FAILURE);
                      }
		      TAO = (Rows*)shmat(shmid, NULL, 0);
                      for (row = 0; row < ROWS; row++){
        		 	for (col = 0; col < COLS; col++){
            				TAO[row][col] = -1;
        		        }
    		      }
                      semkey=ftok("cliente.c", j);
                      if(semkey == -1){
                      	      perror("ftok: ");
                      	      exit(EXIT_FAILURE);
                      }
                      int semid=semget(semkey, 1, IPC_CREAT|IPC_EXCL|0666);
                      if(semid == -1){
                             perror("semget: ");
                             exit(EXIT_FAILURE);
                      }
                      arg.val=0;
                      semctl(semid, 0, SETVAL, arg);
                      int r;
                      for(r=0; r<k; r++){
                      	     risorsa ris;
                      	     int status;
                             if(!(strcmp(prodotti[j].nome, risorse[r].nome))){
                             	     if(!(strcmp(prodotti[j].nome, "CPU")) && risorse[r].status == REG){
                                   	   ris.type=CPU;
                                   	   strcpy(ris.nome, "CPU");
                                   	   ris.shm=shmid;
                                   	   ris.sem=semid;
                                   	   ris.base_asta=prodotti[j].base_asta;
                                   	   while((status=msgsnd(queue_id, &ris, sizeof(risorsa)-sizeof(long), 0))==-1 && errno == EINTR);
                                   	   if(status == -1){
                                   	   		 perror("msgsnd: ");
                                   	   		 exit(EXIT_FAILURE);
                                   	   }
                                      }  
                                      if(!(strcmp(prodotti[j].nome, "RAM")) && risorse[r].status == REG){
                                   	   ris.type=RAM;
                                   	   strcpy(ris.nome, "RAM");
                                   	   ris.shm=shmid;
                                   	   ris.sem=semid;
                                   	   ris.base_asta=prodotti[j].base_asta;
                                   	   while((status=msgsnd(queue_id, &ris, sizeof(risorsa)-sizeof(long), 0))==-1 && errno == EINTR);
                                   	   if(status == -1){
                                   	   		 perror("msgsnd: ");
                                   	   		 exit(EXIT_FAILURE);
                                   	   }
                                      }
                                    if(!(strcmp(prodotti[j].nome, "MOBO")) && risorse[r].status == REG){
                                   	   ris.type=MOBO;
                                   	   strcpy(ris.nome, "MOBO");
                                   	   ris.shm=shmid;
                                   	   ris.sem=semid;
                                   	   ris.base_asta=prodotti[j].base_asta;
                                   	   while((status=msgsnd(queue_id, &ris, sizeof(risorsa)-sizeof(long), 0))==-1 && errno == EINTR);
                                   	   if(status == -1){
                                   	   		  perror("msgsnd: ");
                                   	   		  exit(EXIT_FAILURE);
                                   	   }
                                   }
				    if(!(strcmp(prodotti[j].nome, "MEMORY")) && risorse[r].status == REG){
                                   	   ris.type=MEMORY;
                                   	   strcpy(ris.nome, "MEMORY");
                                   	   ris.shm=shmid;
                                   	   ris.sem=semid;
                                   	   ris.base_asta=prodotti[j].base_asta;
                                   	   while((status=msgsnd(queue_id, &ris, sizeof(risorsa)-sizeof(long), 0))==-1 && errno == EINTR);
                                   	   if(status == -1){
                                   	   		  perror("msgsnd: ");
                                   	   		  exit(EXIT_FAILURE);
                                   	   }
                                   }
				    
                             }	   
                       }
                                  
                      alarm(3);
                      while(!asta_ctrl); //aspetto che scada il timer per avviare l'asta (alzando il valore del semaforo precedentemente inizializzato a 0) PAUSE!!!
                      arg.val=1;
                      semctl(semid, 0, SETVAL, arg);
                      asta_ctrl=0;
                      alarm(4);
                      while(!asta_ctrl); //aspetto che scada il timer dell'asta effettiva
		      if(sigint){
		     		semctl(semid, 0, IPC_RMID, 0);
		                shmctl(shmid, IPC_RMID, NULL);
		                exit(EXIT_FAILURE); 
	       	      }
		      P(semid, 0);
		      semctl(semid, 0, IPC_RMID, 0);
                      int row, max_pid, max_qta, qta_ban=prodotti[j].qta, contatore=0,spesa=0, posizione=0;
		      float max=0.0;
		      esito esiti[ROWS];
		     while(qta_ban && contatore<ROWS){
		      	for(row=0; row<ROWS; row++){
					if(max<((float)TAO[row][2]/(float)TAO[row][1]) || max==0.0){
						max=(float)TAO[row][2]/(float)TAO[row][1];
					        spesa=TAO[row][2];
						max_pid=TAO[row][0];
						max_qta=TAO[row][1];
						posizione=row;
					}
		       }	
		       if(qta_ban>=max_qta){
				qta_ban-=max_qta;
				esit.type=max_pid;
				strcpy(esit.nome, prodotti[j].nome);
				esit.qta=max_qta;
				esit.esito=1;
				esit.costo=spesa;
				esiti[contatore]=esit;
		       }
		       else{
				esit.type=max_pid;
				strcpy(esit.nome, prodotti[j].nome);
				esit.qta=qta_ban;
				esit.esito=1;
				esit.costo=max*qta_ban;   				
				qta_ban=0;
				esiti[contatore]=esit;
				
		       }
		       contatore++;
		       TAO[posizione][0]=1;
		       TAO[posizione][1]=1;
		       TAO[posizione][2]=1;
		       max=0;
		      }
		      int ind_cli, ind_esiti, mandato=0;
		      for(ind_cli=0; ind_cli<k; ind_cli++){
				if(risorse[ind_cli].status == REG){
					for(ind_esiti=0; ind_esiti<contatore; ind_esiti++){
					  	if((risorse[ind_cli].pid == esiti[ind_esiti].type) && (!(strcmp(risorse[ind_cli].nome, esiti[ind_esiti].nome)))){
							if((msgsnd(queue_id, &esiti[ind_esiti], sizeof(esito)-sizeof(long), 0)) == -1){
								perror("msgsnd ");
								mandato=1;
							}
						}
					}
					if(!mandato){
						esit.type=risorse[ind_cli].pid;
						esit.qta=esit.costo=esit.esito=0;
						strcpy(esit.nome, risorse[ind_cli].nome);
						if((msgsnd(queue_id,  &esit, sizeof(esito)-sizeof(long), 0)) == -1)
							perror("msgsnd");
     					}
             			}
				mandato=0;
		      }					
                      int shm_ctl=shmctl(shmid, IPC_RMID, NULL);
		      if(shm_ctl == -1) printf("%s\n", strerror(errno));
                      V(semcontid, 0);
                      exit(EXIT_SUCCESS);
              }
	    j++;
            }
   int asp_child;
   while((asp_child=wait(NULL)) != -1);
   semctl(semcontid, 0, IPC_RMID, 0);
   int child_pid=0;
   while((child_pid=wait(NULL)) != -1);
   asta_ctrl=0;
   alarm(5);
   while(!asta_ctrl);	
   msgctl(queue_id, IPC_RMID, NULL);
}
