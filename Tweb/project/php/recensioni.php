<?php include("../top.php"); ?>
<?php include_once("db.php"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina si visualizzano le recensioni che l'utente ha scritto. -->	

	<h3>Le tue recensioni</h3>
	</br>
	<?php
  //In questa funzione in db.php si trovano le recensioni dell'utente
  $rows = recensioniUtente($_SESSION["name"]);
  //Visualizzo le recensioni
	?>
	<div id="recensioni">
	<?php
	foreach ($rows as $recensione) {
	?>
	  <div class="col-lg-4 col-md-6 col-sd-12">
      <div class="recensione">
      <?php
      /*Se il voto è superiore a 5, si visualizza l'immagine 'ok', altrimente 'bad' */
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
			      <a href="http://tweb-lubiejewski.rhcloud.com/progetto/php/beer.php?Nome=<?= $recensione["Birra"] ?>"><?= $recensione["Birra"] ?></a>
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
<?php include("../bottom.html"); ?>
