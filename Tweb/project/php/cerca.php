<?php include("../top.php"); ?>
<?php include_once("db.php"); ?>
<?php include_once("cerca-submit.php"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: In questa pagina si visualizzano i form e i risulatati della ricerca.
    la ricerca può essere fatta per nome, tipo e paese. -->

<div class="col-md-9 col-sd-12">

  <h2> Cerca per: </h2>
  <!-- Ricerca per nome -->
	<div>
	  <form action="cerca.php" method="get"> 
		  <strong>Nome: </strong>
		  <input type="text" name="Nome" size="16" />
		  <input type="submit" value="Cerca" /><br/></br>		
		</form> 
	</div>
	<!--Ricerca per tipo -->
  <div>
	  <form action="cerca.php" method="get"> 
		  <strong>Tipo: </strong>
		  <input type="radio" name="Tipo" value="Bionda"
		  checked="checked" />Bionda
		  <input type="radio" name="Tipo" value="Rossa" />Rossa
		  <input type="radio" name="Tipo" value="Scura" />Scura	
		  <input type="submit" value="Cerca" /><br/><br/>
    </form> 
	</div>
	<!--Ricerca per Paese -->	
  <div>
	  <form action="cerca.php" method="get"> 
		  <strong>Paese: </strong> 
		  <select name="Paese" size="1">
			  <?php
			  //Stampo i paesi contenuti nel db
			  $query = "SELECT DISTINCT Paese FROM Birre ORDER BY Paese";
			  get_and_print_options($query, "Paese");
			  ?>
	          </select>	
    				<input type="submit" value="Cerca" /><br/></br>	
			</form> 
		</div>
		<?php
		if($_GET){
			$array = array_keys($_GET);//prendo la chiave dell'array $_GET
			//Questa parte stampa i risultati di ricerca
			?>
			<h3>Risultati per <?= $array[0] ?>: <?= $_GET[$array[0]] ?></h3>
			<br/>
			<?php
			cerca($array[0], $_GET[$array[0]]);/*eseguo la funzione in cerca-submit.php passando il campo
       di ricerca e il suo valore*/
			?>
				
			
		<?php
		}
/*In questa funzione si trovano i paesi da visualizzare nel menu a tendina*/
function get_and_print_options($query, $column) {
  foreach (perform_query($query) as $row) {
    $choice = $row[$column];
    ?>
    <option value="<?= $choice ?>"><?= $choice ?></option>
    <?php
  }
}
?>
</div>	        
<?php include("../bottom-pref.html"); ?>
