<?php include("../top.html"); ?>
<!-- Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: In questa pagina si gestisce l'autenticazione dell'utente. Se l'utente è autenticato,
  si mostra il form di logout, altrimenti quello di login e reigistrazione.-->

	<?php if (isset($_SESSION["name"])) { ?>
	  <h2>User Status</h2>
	  <p>You are logged in as <?= $_SESSION["name"] ?>.</p>
    <!-- Form di logout -->
	  <?php include("../html/logout.html"); ?>         
	<?php } else { ?>
		<div class="col-md-6 col-sd-12">
      <!-- Form di login -->
		  <?php include("../html/login.html");
      //Se sono stato reindirizzato alla pagina stampo un messaggio
			if($_GET){
				$array = array_keys($_GET);
				if($array[0]){
					?>
					<br />					
					<p> <?= $_GET[$array[0]]?> </p>
			  <?php
				}
			} ?> 
		</div>
		<div class="col-md-6 col-sd-12">
      <!-- Form di registrazione -->
			<?php include("../html/signup.html"); ?>
		</div>        
	<?php 
  } ?>
<?php include("../bottom.html"); ?>
