<?php
/*Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina si gestisce il collegamento al DB del progetto e si eseguono le 
    query, per interrogare il DB. */

if (!isset($_SESSION)) session_start();
/*Definizione dei dati del DB 'progetto'*/
define('DB_HOST',getenv('OPENSHIFT_MYSQL_DB_HOST'));
define('DB_PORT',getenv('OPENSHIFT_MYSQL_DB_PORT')); 
define('DB_USER',getenv('OPENSHIFT_MYSQL_DB_USERNAME'));
define('DB_PASS',getenv('OPENSHIFT_MYSQL_DB_PASSWORD'));
define('DB_NAME','progetto');

/*Questa funzione esegue la query passata come parametro*/
function perform_query($query) {
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    try {
	  $db = new PDO($dsn, DB_USER, DB_PASS);
	  $rows = $db->query($query);
	  return $rows;
    } catch (PDOException $ex) {
		?>
		<p>Sorry, a database error occurred.</p>
		<?php
		return NULL;
	}
}


#Ritorna true se la password è corretta per quell'utente
function is_password_correct($name, $password) {
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);

    $name = $db->quote($name);
    $rows = perform_query("SELECT Password FROM Utenti WHERE Username = $name");
    if ($rows) {
		foreach ($rows as $row) {
		  $correct_password = $row["Password"];
		  return $password === $correct_password;
		}
    } else {
		return FALSE;   # utente non trovato
    }
}

/*In questa query si inserisce l'utente nei utenti registrati, se non era già presente nel DB*/
function insertUser($username, $password, $email, $age){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
  $db = new PDO($dsn, DB_USER, DB_PASS);
	$username = $db->quote($username);
	$password = $db->quote($password);
	$email = $db->quote($email);
	$age = $db->quote($age);
	$query = "INSERT INTO Utenti (Username, Password, Email, Eta) VALUES ($username,$password,$email,$age)";
	$rows = perform_query($query);
	if($rows){
		return true;	
	}
	else{
		return false;	
	}
}
/*In questa query si inserisce la birra nei preferiti dell'utente*/
function insertPreferiti($username, $beer){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$username = $db->quote($username);
	$beer = $db->quote($beer);
	$query = "INSERT INTO Preferiti (Utente, Birra) VALUES ($username,$beer)";
	$rows = perform_query($query);
	if($rows){
		return true;	
	}
	else{
		return false;	
	}
}
/*In questa query si inserisce la recensione della birra scritta dall'utente*/
function insertRecensione($username, $beer, $testo, $voto){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$username = $db->quote($username);
	$beer = $db->quote($beer);
	$testo = $db->quote($testo); 
	$query = "INSERT INTO Recensioni (Utente, Birra, Testo, Voto) VALUES ($username,$beer,$testo,$voto)";
	$rows = perform_query($query);
    return $rows;
}
/*In questa query si modifica la recensione della birra dell'utente*/
function updateRecensione($username, $beer, $testo, $voto){
    $dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$username = $db->quote($username);
	$beer = $db->quote($beer);
	$testo = $db->quote($testo);
    $query = "UPDATE Recensioni 
	         SET Testo = $testo, Voto= $voto
	         WHERE Utente = $username and Birra = $beer";
    perform_query($query);
}
/*In questa query si cancella una birra dai preferiti dell'utente*/
function deletePreferiti($username, $beer){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$username = $db->quote($username);
	$beer = $db->quote($beer);
	$query = "DELETE FROM Preferiti WHERE Utente = $username and Birra = $beer";
	perform_query($query);
}
/*In questa query si trovano e si selezionano i preferiti dell'utente*/
function selectPreferiti($username){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$user = $username;
    $user = $db->quote($user);
    $query =  "SELECT Birra
               FROM Preferiti
               WHERE Utente = $user";
    $rows = perform_query($query);
	return $rows;
}
/*In questa query si trovano e si selezionano le recensioni che la birra ha ottenuto*/
function selectRecensioni($beer){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$beer= $db->quote($beer);
    $query =  "SELECT Recensioni.Utente, Recensioni.Testo, Recensioni.Voto
			   FROM Recensioni join Birre on Recensioni.Birra = Birre.Nome
			   WHERE Birre.Nome = $beer";
     $rows = perform_query($query);
	return $rows;
}
//In questa query si selezionano le recensioni dell'utente
function recensioniUtente($user){
    $dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
	$user = $_SESSION["name"];
    $user = $db->quote($user);
    $query =  "SELECT Birra, Testo, Voto
			   FROM Recensioni
			   WHERE Utente = $user";
    $rows = perform_query($query);
    return $rows;
}

/*In questa query si seleziona le t-uple delle birre in base al tipo di ricerca, contenuto nel parametro $campo,
con il valore cercato nel parametro $valore*/
function searchBeer($campo, $valore){
    $dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
    $db = new PDO($dsn, DB_USER, DB_PASS);
    $valore = $db->quote($valore);
    $query =  "SELECT *
              FROM Birre
              WHERE $campo = $valore";
    $rows = perform_query($query);
    return $rows;
}

/*In questa query si seleziona i dati della birre passata come parametro*/
function selectBeer($beer){
	$dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
  $db = new PDO($dsn, DB_USER, DB_PASS);
  $beer = $db->quote($beer);
  $query =  "SELECT *
    FROM Birre
    WHERE Nome = $beer";
  $rows = perform_query($query);
	return $rows;
}

/*In questa query si seleziona il voto medio della birra, facendo la media tra i voti ricevuti nelle recensioni*/
function selectVotoMedio($beer){
  $dsn = 'mysql:dbname='.DB_NAME.';host='.DB_HOST.';port='.DB_PORT;
  $db = new PDO($dsn, DB_USER, DB_PASS);
  $beer = $db->quote($beer);
  $query =  "SELECT AVG(Recensioni.Voto) as votoMedio
    FROM Recensioni join Birre on Recensioni.Birra = Birre.Nome
    WHERE Birre.Nome = $beer
    GROUP BY Recensioni.Birra";
  $rows = perform_query($query);
  return $rows;
}

# Redirige la pagina corrente a user.php se l'utente non è autenticato.
function ensure_logged_in() {
  if (!isset($_SESSION["name"])) {
    redirect("http://tweb-lubiejewski.rhcloud.com/progetto/php/user.php", "You must log in before you can view the content of this site.");
  }
}

# Redirects current page to the given URL and optionally sets flash message.
function redirect($url, $flash_message) {
  if ($flash_message) {
    $_SESSION["flash"] = $flash_message;
  }
  # session_write_close();
  header("Location: $url?message=$flash_message");
  die;
}
?>
