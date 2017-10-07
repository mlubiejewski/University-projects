<?php 
	include_once("php/db.php");
  //Controllo se l'utente ha fatto il login, altrimenti lo si rimanda a user.php 
	ensure_logged_in();
?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa è la pagina top con menu, da includere in tutte le pagine visibili dopo
    il login. -->

<html>
	<head>
		<title>Progetto</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta charset="utf-8" />
		<!-- favicon -->
		<link rel="icon" href="favicon.ico" type="image/x-icon"/>
		<!-- instructor-provided JavaScript files; do not modify -->
		<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.7.0.0/prototype.js"
		  type="text/javascript"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/scriptaculous/1.9.0/scriptaculous.js" 
			type="text/javascript"></script>
		<!-- ajax -->
		<script src="http://tweb-lubiejewski.rhcloud.com/progetto/js/ajax_util.js" 
      type="text/javascript"></script>
		<!-- My JavaScript files -->
		<script src="http://tweb-lubiejewski.rhcloud.com/progetto/js/preferiti.js" 
      type="text/javascript"></script>
		<!-- Bootstrap -->
		<link href="http://tweb-lubiejewski.rhcloud.com/progetto/bootstrap-3.1.1-dist/css/bootstrap.min.css"
      rel="stylesheet" media="screen" />
		<link href="http://tweb-lubiejewski.rhcloud.com/progetto/css/stile.css" rel="stylesheet" />
		
	</head>
	<body>

		<div class ="container">

			<header>
			  <!-- Barra di menu orizzontale, con le voci del menu a sinistra e il bottone per il logout a destra -->
      	<nav class="navbar navbar-inverse">
    			<div class="navbar-header">
			      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar">
			        <span class="sr-only">Toggle navigation</span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			      </button>
		      </div>
		      <div id="navbar" class="navbar-collapse collapse">
  			    <ul class="nav navbar-nav">
		          <li><a href="http://tweb-lubiejewski.rhcloud.com/progetto/index.php">Home</a></li>
				      <li><a href="http://tweb-lubiejewski.rhcloud.com/progetto/php/cerca.php">Cerca</a></li>
				      <li><a href="http://tweb-lubiejewski.rhcloud.com/progetto/php/preferiti.php">Preferiti</a></li>
				      <li><a href="http://tweb-lubiejewski.rhcloud.com/progetto/php/recensioni.php">Recensioni</a></li>
			      </ul>
  			    <ul class="nav navbar-nav navbar-right">
				      <li><a href="http://tweb-lubiejewski.rhcloud.com/progetto/php/logout.php">
              <span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
		     	  </ul>
		      </div>
	      </nav>

      </header>
		
		
