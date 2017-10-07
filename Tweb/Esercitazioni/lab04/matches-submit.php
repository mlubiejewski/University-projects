<?php include("top.html"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina viene visualizzata la lista dei match,
		dell'utente che ha effettuato il login-->
<div>
	<h1>Matches for <?= $_GET["name"] ?> </h1>

	<div class="match">
	
	<?php
	/*1-Si leggono i dati dell'utente che ha effettuato il login
	2-Si scrorre il file e per ogni riga si verifica se sono soddisfatti i vari criteri,
		in caso affermativo si visualizzano i dati dell'utente nella lista di match ideali*/

	//1
	foreach(file("singles.txt", FILE_IGNORE_NEW_LINES) as $iterator){
		list ($name, $gender, $age, $type, $os, $from_age, $to_age) = explode(",", $iterator);
		if(strcmp($_GET["name"], $name)==0)
			list($user_name, $user_gender, $user_age, $user_type, $user_os, $user_from_age, $user_to_age)  = explode(",", $iterator);
	}
	//2
	foreach(file("singles.txt", FILE_IGNORE_NEW_LINES) as $iterator){
		list ($name, $gender, $age, $type, $os, $from_age, $to_age) = explode(",", $iterator);
		//Sesso opposto
		if(strcmp($user_gender, $gender)!=0){
			//Età compatibile
			if($user_from_age <= $age && $user_to_age >= $age){
				//Lo stesso sistema operativo
				if(strcmp($user_os, $os)==0){
					//almeno un'attrbuto di personalità uguale
					if($user_type[0] == $type[0] || $user_type[1] == $type[1] 
					|| $user_type[2] == $type[2] || $user_type[3] == $type[3]){
					?>
						<p>
							<img src="http://www.cs.washington.edu/education/courses/cse190m/12sp/homework/4/user.jpg"
							 alt="user" />
							<?= $name?>
						</p>
							<ul>
								<li><strong>gender: </strong><?= $gender ?> </li>
								<li><strong>age: </strong><?= $age ?> </li>
								<li><strong>type: </strong><?= $type ?> </li>
								<li><strong>OS: </strong><?= $os ?> </li>
							</ul>
					<?php
					}
				}
			}
		}
			
	}
	?>
	</div>
</div>

<?php include("bottom.html"); ?>
