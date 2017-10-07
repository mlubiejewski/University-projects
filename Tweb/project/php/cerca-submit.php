<?php

/*Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa pagina contiene la funzione per la ricerca. */

function cerca($campo, $valore) {
/*In questa funzione di db.php si seleziona le t-uple delle birre in base al tipo di ricerca, contenuto nel parametro $campo,
con il valore cercato nel parametro $valore*/
  $rows = searchBeer($campo, $valore);
	?>
	<div id="beers" class="row">
	<?php
	foreach ($rows as $beer) {
  /*Si visualizza i risultati di ricerca. */
	?>
		<div class="col-md-4 col-sd-4 col-xs-6 beer" id="<?= $beer["Nome"] ?>">
			<a href ="http://tweb-lubiejewski.rhcloud.com/progetto/php/beer.php?Nome=<?= $beer["Nome"] ?>">
				<h4><?= $beer["Nome"] ?></h4>
			</a>
			<div class ="draggable">
				<img src="../img/<?= $beer["Nome"] ?>.png" alt="<?= $beer["Nome"] ?>" width="140" height="140">
			</div>
			<br/>
			<!-- Il bottone per aggiungere le birre sarà visibile sugli schermi piccoli-->
			<button class="aggiungi hidden-lg hidden-xl hidden-md" >Aggiungi</button>
		</div>
	<?php	
	}
	?>
	</div>
	<?php
}
?>
