<?php include("top.html"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina si chiede all'utente di effettuare il login, 
    l'utente inserisce il suo nome in un form, dopodichè viene mandato alla pagina
    dove può vedere i suoi match-->
<div>
	<form action="matches-submit.php" method="get"> 
 		<div>
			<fieldset>
				<legend>Returning User: </legend>
				<strong>Name: </strong>
				<input type="text" name="name" size="16" /> <br/>
				<input type="submit" value="View My Matches" />
			</fieldset>
		</div>
	</form>                                                        
</div>

<?php include("bottom.html"); ?>
