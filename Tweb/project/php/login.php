<?php
/*Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: Questa è la pagina di login. Se l'autenticazione ha successo viene iniziata lessione*/
include("db.php");
if (isset($_REQUEST["name"]) && isset($_REQUEST["password"])) {
  $name = $_REQUEST["name"];
  $password = $_REQUEST["password"];
  if (is_password_correct($name, $password)) {
    if (isset($_SESSION)) {
      session_destroy();
      session_regenerate_id(TRUE);
      session_start();
    }
    /*Inizio sessione*/
    $_SESSION["name"] = $name; 
    redirect("http://tweb-lubiejewski.rhcloud.com/progetto/", "Login successful! Welcome back.");
  } else {
    redirect("http://tweb-lubiejewski.rhcloud.com/progetto/php/user.php", "Incorrect user name and/or password.");
  }
}
?>
