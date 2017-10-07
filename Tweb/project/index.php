<?php include("top.php"); ?>
<!--Nome e congome: Mateusz Lubiejewski
    Corso: Tecnologie Web (6 cfu) - a.a. 2015/16
    Descrizione: Questa è la pagina principale del sito. Viene mostrata una slideshow con diverse 
    immagini che si alternano in automatico, oppure manualmente tramite i bottoni. Ci sono diversi 
    paragrafi sulla birra e una tabella con i valori nutrizionali.-->

<!-- slideshow -->
<div id="myCarousel" class="carousel slide" data-ride="carousel">
  <!-- Indicators -->
  <ol class="carousel-indicators">
    <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
    <li data-target="#myCarousel" data-slide-to="1"></li>
    <li data-target="#myCarousel" data-slide-to="2"></li>
    <li data-target="#myCarousel" data-slide-to="3"></li>
    <li data-target="#myCarousel" data-slide-to="4"></li>
  </ol>

  <!-- Wrapper for slides -->
  <div class="carousel-inner" role="listbox">
    <div class="item active">
      <img src="img/slideshow/slider1.jpg" alt="beer">
    </div>
    <div class="item">
      <img src="img/slideshow/slider2.jpg" alt="beer">
    </div>
    <div class="item">
      <img src="img/slideshow/slider3.jpg" alt="beer">
    </div>
	  <div class="item">
      <img src="img/slideshow/slider4.jpg" alt="beer">
    </div>
	  <div class="item">
      <img src="img/slideshow/slider5.jpg" alt="beer">
    </div>
	</div>
  <!-- Left and right controls -->
  <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
    <span class="sr-only">Successivo</span>
  </a>
  <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
    <span class="sr-only">Precedente</span>
  </a>
</div>
<!-- fine slideshow-->

<br />
<div class="col-md-8 col-sd-12">
  <!-- Paragrafi -->
  <h3>Birra</h3>
  <p>
	  La birra è una bevanda alcolica ottenuta dalla fermentazione di mosto a base di malto d'orzo, 
	  aromatizzata da luppolo.
  </p>

  <h3>Generalità</h3>
  <p>
    La birra è una delle più diffuse e più antiche bevande alcoliche del mondo. Viene prodotta 
    attraverso la fermentazione alcolica con ceppi di Saccharomyces cerevisiae o Saccharomyces 
    carlsbergensis di zuccheri derivanti da fonti amidacee, la più usata delle quali è il malto 
    d'orzo, ovvero l'orzo germinato ed essiccato, chiamato spesso semplicemente malto.
    <br />
    Vengono poi usati frumento, il mais, il riso - questi ultimi due specialmente come aggiunte in 
    birre di produzione industrali - e, in misura minore, l'avena, il farro, la segale. Altre piante 
    meno utilizzate sono invece la radice di manioca, il miglio e il sorgo in Africa, la patata in 
    Brasile e l'agave in Messico.
    <br />
    Per produrre la birra, il malto viene immerso in acqua calda dove, grazie all'azione di alcuni
    enzimi presenti nella radichetta che si forma durante la germinazione, gli amidi presenti vengono
    convertiti in zuccheri fermentescibili. Questo mosto zuccheroso può essere aromatizzato con erbe
    aromatiche, frutta o più comunemente con il luppolo. Successivamente viene impiegato un lievito
    che dà inizio alla fermentazione e porta alla formazione di alcool, unitamente ad anidride 
    carbonica, che viene per la maggior parte espulsa, ed altri prodotti di scarto derivanti dalla
    respirazione anaerobica dei lieviti.
    <br />
    In questo processo si utilizzano ingredienti, tradizioni e metodi produttivi diversi. Il tipo di
    lievito e il metodo di produzione possono essere usati per classificare le  birre in ale, lager
    o birre a fermentazione spontanea.
  </p>

  <h3>Storia</h3>
  <p>
    La birra è una delle bevande più antiche prodotte dall'uomo, probabilmente databile al settimo 
    millennio a.C., registrata nella storia scritta dell'antico Egitto e della Mesopotamia. La prima 
    testimonianza chimica nota è datata intorno al 3500-3100 a.C.. Poiché quasi qualsiasi sostanza 
    contenente carboidrati, come ad esempio zucchero e amido, può andare naturalmente incontro a 
    fermentazione, è probabile che bevande simili alla birra siano state inventate l'una 
    indipendentemente dall'altra da diverse culture in ogni parte del mondo. È stato sostenuto che 
    l'invenzione del pane e della birra sia stata responsabile della capacità dell'uomo di sviluppare 
    tecnologie e di diventare sedentario, formando delle civiltà stabili. È verosimile che la 
    diffusione della birra sia infatti coeva a quella del pane; poiché le materie prime erano le 
    stesse per entrambi i prodotti, era solo "questione di proporzioni": se si metteva più farina 
    che acqua e si lasciava fermentare si otteneva il pane; se invece si invertivano le quantità 
    mettendo più acqua che farina, dopo la fermentazione si otteneva la birra.
  </p>
</div>
<!-- Tabella che non viene mostrato sugli schermi piccoli. -->
<div class="col-md-4 hidden-sd hidden-xs">
  <dl>
	  <dt>Valori nutrizionali</dt>
	  <hr />
	  <dt>Quantità per 100 grammi</dt>
	  <dt>Calorie 43</dt>
	  <hr />
	  <dt>Grassi 0 g</dt>	
	  <dd>Acidi grassi saturi 0 g</dd>	
	  <dd>Acidi grassi polinsaturi 0 g</dd>	
	  <dd>Acidi grassi monoinsaturi 0 g</dd>	
	  <dt>Colesterolo 0 mg</dt>	
	  <dt>Sodio 4 mg</dt>	
	  <dt>Potassio 27 mg</dt>	
	  <dt>Carboidrati 3,6 g</dt>	
	  <dd>Fibra alimentare 0 g</dd>	
	  <dd>Zucchero 0 g</dd>	
	  <dt>Proteina 0,5 g</dt>
  </dl>
  <hr />
  <table>
    <tr>	
	    <td>Vitamina A &nbsp; &nbsp; &nbsp;</td>
	    <td> 0 IU &nbsp; &nbsp;</td>
	    <td>Vitamina C &nbsp; &nbsp; &nbsp;</td>	
	    <td> 0 mg </td>
    </tr>
    <tr>
	    <td>Calcio </td>	
	    <td> 4 mg </td>
	    <td>Ferro </td>
	    <td> 0 mg </td>
    </tr>
    <tr>
	    <td>Vitamina D </td>
	    <td> 0 IU</td>
	    <td>Vitamina B6 </td>
	    <td> 0 mg</td>
    </tr>
    <tr>
	    <td>Vitamina B12 </td>	
	    <td> 0 µg</td>
	    <td>Magnesio </td>	
	    <td> 6 mg</td>
    </tr>
  </table>
  <hr />
</div>
<?php include("bottom.html"); ?>
