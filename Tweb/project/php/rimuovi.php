<?php
include_once("db.php");
/*Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: In questa pagina si gestisce la rimozione della birra dai preferiti dopo che
    l'utente abbia premuto il relativo bottone*/


# main program
if (!isset($_SERVER["REQUEST_METHOD"]) || $_SERVER["REQUEST_METHOD"] != "GET") {
	header("HTTP/1.1 400 Invalid Request");
	die("ERROR 400: Invalid request - This service accepts only GET requests.");
}

$delay = 0;

if (isset($_REQUEST["delay"])) {
	# for debugging; makes the service delay for a given number of seconds to test 'loading' animations
	$delay = max(0, min(60, (int) filter_chars($_REQUEST["delay"])));
}

if ($delay > 0) {
	sleep($delay);
}
//Si esegue la query per rimuovere la birra
deletePreferiti($_SESSION["name"], $_GET["beer"]);
?>
