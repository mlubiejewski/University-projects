#define MAX_RISORSE 30
#define MAX_PRODOTTI 30
#define MAX_NOME 30
#define MAX_CLIENTI 30
#define MAX_AGENTI 30
#define REGISTRAZIONE 112233
#define DEREGISTRAZIONE 445566
#define SEMCONTKEY 885577
#define CPU 778899
#define RAM 990011
#define MOBO 990022
#define MEMORY 990033
#define ROWS 5
#define COLS 3

typedef enum {REG, DEREG} status;
typedef enum {ALIVE, DEAD} ag_status;
typedef int Rows[COLS];
Rows *TAO;

int P (int semid, int semnum){
 struct sembuf cmd;
 cmd.sem_num = semnum;
 cmd.sem_op = -1;
 cmd.sem_flg = 0;
 return semop(semid, &cmd, 1);
}

int V (int semid, int semnum){
 struct sembuf cmd;
 cmd.sem_num = semnum;
 cmd.sem_op = 1;
 cmd.sem_flg = 0;
 return semop(semid, &cmd, 1);
}

//Struct per lo scambio di messaggi.

typedef struct{
	long type; //REGISTRAZIONE
	int pid;
	char nome[MAX_NOME];
	status status;
} reg;

typedef struct{
	long type; //RISORSA(CPU, RAM, MOBO)
	char nome[MAX_NOME];
	int shm;
	int sem;
	int base_asta;
} risorsa;

typedef struct{
	long type; //DEREGISTRAZIONE
	char nome[MAX_NOME];
	int pid;
} dereg;

typedef struct{
	long type; //PID CLIENTE
	char nome[MAX_NOME];
	int esito;
	int qta;
	int costo;
} esito;

//Struct di utilitÃ  per clienti e banditore.

typedef struct{
	char nome[MAX_NOME];
	int qta;
} ris_cli;

typedef struct{
	char nome[MAX_NOME];
	int qta;
	int base_asta;
} prod_ban;

typedef struct{
	int pid;
	int budget;
	ag_status ag_status;
	char risorsa[MAX_NOME];
} dati_agenti;

