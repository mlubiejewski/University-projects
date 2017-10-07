/*Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: In questa esercitazione si crea una pagina con il gioco del 15, 
  facendo uso di javascript*/
"use strict";
//riga e colonna della cella vuota
var empty_line = 3;
var empty_col = 3;
//altezza e larghezza di singola cella in px
var CELL_SIZE = 100;
//numero righe e colonne del puzzle
var N = 4;

window.onload = function() {
    /*Questa funzione prende i <div> con i numeri delle celle, contenuti nella puzzlearea,
    aggiunge la classe e per ogni cella un id diverso per identificarla.
    Si posizionano le celle in modo ordinato e si adatta l'immagine di background.
    Si gestiscono inoltre gli eventi del click e passaggio del mouse sulla cella, 
    e del click sul bottone shuffle*/
	var cell = $("puzzlearea").childElements();
	var cont = 0;
	for(var i = 0; i < N; i++){
		for(var j = 0; j < N; j++){
			if(i!=empty_line || j!=empty_col ){
				cell[cont].id = "cell_" + i + "_" + j;
				cell[cont].className = "cell";
				//posizione cella
				cell[cont].style.left = (j * CELL_SIZE) + "px";
				cell[cont].style.top = (i * CELL_SIZE) + "px";
				//posizione immagine di background della cella
				cell[cont].style.backgroundPosition = "-" + (j * CELL_SIZE) + "px " + "-" + (i*CELL_SIZE) + "px";
				cell[cont].onclick = cellClick;
				cell[cont].onmouseover = cellColor;
				cont++;
			}
		}
	}
	$("shufflebutton").onclick = shuffleClick;
};

//click sulla cella
function cellClick() {
	if(mossa(this)){
	//Se la cella si è spostata si controlla se il giocatore ha vinto
		if(vittoria()){
			alert("Hai vinto!");
		}
	}
}
//Questa funzione restituisce la cella con la riga e colonna passati come parametro
function getCell(riga, colonna){
	var cell = $("cell_" + riga + "_" + colonna);
	if(cell){
		return cell;
	}
}

function mossa(cell){
	//Questa funzione gestisce le mosse, se sono disponibili
		var rigaCella = parseInt(cell.style.top, 10)/CELL_SIZE;
		var colonnaCella = parseInt(cell.style.left, 10)/CELL_SIZE;
		if(mossaDisponibile(rigaCella, colonnaCella)){
			//Scambio la posizione della cella selezionata con cella vuota
			cell.style.left = (empty_col * CELL_SIZE) + "px";
			cell.style.top = (empty_line * CELL_SIZE) + "px";
			//Aggiorna le informazioni della cella vuota
			empty_line = rigaCella;
			empty_col = colonnaCella;
			return true;
		}
		return false;
}

//Controllo se la cella selezionata è accanto alla cella vuota, in quel caso si può spostare
function mossaDisponibile(rigaCella, colonnaCella){
	if(colonnaCella == empty_col){
		if(rigaCella == (empty_line+1) || rigaCella ==(empty_line-1)){
			return true;
		}
	}
	if(rigaCella == empty_line){
		if(colonnaCella == (empty_col+1) || colonnaCella == (empty_col-1)){
			return true;
		}
	}
	return false;
}

function shuffleClick() {
	//Questa funzione sposta a caso i pezzi del puzzle, spostando un pezzo per volta
	var i = 0;
	while(i < 100){
		do{
		//Prendo a caso una riga e una colonna tra 0 e 3 compresi
			var r = parseInt(Math.random() * 4, 10);
	    		var c = parseInt(Math.random() * 4, 10);
		//riga e colonna non possono essere entrambe '3' perchè non c'è nessuna cella con quel id
		}while(r == 3 && c == 3); 
		var cell = getCell(r, c);
		if(mossa(cell)){ 
		//si incrementa solo se è stata eseguita una mossa, in questo modo verranno sicuramente eseguite 100 mosse
				i++;
		}
	}
}	

function vittoria(){
	//Si controlla se i pezzi del puzzle sono nella posizione giusta
	if(empty_line != 3 || empty_col != 3){
	//Se la cella vuota NON è l'ultimo elemento allora sicuramente le celle non sono ordinate
		return false;
	}
	//Si esegue il controllo per ogni cella
	for(var i = 0; i < N; i++){
		for(var j = 0; j < N; j++){
			if(i!=3 || j!= 3){
				var cell = getCell(i, j);
				//controllo se la cella è nella posizione giusta
				if(((parseInt(cell.style.top, 10)/CELL_SIZE) != i) || ((parseInt(cell.style.left, 10)/CELL_SIZE) != j)) {
					return false;
				}
			}
		}
	}
	return true;
}

function cellColor() {
//Questa funzione cambia il colore, del bordo e del numero della cella, passandoci sopra con il mouse,
//solo se la cella si può spostare.
	if(this.hasClassName("colorCell")){
		this.removeClassName("colorCell");
	}
	var r = parseInt(this.style.top, 10)/CELL_SIZE;
	var c = parseInt(this.style.left, 10)/CELL_SIZE;
	if(mossaDisponibile(r, c)){
		this.addClassName("colorCell");
	}
}
