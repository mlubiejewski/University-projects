<?php
include_once("db.php");
/*Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa è la pagina che manda la risposta in XML alllo script che ha chiamato la 
    funzione AJAX. Si manda un messagio per informare se la birra è stata inserita nei 
    preferiti con successo.*/

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
//Si esegue la query di inserimento
$inserito = insertPreferiti($_SESSION["name"], $_GET["beer"]);
if($inserito)
	$msg = "Birra inserita";
else
	$msg = "Birra già nei preferiti";
header("Content-type: application/xml");
print "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
print "<esito>\n";
print "\t<beer>$msg</beer>\n";
print"</esito>\n";
?>
