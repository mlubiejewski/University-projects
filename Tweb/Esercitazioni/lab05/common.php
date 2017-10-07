<?php
define('DB_HOST',getenv('OPENSHIFT_MYSQL_DB_HOST'));
define('DB_PORT',getenv('OPENSHIFT_MYSQL_DB_PORT')); 
define('DB_USER',getenv('OPENSHIFT_MYSQL_DB_USERNAME'));
define('DB_PASS',getenv('OPENSHIFT_MYSQL_DB_PASSWORD'));
define('DB_NAME','imdb');

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
?>

<?php
function print_movies($query) {
    /*Si esegue la query passata come stringa nel parametro.
    Si stampa il risultato in una tabella*/
    $rows = perform_query($query);
    if ($rows->rowCount() > 0) {
        ?>
        <table>
		<tr>
			<td>#</td>
			<td>Title</td>
			<td>Year</td>

		</tr>
		<?php
		$cont=0;
		foreach ($rows as $row) {
			$cont++;
		?>
			<tr>
				<td><?= $cont ?></td>
		    		<td><?= $row["name"] ?></td>
				<td><?= $row["year"] ?></td>
			</tr>
		<?php
		}
		?>		
        </table>
    <?php
	return true;
    } 
    else return false;
}
?>

<?php
function actors($firstname, $lastname) {
/*In questa query si seleziona le t-uple degli attori che hanno lo stesso
nome e cognome dell'attore cercato. Si ordina per il numero dei film in ordine decresente, in caso
che 2 o più attori abbiano lo stesso numero di film, si ordina in ordine crescente per id.
In questo modo l'attore che ci interessa sarà presente nella prima riga del risultato della query*/
    $query =  "SELECT id, first_name, last_name, film_count
              FROM actors
              WHERE last_name = '" . $lastname . "'
              AND first_name LIKE '" . $firstname . "%'
	      ORDER BY film_count DESC, id";
    $rows = perform_query($query);
  
    if ($rows->rowCount() > 0) {
	$line = $rows->fetch();
	$actor = array("firstname" => $line["first_name"], "lastname" => $line["last_name"]);
	return $actor;
    } else return null;
}
?>
