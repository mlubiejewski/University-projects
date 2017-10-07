<?php include("top.html"); ?>
<?php include_once("common.php"); ?>
<h1>Results for <?= $_GET["firstname"] ?> <?= $_GET["lastname"] ?> </h1>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa pagina visualizza tutti i film fatti dall'attore e da Kevin Bacon.
    Oppure si visualizza un opportuno messagio di errore. -->
<?php
	/*Si salva in un array i dati dell'attore che sono restiuiti dalla funzione actors().
	Questo serve nel caso ci siano 2 o più attori con lo stesso nome e cognome,
	in questo modo si ottiene il nome dell'attore seguito da un numero d'ordine, ad esempio
	Chris Miller diventa Chris (III) Miller*/
	$actor = actors($_GET["firstname"], $_GET["lastname"]);
	if($actor){
		/*Questa query seleziona le tuple con L'attore cercato e Kevin Bacon, 
		che sono presenti in uno stesso film. Ordina le righe stampate per anno,
		in ordine decrescnte, e per titolo del film in modo crescente.*/ 
		$query = "SELECT m.name, m.year
		      FROM actors a
		      JOIN roles r ON a.id = r.actor_id
		      JOIN movies m ON m.id = r.movie_id
		      JOIN roles r2 ON m.id = r2.movie_id
		      JOIN actors a2 ON a2.id = r2.actor_id
		      WHERE a.first_name = '" . $_GET["firstname"] . "'
		      AND a.last_name = '" . $_GET["lastname"] . "'
		      AND a2.first_name = 'Kevin'
		      AND a2.last_name = 'Bacon' 
		      ORDER BY m.year DESC, m.name";
		?>
		<p>Films with <?= $_GET["firstname"] ?> <?= $_GET["lastname"] ?> and Kevin Bacon</p>
		<?php
		if(!print_movies($query)){
		?>
			<p><?= $_GET["firstname"] ?> <?= $_GET["lastname"] ?> wasn't in any films with Kevin Bacon.</p>
		<?php
		}
	}
	else{
	?>
		<p>Actor <?= $_GET["firstname"] ?> <?= $_GET["lastname"] ?> not found.</p>
	<?php
	}
?>

<?php include("bottom.html"); ?>
