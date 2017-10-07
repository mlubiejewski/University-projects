<!DOCTYPE html>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa esercitazione si aggiungono delle funzionalità dinamiche al sito 
    dell'esercitazione precedente utilizzando il linguaggio php. Si visualizza la pagina di 
    un determinato film in base al parametro passato nella URL.-->
<?php 
/*Si memorizza il nome del film, che corrisponde anche alla directory con i suoi file, 
in una variabile. Si salva in un array le principali informazioni del film */
$movie=$_GET["film"];
$info = file("$movie/info.txt", FILE_IGNORE_NEW_LINES);
?>
<?php function review($inizio, $fine, $recensioni){
/*per ogni file si leggono le righe in una lista, le righe contengono in ordine: testo recensione,
nome immagine, autore e pubblicazione. Si visualizzano al massimo 10 recensioni*/
	for($i = $inizio; $i<$fine and $i<10; $i++){
		list ($review, $image, $author, $pubblication) = file("$recensioni[$i]", FILE_IGNORE_NEW_LINES);
			$image = strtolower($image);
		?>
			<p>
				<img src="http://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/<?=$image?>.gif"
				 alt="<?=$image?>" />
			  <q><?= $review ?></q>
			</p>
			<p>
				<img src="http://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/critic.gif"
				 alt="Critic" />
				<?= $author ?> <br />
				<?= $pubblication ?>
			</p>
	<?php
	}
} 
?>
<html>
	<head>
		<title>Rancid Tomatoes</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="movie.css" type="text/css" rel="stylesheet" />
		<link rel="icon" href="http://courses.cs.washington.edu/courses/cse190m/11sp/homework/2/rotten.gif"
		 type="image/x-icon"/>
	</head>
	<body>
		<div id = "banner">
			<img src="http://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/banner.png"
			 alt="Rancid Tomatoes" />
		</div>
		<h1><?= $info[0] ?> (<?= $info[1] ?>)</h1> <!--Stampa del titolo e anno del film-->
		<div id = "main">
			<div id = "right">
				<img src="<?= $movie ?>/overview.png" alt="general overview" /> <!-- immagine del film -->
				<div id = "right-overview">
					<dl>
						<?php
						/*Per la parte Overview si legge il file overview.txt contenuto nella directory del film, 
						salvando le righe in un array. Si divide ogni riga in due parti che sono separate dal
						carattere ':' in modo da stampare le liste con definizioni*/
						foreach(file("$movie/overview.txt", FILE_IGNORE_NEW_LINES) as $line){
							list($dt, $dd)=explode(":", $line);
						?>
							<dt><?= $dt ?></dt>
							<dd><?= $dd ?></dd>
						<?php
						}
						?>
					</dl>
				</div>
			</div>
			<div id = "left">
				<div id = "rotten">
					<?php /*In base alla valutazione ricevuta dal film si mostra un'immagine Rotten
					se è minore del 60%, altrimenti si mostra l'immagine Fresh*/
					if($info[2]<60){
					?>
						<img src="http://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/rottenbig.png"
						 alt="Rotten" />
					<?php
					}
					else{
					?>
						<img src="http://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/freshbig.png"
						 alt="Fresh" />
					<?php
					}
					?>
					<span id = "percento"><?= $info[2]?>%</span>	<!-- stampa della valutazione -->
				</div>
				<?php
				/*Recensioni: Per un film ci sono diversi file di recensione, la prima metà delle recensioni
        deve stare nella colonna sinistra, la seconda nella colonna destra. Se c'è un numero 
				dispari, ci deve essere una recensione in più nella colonna di sinistra.*/
				$recensioni = glob("$movie/review*.txt");
				$n = count($recensioni);
				if((($n % 2) == 0) and $n <= 10)
					$meta = $n/2;
				else if($n<10)
					$meta = (int)($n/2) +1; //Approssimo per eccesso se è dispari
				else 
					$meta = 5;			
				?>
				<div id="colleft">
					<?php
					review(0, $meta, $recensioni);
					?>
				</div>
				<div id = "colright">
					<?php
					review($meta, $n, $recensioni);
					?>
				</div>
			</div>
			<div id = "footer">
				<?php
				if($n <= 10) //Il testo nel footer cambia se ci sono più di 10 recensioni
					$foot = "(1-$n) of $n";
				else	
					$foot = "(1-10) of $n";
				?>
				<p id = "last"><?= $foot ?></p>
			</div>
		</div>
		<div id = "validator">
			<a href="http://validator.w3.org/check/referer"><img src="http://www.cs.washington.edu/education/courses/cse190m/11sp/homework/2/w3c-xhtml.png"
			 alt="Validate HTML" /></a>
			<br/>
			<a href="http://jigsaw.w3.org/css-validator/check/referer"><img src="http://jigsaw.w3.org/css-validator/images/vcss"
			 alt="Valid CSS!" /></a>
		</div>
	</body>
</html>
