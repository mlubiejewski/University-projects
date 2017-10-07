#include <stdio.h>
#include <dirent.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>

extern int errno;

int main(int argc, char* argv[]){
	if(argc < 3){
		printf("Errore, inserire il nome dell'eseguibile da caricare!\n");
	        exit(EXIT_FAILURE);
	}
	DIR *dir=opendir(argv[1]);
	char *argvec[3];
	struct dirent *dit;
	while((dit=readdir(dir)) != NULL){
		char *ris=strstr(dit->d_name, ".cl2");
		if(ris != NULL){
			int n=fork();
			if(!n){
				argvec[0]=argv[2];
				argvec[1]=dit->d_name;
				argvec[2]=NULL;
				execv(argv[2], argvec);
	                }
	        }
	}
	int childpid;
	while((childpid=wait(NULL))!=-1);
	if(closedir(dir)==-1) perror("closedir");
}
