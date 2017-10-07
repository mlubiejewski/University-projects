<?php include("../top.php"); ?>
<?php include_once("db.php"); ?>

<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa è la pagina dedicata alla birra cliccata. Si visualizzano le informazioni
    prese dal DB, tra cui la descrizione, l'immagine, le recensioni. Inoltre è possibile aggiungere 
    la birra nei preferiti, e scrivere o aggiornare la recensione. -->

<div id="left">
	<?php
	//Questa funzione visualizza i dati della birra
	beer($_GET["Nome"]);
	?>
	</br>
	</br>
	<?php
	//Questa funzione visualizza le recensioni che la birra ha ricevuto
	recensioni($_GET["Nome"]);
	?>
</div>


<?php
/*In questa funzione si cerca la birra in base al nome, passato come parametro, e si stampa
le informazioni ad essa relative, contenute nel database.*/
function beer($nome) {
	$rows = selectBeer($nome);
	$voto = votoMedio($nome);
	foreach ($rows as $beer) {
	?>
		<div class="row"> 
			<div class="col-md-6">
				<p>
					Nome: <?= $beer["Nome"] ?><br/>
					Tipo: <?= $beer["Tipo"] ?><br/>
					Gradi: <?= $beer["Gradi"] ?>%<br/>
					Paese: <?= $beer["Paese"] ?><br/>
					Voto: <?= $voto ?><br/>
				</p>
				<p>
					Descrizione: <br/>
					<?= $beer["Descrizione"] ?>
				</p>
			</div>
			<div class="col-md-6" id="<?= $beer["Nome"] ?>">
				<div class="draggable">
					<img src="../img/<?= $_GET["Nome"] ?>.png" alt="<?= $_GET["Nome"] ?>"
					 width="200" height="200"><br/><br/>
				</div>
				<!-- Il bottone per aggiungere le birre non sarà visibile su schermi grandi-->
				<button class="aggiungi hidden-md hidden-lg hidden-xl"> Aggiungi </button>
				<button>
					<a href ="http://tweb-lubiejewski.rhcloud.com/progetto/php/scriviRecensione.php?birra=<?= $beer["Nome"] ?>">
						Recensisci
					</a>
				</button>

			</div>
		</div>
	<?php	
	}
}

/*In questa funzione si trovano e si visualizzano le recensioni che la birra ha ottenuto*/
function recensioni($nome) {
  $rows = selectRecensioni($nome);
	?>
	<h3>Recensioni</h3>
  <div id="recensioni">
	<?php
	foreach ($rows as $recensione) {
	?>
	 
		<div class="col-md-6">
			<div class="recensione">
       <?php
				  if($recensione["Voto"] > 5){
				  ?>
					  <img src="../img/ok.png" alt="ok" />
				  <?php
				  }
				  else{
				  ?>
					  <img src="../img/bad.png" alt="bad" />
				  <?php
				  } ?>
          <div class="recensione-in">
			      <p>
				      Utente: <?= $recensione["Utente"] ?>
				      <br/>
				      Voto: <?= $recensione["Voto"] ?><br/>
		      	</p>
			      <p>
				      <q><?= $recensione["Testo"] ?></q>
			      </p>
          </div> <!-- /.recensione.in -->
      </div><!-- /.recensione -->
		</div>
	<?php
	}
	?>
	</div><!-- /.recensioni -->
	<?php
}

/*In questa funzione si calcola il voto medio della birra, facendo la media tra i voti ricevuti
   nelle recensioni*/
function votoMedio($nome) {
	$rows = selectVotoMedio($nome);
	$voto = 0;
	foreach ($rows as $row){
		$voto = $row["votoMedio"];
	}
	$votoFormattato = number_format($voto, 1);
	return $votoFormattato;
}

include("../bottom-pref.html"); ?>
