<?php include("top.html"); ?>
<?php include_once("common.php"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa pagina visualizza tutti i film interpretati dall'attore
    richiesto dall'utente, se questo è presente nel db. Altrimenti si informa l'utente che l'attore
    non è stato trovato. -->

<h1>Results for <?= $_GET["firstname"] ?> <?= $_GET["lastname"] ?> </h1>

<?php
/*Si salva in un array i dati dell'attore che sono restiuiti dalla funzione actors().
Questo serve nel caso ci siano 2 o più attori con lo stesso nome e cognome,
in questo modo si ottiene il nome dell'attore seguito da un numero d'ordine, ad esempio
Chris Miller diventa Chris (III) Miller*/
	$actor = actors($_GET["firstname"], $_GET["lastname"]);
	if($actor){
 	        /*Questa query seleziona le tuple con l'attore cercato.
		Ordina le righe stampate per anno, in ordine decrescnte,
		e per titolo del film in ordine crescente.*/ 
		$query = "SELECT m.name, m.year
         	       	 FROM actors a
            	 	 JOIN roles r ON a.id = r.actor_id
	     	 	 JOIN movies m ON m.id = r.movie_id
             	 	 WHERE a.first_name = '" . $actor["firstname"] . "'
              		 AND a.last_name = '" . $actor["lastname"] . "'
             	 	 ORDER BY m.year DESC, m.name";
		?>
		<p>All films</p>
		<?php
        	print_movies($query);
	}
	else{
	?>
		<p>Actor <?= $_GET["firstname"] ?> <?= $_GET["lastname"] ?> not found.</p>
	<?php
	}
?>
		
<?php include("bottom.html"); ?>


