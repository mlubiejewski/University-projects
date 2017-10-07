<?php include("../top.php"); ?>
<?php include_once("db.php"); ?>
<!-- Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: In questa pagina si visualizzano le birre nei preferiti dell'utente.-->

	<h3>La tua lista preferiti</h3>
	</br>
	<?php
	//Stampo le birre nei preferiti dell'utente
	$rows = selectPreferiti($_SESSION["name"]);
	?>
	<div class="row">
	<?php
	foreach ($rows as $preferiti) {
	?>
		<div id="<?= $preferiti["Birra"] ?>" class="col-md-4 beer" >
			<h4><?= $preferiti["Birra"] ?></h4>
			<a href ="http://tweb-lubiejewski.rhcloud.com/progetto/php/beer.php?Nome=<?= $preferiti["Birra"] ?>">
			<img class="img-circle" src="../img/<?= $preferiti["Birra"] ?>.png" alt="<?=$preferiti["Birra"]?>" width="140" height="140">
			</a><br/>
      <!--Bottone per rimuovere la birra dai preferiti -->
			<button class="rimuovi" >Rimuovi</button>
		</div><!-- /.col-md-4 -->
	<?php	
	}
	?>
	</div>
<?php include("../bottom.html"); ?>
