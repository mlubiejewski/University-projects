<?php include("../top.html"); ?>
<?php include_once("db.php"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina si informa l'utente se la registrazione ha avuto successo -->
<div>
	<?php
	if($_POST){
		//controllo se l'utente è stato inserito con successo
		if(insertUser($_POST["name"], $_POST["password"], $_POST["email"], $_POST["age"])){
	?>
			<h2>Benvenuto, <?= $_POST["name"] ?>!</h2>
			<h4>Ora effettua il log in!</h4>
			<br/>
			<div class="col-lg-6 col-sd-12">
			  <?php include("../html/login.html"); ?>
			</div>
			<?php
		}
		else{
			redirect("http://tweb-lubiejewski.rhcloud.com/progetto/php/user.php", "Utente già esistente! Registrazione non effettuata.");
		}
		
	}
	else{
		redirect("http://tweb-lubiejewski.rhcloud.com/progetto/php/user.php", "Inserisci i dati! Registrazione non effettuata.");
	}
	?> 	
</div>

<?php include("../bottom.html"); ?>
