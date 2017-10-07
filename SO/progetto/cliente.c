#include <stdio.h>
#include <sys/types.h>
#include <errno.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <signal.h>
#include "mytypes.h"

dati_agenti registro_agenti[MAX_AGENTI]; //registro agenti
int ind_registro_agenti=0; //indice registro agenti
int sigint=0;

void sigchild_handler(int s){
 signal(SIGCHLD, sigchild_handler);
 int status;
 int child;
 int i;
 while (child = wait(&status) > 0) {
     for(i=0; i<ind_registro_agenti; i++){
     	if(child == registro_agenti[i].pid)
         	registro_agenti[i].ag_status=DEAD;
    }
  }
}

void sigint_handler(int s){
 signal(SIGINT, sigint_handler);
 sigint=1;
}

int main(int argc, char *argv[]){
 char temp[100];
 const char s[2]=";";
 char *token;
 ris_cli risorse[MAX_RISORSE]; //risorse del cliente lette da file, struttura definita in mytypes
 reg regis; //struttura, definita nell'header mytypes, per inviare il messaggio di registrazione al banditore
 risorsa ris; //struttura, definita nell'header mytypes, per passare le informazioni necessarie al processo agente
 dereg der; //struttura, definita nell´header mytypes, per i messaggi di deregistrazione
 esito esit; //struttura, definita nell'header mytypes, per ricevere il messaggio di acquisizione della risorsa nel caso di vittoria di un'asta
 char *argvec[7]; //argomenti da passare al processo agente
 char fileacq[10]; //nome del file, in questo caso il PID del cliente, dove andrÃ  a scrivere l'eventuale acquisizione della risorsa
 FILE *f=fopen(argv[1], "r");
 int i=0, k=0, budget_tot, key, queue_id;
 int mobo_flag=1, cpu_flag=1, ram_flag=1, memory_flag=1;
 int attesa_band; //variabile che controlla lo scambio di messaggi banditore-cliente relativo all'allocazione dei TAO e al conseguente avvio dei processi agente
 int resto=0; //variabile da sommare al budget parziale
 if(f == NULL){
    printf("Errore nellÃ‚Â´aprire il file\n");
    exit(EXIT_FAILURE);
 }
 fgets(temp,6,f);
 sscanf(temp, "%d", &budget_tot);
 while((fgets(temp,100,f)) != NULL){
    token=strtok(temp, s);
    strcpy(risorse[i].nome, token);
    if(token != NULL){
        token=strtok(NULL, s);
        sscanf(token,"%d",&(risorse[i].qta));
    }
    i++;
 }
 //RICEZIONE E INVIO MESSAGGI
 key=ftok("banditore.c", 'x');
 if(key == -1){
        perror("ftok: ");
        exit(EXIT_FAILURE);
 }
 queue_id=msgget(key, 0);
 if(queue_id == -1){
        perror("msgget: ");
        exit(EXIT_FAILURE);
 }
 while(k<i){
     regis.type=REGISTRAZIONE;
     regis.pid=getpid();
     strcpy(regis.nome, risorse[k].nome);
     regis.status=REG;
     int status=0;
     msgsnd(queue_id, &regis, sizeof(reg)-sizeof(long), 0);
     if(status == -1){
        perror("msgsnd: ");
        exit(EXIT_FAILURE);
     } 
     k++;
 }
 int budget_parziale=budget_tot/i;
 //ATTESA MESSAGGI BANDITORE ED AVVIO PROCESSI AGENTE
 attesa_band=1;
 while(attesa_band){
     if(sigint){
        int ii;
        for(ii=0; ii<ind_registro_agenti; ii++){
     		if(registro_agenti[ii].ag_status == ALIVE)
			kill(registro_agenti[ii].pid, SIGKILL);
        }
	der.type=DEREGISTRAZIONE;
        der.pid=getpid();
	strcpy(der.nome, "fine");
        if(msgsnd(queue_id, &der, sizeof(dereg)-sizeof(long), 0) == -1)
		perror("msgsnd ");
        exit(EXIT_FAILURE);
     }
     int a;
     if(cpu_flag){	 
     	int cpu_bytes=msgrcv(queue_id, &ris, sizeof(risorsa)-sizeof(long), CPU, IPC_NOWAIT);
    	 if(cpu_bytes > 0){
		cpu_flag=0;
        	for(a=0; a<i; a++){
        		if(strcmp(risorse[a].nome, "CPU") == 0){
        			int n=fork();
        		if(n<0) 
        			perror("fork: ");
        		if(!n){
        			argvec[0]= "./agente";
        			char temp1[20];
        			sprintf(temp1, "%d", risorse[a].qta);
        			argvec[1]= temp1;
        			char temp2[20];
        			sprintf(temp2, "%d", ris.base_asta);
        			argvec[2]=temp2;
        			char temp3[20];
        			sprintf(temp3, "%d", ris.shm);
        			argvec[3]=temp3;
        			char temp4[20];
        			sprintf(temp4, "%d", ris.sem);
        			argvec[4]=temp4;
				registro_agenti[ind_registro_agenti].pid=getpid();
				registro_agenti[ind_registro_agenti].budget=budget_parziale+resto;
				registro_agenti[ind_registro_agenti].ag_status=ALIVE;
				strcpy(registro_agenti[ind_registro_agenti].risorsa, "CPU");
				ind_registro_agenti++;
				char temp5[6];
				sprintf(temp5, "%d", (budget_parziale+resto));
				argvec[5]=temp5;
        			argvec[6]= NULL;
        			execv("./agente", argvec);
        	       }
        	}
         }
      }
   }
   if(ram_flag){
     int ram_bytes=msgrcv(queue_id, &ris, sizeof(risorsa)-sizeof(long), RAM, IPC_NOWAIT);
     if(ram_bytes > 0){
	ram_flag=0;
        for(a=0; a<i; a++){
        	if(strcmp(risorse[a].nome, "RAM") ==0){
		        ram_flag=0;
        		int n=fork();
        		if(n<0) 
        			perror("fork: ");
        		if(!n){ 
        			argvec[0]= "./agente";
        			char temp1[20];
        			sprintf(temp1, "%d", risorse[a].qta);
        			argvec[1]= temp1;
        			char temp2[20];
        			sprintf(temp2, "%d", ris.base_asta);
        			argvec[2]=temp2;
        			char temp3[20];
        			sprintf(temp3, "%d", ris.shm);
        			argvec[3]=temp3;
        			char temp4[20];
        			sprintf(temp4, "%d", ris.sem);
        			argvec[4]=temp4;
				registro_agenti[ind_registro_agenti].pid=getpid();
				registro_agenti[ind_registro_agenti].budget=budget_parziale+resto;
				registro_agenti[ind_registro_agenti].ag_status=ALIVE;
				strcpy(registro_agenti[ind_registro_agenti].risorsa, "RAM");
				ind_registro_agenti++;
				char temp5[6];
				sprintf(temp5, "%d", (budget_parziale+resto));
				argvec[5]=temp5;
        			argvec[6]= NULL;
        			execv("./agente", argvec);
        		}
        	}
        }
    }
 }
  if(mobo_flag){
    int mobo_bytes=msgrcv(queue_id, &ris, sizeof(risorsa)-sizeof(long), MOBO, IPC_NOWAIT);
    if(mobo_bytes > 0){
	mobo_flag=0;
        for(a=0; a<i; a++){
        	if(strcmp(risorse[a].nome, "MOBO") == 0){
        		int n=fork();
        		if(n<0) 
        			perror("fork: ");
        		if(!n){ 
        			argvec[0]= "./agente";
        			char temp1[20];
        			sprintf(temp1, "%d", risorse[a].qta);
        			argvec[1]= temp1;
        			char temp2[20];
        			sprintf(temp2, "%d", ris.base_asta);
        			argvec[2]=temp2;
        			char temp3[20];
        			sprintf(temp3, "%d", ris.shm);
        			argvec[3]=temp3;
        			char temp4[20];
        			sprintf(temp4, "%d", ris.sem);
        			argvec[4]=temp4;
				registro_agenti[ind_registro_agenti].pid=getpid();
				registro_agenti[ind_registro_agenti].budget=budget_parziale+resto;
				registro_agenti[ind_registro_agenti].ag_status=ALIVE;
				strcpy(registro_agenti[ind_registro_agenti].risorsa, "MOBO");
				ind_registro_agenti++;
				char temp5[6];
				sprintf(temp5, "%d", (budget_parziale+resto));
				argvec[5]=temp5;
        			argvec[6]= NULL;
        			execv("./agente", argvec);
        		}
        	}
        }
    }
  }
  if(memory_flag){	 
     	int memory_bytes=msgrcv(queue_id, &ris, sizeof(risorsa)-sizeof(long), MEMORY, IPC_NOWAIT);
    	 if(memory_bytes > 0){
		memory_flag=0;
        	for(a=0; a<i; a++){
        		if(strcmp(risorse[a].nome, "MEMORY") == 0){
        			int n=fork();
        		if(n<0) 
        			perror("fork: ");
        		if(!n){
        			argvec[0]= "./agente";
        			char temp1[20];
        			sprintf(temp1, "%d", risorse[a].qta);
        			argvec[1]= temp1;
        			char temp2[20];
        			sprintf(temp2, "%d", ris.base_asta);
        			argvec[2]=temp2;
        			char temp3[20];
        			sprintf(temp3, "%d", ris.shm);
        			argvec[3]=temp3;
        			char temp4[20];
        			sprintf(temp4, "%d", ris.sem);
        			argvec[4]=temp4;
				registro_agenti[ind_registro_agenti].pid=getpid();
				registro_agenti[ind_registro_agenti].budget=budget_parziale+resto;
				registro_agenti[ind_registro_agenti].ag_status=ALIVE;
				strcpy(registro_agenti[ind_registro_agenti].risorsa, "MEMORY");
				ind_registro_agenti++;
				char temp5[6];
				sprintf(temp5, "%d", (budget_parziale+resto));
				argvec[5]=temp5;
        			argvec[6]= NULL;
        			execv("./agente", argvec);
        	       }
        	}
         }
      }
   }
    int acq_bytes=msgrcv(queue_id, &esit, sizeof(esito)-sizeof(long), getpid(), IPC_NOWAIT);
    if(acq_bytes > 0){
	if(esit.esito){
		sprintf(fileacq, "%d.log", getpid());
    		FILE *fc=fopen(fileacq, "a ");
    		if(fc == NULL){
    			printf("Errore nell'aprire il file!");
    			exit(EXIT_FAILURE);
    		}
    		fprintf(fc, "%s %d %d\n", esit.nome, esit.qta, esit.costo);
    		fflush(fc);
    		fclose(fc);
		int index;
		for(index=0; index<ind_registro_agenti; i++){
  			if(!(strcmp(esit.nome, registro_agenti[index].risorsa)))
				resto=registro_agenti[index].budget - esit.costo;
                }
    	}
        else{
          	int index;
		for(index=0; index<ind_registro_agenti; i++){
  			if(!(strcmp(esit.nome, registro_agenti[index].risorsa)))
				resto=registro_agenti[index].budget;
                }
        }
                
    }
    if(errno == EINVAL || errno == EIDRM) attesa_band=0;
 }
}
