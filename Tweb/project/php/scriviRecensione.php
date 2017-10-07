<?php include("../top.php"); ?>
<?php include_once("db.php");
/* Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: In questa pagina viene mostrato all'utente un from per inserire il voto e il testo della 
    recensione della birra. Qunado l'utente conferma cliccando sul bottone di submit, viene
    rimandato sulla stessa pagina e viene eseguita la query per l'inserimento.*/
?>	
	<div class="col-md-6">
	  <div>
			<form action="scriviRecensione.php?birra=<?= $_GET["birra"] ?>&conferma='TRUE'" method="post"> 
		 		<div>
					<fieldset>
						<legend>Recensione <?= $_GET["birra"] ?>:</legend>
						<strong>Voto: </strong> 
						<select name="voto">
						<?php
						for($i=1; $i<11; $i++){
						?><option><?= $i ?></option>
						<?php
						}
						?>
						</select><br/>	
						<strong>Testo: </strong><br/> 
						<textarea name="testo" rows="10" cols="50"></textarea><br/><br/>
		      	  <input type="submit" value="Conferma" />
					</fieldset>
				</div>
			</form>
			</div>
	</div>
	<?php
	if(count(array_keys($_GET))>1){
    //Questa parte viene eseguita se prima l'utente ha premuto il bottone per inserire la recensione.
    //Se l'utente non ha ancora recensito la birra, allora la recensione vienne aggiunta, altrimenti,
    //la vecchia recensione viene sovrascritta
		$rows = insertRecensione($_SESSION["name"], $_GET["birra"], $_POST["testo"], $_POST["voto"]);
    if($rows){  
    ?>
      <p>Recensione inserita.</p>
    <?php
    }
    else{
      updateRecensione($_SESSION["name"], $_GET["birra"], $_POST["testo"], $_POST["voto"]);
    ?>
      <p>Recensione sovrascritta.</p>
    <?php
	  }
  }
	?>
<?php include("../bottom.html"); ?>
