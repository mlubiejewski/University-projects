<?php include("top.html"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina si ringrazia l'utente  della registrazione effettuata, 
    e lo si invita ad effettuare il login per vedere i suoi match. -->
<div>
	<?php
	if($_POST){
	?>
		<h1>Thank you!</h1>
		<p>Welcome to NerdLuv, <?= $_POST["name"] ?>!</p>
		<p>Now <a href="matches.php">log in to see your matches!</a></p>
		<?php
		/*I dati dell'utente devono essere scritti in append sul file "singles.txt". */
		$line = implode(",", $_POST);
		$line .= "\n";
		file_put_contents("singles.txt", $line, FILE_APPEND);
	}
	else{
	?>
	<p>Please login before submit</p>
	<?php
	}
	?> 	
</div>

<?php include("bottom.html"); ?>
