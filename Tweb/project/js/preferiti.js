/*Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione pagina: In questa pagina si gestisce il comportamento del sito, a seconda
  dell' interazione con i vari elementi */
"use strict";
var preferiti = [];
window.onload = function() {
  
	//Mando una richiesta Ajax per memorizzare in una array
	//le birre della lista preferiti dell'utente
	new Ajax.Request("http://tweb-lubiejewski.rhcloud.com/progetto/php/lista-preferiti.php", 
        	{
		    method: "GET",
		    onSuccess: listaPreferiti,
		    onFailure: ajaxFailed,
		    onException: ajaxFailed
        	}
	);
	//prendo tutti gli elementi di classe aggiungi e collego ogni elemento all'event handler del click e
	var aggiungi = document.getElementsByClassName("aggiungi");
	for(var i=0; i<aggiungi.length; i++){
		aggiungi[i].onclick = aggiungiPreferiti;
	}
	//tutti gli elementi di classe rimuovi e collego ogni elemento all'event handler del click
	var rimuovi = document.getElementsByClassName("rimuovi");
	for(var i=0; i<rimuovi.length; i++){
		rimuovi[i].onclick = rimuoviPreferiti;
	}
	
	//tutte le immagini da rendere trascinabili
	var draggables = document.getElementsByClassName("draggable");
	for(var i=0; i<draggables.length; i++){
		//Rendo le immagini delle birre trascinabili
    var trovato = false;
    for(var j=0; j<preferiti.length; j++){
      if(draggable[i].parentElement.id == preferiti[j]){
        trovato = true;
      }
    }
    if(!trovato){
		  new Draggable(draggables[i], {revert: true});
    }
	}
  //Rendo droppable l'are dei preferiti e faccio in modo che accetti gli elementi draggable
	if(document.getElementById("preferiti")){
		Droppables.add("preferiti", { 
		   	accept: "draggable",
		    	onDrop: aggiungiPreferiti2
	  });
	}
};

function listaPreferiti(ajax){
	var esito = ajax.responseXML.getElementsByTagName("beer");
	//Creo un array per memorizzare le birre preferite dell'utente
	
  for (var i = 0; i < esito.length; i++) {
    preferiti[i]  = esito[i].firstChild.nodeValue;
  }
}

//click sul bottone aggiungi ai preferiti
function aggiungiPreferiti() {
	new Ajax.Request("http://tweb-lubiejewski.rhcloud.com/progetto/php/aggiungi.php", 
        	{
		    method: "GET",
		    parameters: {beer: this.parentElement.id},
		    onSuccess: esito,
		    onFailure: ajaxFailed,
		    onException: ajaxFailed
        	}
	);
}

//aggiungi ai preferiti tramite drag&drop
function aggiungiPreferiti2(draggable) {
	new Ajax.Request("http://tweb-lubiejewski.rhcloud.com/progetto/php/aggiungi.php", 
	        {
	            method: "GET",
	            parameters: {beer: draggable.parentElement.id},
	            onSuccess: esito2(draggable),
	            onFailure: ajaxFailed,
	            onException: ajaxFailed
	        }
	);
}


//click sul bottone rimuovi dai preferiti
function rimuoviPreferiti() {
	new Ajax.Request("http://tweb-lubiejewski.rhcloud.com/progetto/php/rimuovi.php", 
        {
            method: "GET",
            parameters: {beer: this.parentElement.id},
            onSuccess: esitoRimuovi(this.parentElement),
            onFailure: ajaxFailed,
            onException: ajaxFailed
        }
    );
}

function esito(ajax){
	//elem.style.visibility = "hidden";
	var esito = ajax.responseXML.getElementsByTagName("beer");
   	for (var i = 0; i < esito.length; i++) {
		//var inseritoNode  = esito[i].getElementsByTagName("beer")[0];
		var inserito  = esito[i].firstChild.nodeValue;
		//var inserito = esito[i].getAttribute("inserito");
		var p = document.createElement("p");
		p.innerHTML = inserito;
		$("preferiti").appendChild(p);
    	}
}

function esito2(elem){
	if(document.getElementById("frigo")){
		//Faccio una copia del draggabble da aggiungere ai preferiti
		var drag = elem.cloneNode(true);
    //Tolgo la classe all'elemento, in modo che non posso essere tracinato una seconda volta
		elem.removeAttribute("class");
    //Ridimensiono l'immagine della birra e la posiziono nel frigo
		drag.down(0).style.width = "75px";
		drag.down(0).style.height = "75px";
    document.getElementById("frigo").appendChild(drag.down(0));
	}	
}

//Cancello la birra rimossa
function esitoRimuovi(elem){
  elem.remove();
}
