<?php
/*Nome e congome: Mateusz Lubiejewski
  Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
  Descrizione: Questa è la pagina di logout. La sessione viene chiusa, e si viene reindirizzati
   alla pagina user.php*/
require_once("db.php");
/*if (!isset($_SESSION)) {
  session_start();
  if (isset($_SESSION["name"])) {
    unset($_SESSION["name"]);
  }
}*/
//la sessioen viene chiusa
session_destroy();
session_regenerate_id(TRUE);
session_start();
redirect("../php/user.php", "Logout successful.");
?>
