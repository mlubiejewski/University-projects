<?php include("top.html"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa è la pagina di registrazione, si usa un form per richiedere all'utente
    di digitare i suoi dati. Una volta premuto il pulsante Sign Up,
    viene mandato alla pagina signup-submit -->
<div>
	<form action="signup-submit.php" method="post"> 
 		<div>
			<fieldset>
				<legend>New User Signup:</legend>
				<strong>Name: </strong>
				<input type="text" name="name" size="16" /><br/>
				<strong>Gender: </strong> 
				<label><input type="radio" name="gender" value="M" /> Male</label>
	  			<label><input type="radio" name="gender" value="F" checked="checked" /> Female </label><br/>
				<strong>Age: </strong> 
				<input type="text" name="age" size="6" maxlength="2" /><br/>
				<strong>Personality Type: </strong> 
				<input type="text" name="personality" size="6" maxlength="4" />
				(<a href="http://www.humanmetrics.com/cgi-win/JTypes2.asp">Don't know your type?</a>)<br/>
				<strong>Favorite OS: </strong>
				<select name="os">
 					<option>Windows</option>
  					<option>Mac OS X</option>
  					<option selected="selected">Linux</option>
				</select><br/>	
				<strong>Seeking age: </strong>
				<input type="text" name="from_age" size="6" maxlength="2" placeholder="min" />
				to <input type="text" name="to_age" size="6" maxlength="2" placeholder="max" /><br/>
      				<input type="submit" value="Sign Up" />
			</fieldset>
		</div>
	</form>             
</div>

<?php include("bottom.html"); ?>
