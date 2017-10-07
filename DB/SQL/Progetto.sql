/*STUDENTI:
Mateusz Lubiejewski     777833
Mario Ramundo           781062
Simone Ricciardi        777848
*/



--Query 1:
--Elencare per ogni citta' il paese in cui si trova (il risultato deve contenere il nome della citta' e il nome del paese e
--essere ordinato alfabeticamente per citta').
select mondial.city.name as City, mondial.country.name as Country
from mondial.city join mondial.country on mondial.city.country = mondial.country.code
order by mondial.city.name asc;
/*COMMENTO: E' stato eseguito un join tra la tabella CITY e la tabella COUNTRY attraverso gli attributi CITY.Country e COUNTRY.Code.
In questo modo per ogni citta' abbiamo ricavato il nome del paese */

--Query 2:
--Elencare le citta' toccate da un lago (il risultato deve contenere soltanto il nome delle citta').
select mondial.located.city
from mondial.located
where mondial.located.lake is not null;
/*COMMENTO: Il risultato e' stato ottenuto tramite la tabella LOCATED, nella quale sono contenute le informazioni sulle citta' e i laghi*/

--CONTROLLO QUERY 2:
--Conto tutte le citta' non toccate da un lago
select mondial.located.city
from mondial.located
where mondial.located.lake is null;
--L'intersezione deve restituire l'insieme vuoto
select mondial.located.city
from mondial.located
where mondial.located.lake is not null
                    intersect
select mondial.located.city
from mondial.located
where mondial.located.lake is null;

--Si fa eccezione per le citta' diverse, con lo stesso nome, per le quali una si affaccia su un lago
--ed una no(Hamilton)

--Query 3:
--Elencare i paesi che hanno citta' con un nome che comprende la lettera y (il risultato deve contenere soltanto i nomi
--dei paesi).
select distinct mondial.country.name
from mondial.city join mondial.country on mondial.city.country = mondial.country.code
where mondial.city.name like '%y%' OR mondial.city.name like '%Y%';
/*COMMENTO: Si esegue un join tra la tabella CITY e la tabella COUNTRY tramite l'attributo CITY.Country e COUNTRY.Code.
Dopodiche' si controlla per ogni citta' che nel nome sia compresa una lettera y minuscola o maiuscola.*/

--CONTROLLO QUERY 3
--Seleziono le citta' che non hanno citta' con un nome che comprende la lettera y
select distinct mondial.city.name
from mondial.city 
where mondial.city.name not like '%y%' and mondial.city.name not like '%Y%';

--L'intersezione deve restituire l'insieme vuoto
select distinct mondial.city.name
from mondial.city
where mondial.city.name like '%y%' OR mondial.city.name like '%Y%'
                      intersect
select distinct mondial.city.name
from mondial.city
where mondial.city.name not like '%y%' and mondial.city.name not like '%Y%'; 
--Allora nella Query 3 ho selezionato solo i paesi (dopo aver effettuato un join)
--che hanno effettivamente citta' con la lettera y nel loro nome

--Query 4:
--Elencare le citta' che si trovano nei circoli polari artico e antartico, cioe' oltre 66 gradi di latitudine nord e sud (il
--risultato deve contenere soltanto i nomi delle citta').
select mondial.city.name
from mondial.city
where mondial.city.latitude > 66 OR mondial.city.latitude < -66;
/*COMMENTO: All'interno della tabella CITY si controlla la latitudine di ogni citta', in modo da selezionare le citta' che stiano al di fuori
dell'intervallo [-66,66] gradi di latitudine.*/

--CONTROLLO QUERY 4
--Seleziono le citta' che sono coprese tra i 66 gradi di latitudine nord e sud
select mondial.city.name
from mondial.city
where mondial.city.latitude > -66 and mondial.city.latitude < 66;

--L'intersezione tra le due query deve restituire l'insieme vuoto
select mondial.city.name
from mondial.city
where mondial.city.latitude > 66 OR mondial.city.latitude < -66
                  intersect
select mondial.city.name
from mondial.city
where mondial.city.latitude > -66 and mondial.city.latitude < 66;

/*Query 5:
Elencare per ogni lingua il numero di persone che la parlano (dalla lingua piu' parlata a quella meno parlata) (il risultato
deve contenere la lingua e il numero di parlanti).*/
select language.name, sum(language.percentage/100  * country.population)as somma
from mondial.language language join mondial.country country on language.country = country.code
group by language.name
order by somma desc;
/*COMMENTO: Il risultato e' stato ottenuto mediante un join tra la tabella COUNTRY e la tabella LANGUAGE tramite
LANGUAGE.Country e COUNTRY.Code. E' stato in seguito eseguito un raggruppamento per nome della lingua ed effettuata
una sum dopo aver calcolato la percentuale.*/

--Query 6:
--Elencare i paesi che confinano con l'Italia (il risultato deve comprendere soltanto il nome dei paesi).

--PRIMA VERSIONE
select mondial.country.name
from mondial.country join mondial.borders on mondial.country.code = mondial.borders.country2
where mondial.borders.country1 = 'I'

union

select mondial.country.name
from mondial.country join mondial.borders on mondial.country.code = mondial.borders.country1
where mondial.borders.country2 = 'I';
/*COMMENTO: Si esegue un join tra la tabella COUNTRY e la tabella BORDERS, prima tramite COUNTRY.Code = BORDERS.Country2, poi tramite 
COUNTRY.Code = BORDERS.Country1, in modo da ottenere i paesi confinanti con l'Italia poiche' BORDERS non e' una tabella simmetrica.
Si e' eseguita dunque una UNION tra le due tabelle ottenute.*/
--SECONDA VERSIONE
select distinct mondial.country.name
from mondial.country join mondial.borders on mondial.country.code = mondial.borders.country1
                                          OR mondial.country.code = mondial.borders.country2
where (mondial.borders.country1 = 'I' OR mondial.borders.country2 = 'I') AND mondial.country.name <> 'Italy';
/*COMMENTO: Invece di usare UNION abbiamo eseguito un join solo tramite la tabella COUNTRY e la tabella BORDERS, utilizzando gli attributi
COUNTRY.Code e BORDERS.Country1 in OR con COUNTRY.Code e BORDERS.Country2.*/

/*query7: Elencare le citta' che hanno piu' abitanti della capitale del loro paese (il risultato deve contenere il nome del paese, il
nome delle citta' e della capitale e il numero dei loro abitanti).
nel secondo join oltre il nome dela capitale si usa il codice dello stato, 
perche' ci possono essere piu' citta' con il nome della citta' capitale(ma non nello stesso paese) (es.London)*/
/*versione1*/
select country.name, city.name as city, city.population, capital.name as capital, capital.population
from mondial.city city join mondial.country country on city.country = country.code join mondial.city capital on 
country.capital = capital.name and country.code = capital.country
where city.population > capital.population;

/*COMMENTO: Si esegue un join tra la tabella CITY e la tabella COUNTRY tramite gli attributi CITY.Country e COUNTRY.Code, in modo da 
selezionare per ogni citta' il proprio paese, dopodiche' si esegue un altro join con la tabella CITY tramite gli attributi 
COUNTRY.Capital e CITY.Name e gli attributi COUNTRY.Code e CITY.Country (la seconda tabella CITY viene rinominata CAPITAL).
Infine si selezionano le citta' della prima tabella che hanno popolazione maggiore della popolazione della capitale del rispettivo paese.*/

/*versione2*/
select country.name, city.name city, city.population, capital.name as capital, capital.population
from mondial.city city join mondial.country country on city.country = country.code join mondial.city capital on country.capital = capital.name
and country.code = capital.country
where city.population >
  (select capital.population
  from mondial.city capital 
  where capital.country = city.country and capital.name = country.capital);
/*COMMENTO: In questa versione i join utilizzati sono analoghi a quelli della versione precedente.
Si e' utilizzata una query nidificata selezionando le varie popolazioni delle capitali. Infine si selezionano le citta' che hanno la 
popolazione maggiore della popolazione della loro capitale, restituita dalla sottoquery.*/
  
--CONTROLLO QUERY 7
--Seleziono le citta' che non hanno piu' abitanti della capitale del loro paese
select mondial.country.name as COUNTRY, mondial.city.name as CITY, mondial.country.capital, mondial.city.population
from (mondial.city join mondial.country on mondial.city.country = mondial.country.code) 
                   join mondial.city city2 on (city2.name = mondial.country.capital AND city2.country = mondial.country.code)
where mondial.city.population <= city2.population;

--L'intersezione tra le due query deve restituire l'insieme vuoto
select mondial.country.name as COUNTRY, mondial.city.name as CITY, mondial.country.capital, mondial.city.population
from (mondial.city join mondial.country on mondial.city.country = mondial.country.code) 
                   join mondial.city city2 on (city2.name = mondial.country.capital AND city2.country = mondial.country.code)
where mondial.city.population > city2.population
                  intersect
select mondial.country.name as COUNTRY, mondial.city.name as CITY, mondial.country.capital, mondial.city.population
from (mondial.city join mondial.country on mondial.city.country = mondial.country.code) 
                   join mondial.city city2 on (city2.name = mondial.country.capital AND city2.country = mondial.country.code)
where mondial.city.population <= city2.population;

/*query8: Calcolare (approssimativamente) il numero di abitanti per ogni continente (il risultato deve contenere il nome del
continente e il numero dei suoi abitanti espresso in milioni).*/
/*versione 1*/
select encompasses.continent, sum(population / 1000000) popolazione
from mondial.encompasses join mondial.country on (encompasses.country = country.code)
group by encompasses.continent;

/*COMMENTO: Il risultato e' stato ottenuto mediante un join tra la tabella COUNTRY e la tabella ENCOMPASSES 
(dove e' contenuta l'informazione sui continenti), tramite gli attributi ENCOMPASSES.Country e COUNTRY.Code,
ed un successivo raggruppamento per continenti. Si e' poi eseguito l'operatore aggregato sum sulle popolazioni.*/

/*versione 2*/
select mondial.encompasses.continent, sum(mondial.province.population)/1000000
from mondial.province join mondial.country on mondial.province.country = mondial.country.code
                      join mondial.encompasses on mondial.encompasses.country = mondial.country.code
group by mondial.encompasses.continent;

/*COMMENTO: In questa versione invece di utilizzare la popolazione della tabella COUNTRY, si e' utilizzato la popolazione ottenuta dalla 
tabella PROVINCE.*/

--Query 9:
--Elencare i paesi composti per almeno il 99% da isole (il risultato deve comprendere soltanto il nome del paese).

--PRIMA VERSIONE
select distinct mondial.country.name
from mondial.island join mondial.geo_island on mondial.geo_island.island = mondial.island.name
                    join mondial.country on mondial.country.code = mondial.geo_island.country
group by mondial.country.name, mondial.country.area
having sum(mondial.island.area) > ((mondial.country.area*99)/100)
order by mondial.country.name asc;

/*COMMENTO: Si esegue un join tra la tabella ISLAND e la tabella GEO_ISLAND tramite gli attributi ISLAND.Name e GEO_ISLAND.Island, 
successivamente si esegue un join con la tabella COUNTRY tramite gli attributi COUNTRY.Code e GEO_ISLAND.Country.
Si raggruppa in base al nome del paese(COUNTRY.Name) e all'area del paese(COUNTRY.Area).
Si controlla se la somma delle aree delle isole che compongono il paese siano maggiori del 99 % dell'area del paese stesso.*/

--SECONDA VERSIONE
select country.name
from mondial.country join mondial.locatedon on (country.code = locatedon.country) join mondial.island on (locatedon.island = island.name)
group by country.name, country.area
having sum(island.area) >= ((99/100) * country.area);

/*COMMENTO: In questa versione si utilizza la tabella LOCATEDON invece della tabella GEO_ISLAND. Si ottengono risultati
diversi a causa del riempimento diversificato tra le tabelle.*/

--CONTROLLO QUERY 9
--Seleziono i paesi che non sono composti per almeno il 99% da isole
select distinct mondial.country.name
from mondial.island join mondial.geo_island on mondial.geo_island.island = mondial.island.name
                    join mondial.country on mondial.country.code = mondial.geo_island.country
group by mondial.country.name, mondial.country.area
having sum(mondial.island.area) < ((mondial.country.area*99)/100)
order by mondial.country.name asc;

--L'intersezione tra le due query deve restituire l'insieme vuoto
select distinct mondial.country.name
from mondial.island join mondial.geo_island on mondial.geo_island.island = mondial.island.name
                    join mondial.country on mondial.country.code = mondial.geo_island.country
group by mondial.country.name, mondial.country.area
having sum(mondial.island.area) > ((mondial.country.area*99)/100)
                  intersect
select distinct mondial.country.name
from mondial.island join mondial.geo_island on mondial.geo_island.island = mondial.island.name
                    join mondial.country on mondial.country.code = mondial.geo_island.country
group by mondial.country.name, mondial.country.area
having sum(mondial.island.area) < ((mondial.country.area*99)/100);     

/*query 10: Trovare la capitale con piu' abitanti di tutte (con e senza operatore aggregato MAX) (il risultato deve contenere
soltanto il nome della capitale).*/
/*versione 1 senza max*/
select capital.name
from mondial.city capital join mondial.country country on capital.name = country.capital and capital.country = country.code
where capital.population >= all 
  (select capital2.population
  from mondial.city capital2 join mondial.country country2 on capital2.name = country2.capital and capital2.country = country2.code
  where capital2.population is not null);

/*COMMENTO: Versione senza operatore aggregato MAX. Si esegue un join tra la tabella CITY(rinominata CAPITAL) e la tabella COUNTRY tramite 
gli attributi CITY.Name e COUNTRY.Capital e CITY.Country e COUNTRY.Code. Dopodiche' si controlla che la popolazione della capitale presa
in considerazione nella query principale sia maggiore o uguale alla popolazione di ogni capitale ottenuta dalla sottoquery nella quale:
si esegue un join tra la tabella CITY e la tabella COUNTRY tramite gi attributi CITY.Name(rinominata CAPITAL2) e COUNTRY.Capital e gli 
attributi CITY.Country e COUNTRY.Code seleionando le citta' con popolazione non nulla. */

/*versione 2 con max*/
select capital.name
from mondial.city capital join mondial.country country on capital.name = country.capital and capital.country = country.code
where capital.population = 
  (select max(capital2.population)
  from mondial.city capital2 join mondial.country country2 on capital2.name = country2.capital and capital2.country = country2.code);
  
/*COMMENTO: Versione con operatore aggregato MAX. In questa versione i join sono analoghi alla versione precedente. La differenza consiste
nell'estrazione della capitale con popolazione maggiore nella sottoquery tramite l'utilizzo dell'operatore aggregato MAX. 
Si controlla dunque che la citta' della query esterna abbia popolazioe uguale alla popolazione della capitale restituita
dalla sottoquery. */ 
  
--CONTROLLO QUERY 10
--Elencando tutte le capitali in ordine di popolazione la prima deve essere quella ottenuta dalle query soprastanti

 select mondial.city.name
 from mondial.city join mondial.country on mondial.city.name = mondial.country.capital and mondial.city.population is not null
 order by mondial.city.population desc;
 
 /*Query 11:
Ricavare la seconda isola per estensione nel mondo (il risultato deve comprendere il nome dell'isola e la sua area).*/
/*versione 1*/
select name, area
from mondial.island
where area =
  (select max(area)
  from mondial.island
  where area <
    (select max(area)
    from mondial.island));

/*COMMENTO:Nelle due sottoquery si ottiene, tramite operatori aggregati MAX, la seconda isola per estensione trovando prima l'isola 
con area maggiore e poi l'isola maggiore per estensione ma che abbia area minore dell'isola precedentemente trovata.
Infine si ha un controllo sull'area in modo da identificare nella query esterna il nome dell'isola. */

/*versione 2*/
select name, area
from mondial.island
where area >= all
  (select area
  from mondial.island
  where area < any
    (select area
    from mondial.island
    where area is not null))
 minus 
(select name, area
from mondial.island
where area >= all
  (select area
  from mondial.island
  where area is not null));
  
/*COMMENTO: Nelle sottoquery della prima parte si selezionano le isole che hanno area minore di almeno un'altra isola. Si selezionano 
dunque le isole che hanno area maggiore o uguale a tutte le isole precedemtemente trovate (in questo modo venono trovate le prime due isole
per estensione). Nella seconda parte, con l'ausilio dell'operatore MINUS si sottrae al risultato ottenuto l'isola con maggiore area, mantenendo
dunque solo la seconda isola per estensione al mondo. */
  
--CONTROLLO QUERY 11
--Elenco le isole in ordine per estensione; la seconda isola deve essere il risultato delle query
--soprastanti
select mondial.island.name
from mondial.island
where mondial.island.area is not null
order by mondial.island.area desc;

--Query 12:
--Per ogni paese trovare la montagna piu' alta (con e senza operatore aggregato MAX) (il risultato deve contenere il
--nome del paese e il nome della montagna (o null se il paese non ha montagne)).

--PRIMA VERSIONE
select distinct country1.name as COUNTRY, mondial.mountain.name as MOUNTAIN
from mondial.mountain join mondial.geo_mountain on mondial.mountain.name = mondial.geo_mountain.mountain
                      right join mondial.country country1 on country1.code = mondial.geo_mountain.country
where mondial.mountain.height >= all
                      (select mondial.mountain.height
                       from mondial.mountain join mondial.geo_mountain on mondial.mountain.name = mondial.geo_mountain.mountain
                                             join mondial.country on mondial.country.code = mondial.geo_mountain.country
                       where country1.code = mondial.country.code); 
                       
/*COMMENTO: Si esegue un join tra la tabella MOUNTAIN e la tabella GEO_MOUNTAIN tramite gli attributi MOUNTAIN.Name e GEO_MOUNTAIN.Mountain.
Successivamente si esegue un right join (in modo da avere la partecipazione completa della tabella a destra) tra la tabella precedentemente 
ottenuta e la tabella COUNTRY tramite gli attributi COUNTRY.Code e GEO_MOUNTAIN.Country. Si controlla dunque che l'altezza della 
montagna considerata sia maggiore o uguale (MOUNTAIN.Height) a tutte le montagne ottenute tramite la sottoquery nella quale:
Si eseguono dei join analoghi alla query principale e si controlla che il paese delle due montagne sia lo stesso. */



--SECONDA VERSIONE
select distinct country.name, geo_mountain.mountain
from mondial.country country left join mondial.geo_mountain geo_mountain on country.code = geo_mountain.country left join mondial.mountain mountain on
geo_mountain.mountain = mountain.name
where geo_mountain.mountain is null or mountain.height = 
   (select max(mountain2.height)
    from mondial.mountain mountain2 join mondial.geo_mountain geo_mountain2 on mountain2.name = geo_mountain2.mountain
    where country.code = geo_mountain2.country);
    
/*COMMENTO: Logicamente i join sono uguali, ma cambiando l'ordine delle tabelle si sono eseguiti due left join in modo da ottenere la
partecipazione completa della tabella COUNTRY. Quello che cambia e' l'utilizzo nella sottoquery dell'operatore aggregato MAX
per estrarre la montagna di altezza maggiore per ogni paese. */
    
--CONTROLLO QUERY 12
--Elenco per ogni paese le montagne. Per ogni paese la montagna piu' alta
--deve essere quella che compare nelle query soprastanti
select mondial.country.name, mondial.mountain.name, mondial.mountain.height
from mondial.country left join mondial.geo_mountain on mondial.country.code = mondial.geo_mountain.country
                     left join mondial.mountain on mondial.mountain.name = mondial.geo_mountain.mountain
where mondial.mountain.height is not null                     
order by mondial.mountain.height desc;

--Query 13:
--Estrarre il paese con piu' fiumi del mondo e il numero di tali fiumi (il risultato deve comprendere il paese e il numero
--dei suoi fiumi).

--contare il numero di fiumi di un paese, usando count di river.name con join fra country e geo_river
--selezionare il paese che ha un numero di fiumi >= a tutti i risultati di prima
--Le due query danno riusltati diversi perche' i dati nel database non sono completi

--PRIMA VERSIONE

select country.name, count(distinct geo_river.river) as n_fiumi
from mondial.geo_river join mondial.country on geo_river.country = country.code
group by country.name, geo_river.country
having count(distinct geo_river.river) >= all
  (select count(distinct geo_river.river)
  from mondial.geo_river join mondial.country on geo_river.country = country.code
  group by mondial.country.name, geo_river.country);
  
/*COMMENTO: Si esegue un join tra la tabella GEO_RIVER e la tabella COUNTRY tramite gli attributi GEO_RIVER.Country e COUNTRY.Code.
In seguito si effettua un raggruppamente in base al nome del paese (COUNTRY.Name) e al paese dei fiumi (GEO_RIVER.Country) e si cerca il 
paese col numero di fiumi (ottenuto tramite operatore aggregato COUNT) maggiore di tutti:
nella sottoquery si cacola il numero di fiumi per ogni paese tramite un join e un raggruppamento analoghi a quelli della query principale. */
  
--SECONDA VERSIONE

select distinct country.name, count(distinct located.river)as n_fiumi
from mondial.country join mondial.located on (country.code = located.country)
group by country.name
having count(distinct located.river) >= all
     (  select count(distinct located.river)
        from mondial.country join mondial.located on (country.code = located.country)
        group by country.name );
        
/*COMMENTO: Si esegue un join tra la tabella COUNTRY e la tabella LOCATED tramite gli attributi COUNTRY.Code e LOCATED.Country.
In seguito si raggruppa per nome del paese (COUNTRY.Name) e si controlla che il numero dei fiumi (ottenuti tramite operatore aggregato
COUNT) del paese in questione si maggiore o uguale al numero di fiumi di tutti i paesi ottenuti della sottoquery nella quale tramite un join
e un raggruppamento analoghi a quelli della query principale si ottiene il numero di fiumi per ogni paese. */

--CONTROLLO QUERY 13
--Associo ad ogni paese il numero dei suoi fiumi. Ordinando per numero di fiumi deve 
--essere uguale il primo della lista coi risultati dellle query soprastanti
select mondial.country.name, count(distinct mondial.geo_river.river)as RIVERS 
from mondial.geo_river join mondial.country on mondial.geo_river.country = mondial.country.code
group by mondial.country.name;

select mondial.country.name, count (distinct located.river) as RIVERS
from mondial.country join mondial.located on mondial.country.code = mondial.located.country
where mondial.located.river is not null
group by mondial.country.name;

/*Query 14:
Estrarre le citta' cinesi che hanno piu' abitanti di ogni citta' italiana (con e senza operatore aggregato MAX) (il risultato
deve comprendere il nome della citta' e la sua popolazione).*/
/*versione1*/
select name, population
from mondial.city
where country ='TJ' and population > all
  (select population
   from mondial.city
   where country = 'I' and population is not null);
   
/*COMMENTO: Versione senza l'utilizzo dell'operatore aggregato MAX. Nella query principale vengono selezionate le citta' cinesi che abbiano popolazione maggiore di tutte le citt� italiane, le quali sono ottenute
attraverso la sottoquery scartando le citta' italiane con popolazione nulla. */

/*versione2*/
select name, population
from mondial.city
where country ='TJ' and population >
  (select max(population)
   from mondial.city
   where country = 'I');
 
/*COMMENTO:  Versione con l'utilizzo dell'operatore aggregato MAX. La query principale e' analoga a quella della query precedente e si scelgono le citt�
con polazione maggiore alla citta' con maggiore popolazione in Italia ottenuta dalla sottoquery. */

--CONTROLLO QUERY 14
--Elenco le citta' cinesi che non hanno piu' abitanti di ogni citta' italiana.
select mondial.city.name, mondial.city.population
from mondial.city
where mondial.city.country = 'TJ' and mondial.city.population <=
                          (select max(mondial.city.population)
                           from mondial.city
                           where mondial.city.country = 'I');
                           
--L'intersezione tra le query deve restituire l'insieme vuoto
select mondial.city.name, mondial.city.population
from mondial.city
where mondial.city.country = 'TJ' and mondial.city.population >
                          (select max(mondial.city.population)
                           from mondial.city
                           where mondial.city.country = 'I')
                           intersect
 select mondial.city.name, mondial.city.population
from mondial.city
where mondial.city.country = 'TJ' and mondial.city.population <=
                          (select max(mondial.city.population)
                           from mondial.city
                           where mondial.city.country = 'I');
                           
select mondial.city.name, mondial.city.population
from mondial.city
where mondial.city.country = 'TJ' and mondial.city.population >= 
                          (select mondial.city.population
                          from mondial.city
                          where mondial.city.country = 'I' and mondial.city.population >= all
                                                    (select mondial.city.population
                                                     from mondial.city
                                                     where mondial.city.country = 'I' and mondial.city.population is not null))
                            intersect 
  select mondial.city.name, mondial.city.population
from mondial.city
where mondial.city.country = 'TJ' and mondial.city.population <=
                          (select max(mondial.city.population)
                           from mondial.city
                           where mondial.city.country = 'I');
                           
/*Query 15:
Per ogni citta' statunitense estrarre la sua popolazione e la superficie dei laghi su cui eventualmente si affaccia (il
risultato deve comprendere la citta', la popolazione e l'area complessiva dei suoi laghi).*/
/*versione1*/
select city.name, city.population, sum(lake.area)
from mondial.city left join mondial.located  on city.name = located.city left join mondial.lake on located.lake = lake.name 
where city.country = 'USA'
group by city.name, city.population
order by city.name;

/*COMMENTO: Si esegue un left join tra la tabella CITY e la tabela LOCATED tramite gli attributi CITY.Name e LOCATED.City (in modo da ottenere la 
partecipazione totale della tabella CITY) e un left join tra la tabella cosi' ottenuta e la tabella LAKE tramite gli attributi LOCATED.Lake
e LAKE.Name selezionando le citta' statunitensi e raggruppando per nome della citta' e popolazione. Infine si effettua una somma tramite 
operatore aggregato SUM sull'area dei laghi. */

/*versione 2*/

select city.name as City, city.population, sum(lake.area) as Area
from mondial.city join mondial.located  on city.name = located.city join mondial.lake on located.lake = lake.name 
where city.country = 'USA' 
group by city.name, city.population
union 
select city.name, city.population, null
from mondial.city
where city.country = 'USA' and city.name not in
    (select located.city
    from mondial.located
    where located.country = 'USA' and located.lake is not null)
order by 3 desc nulls last; /*con la UNION per fare ORDER BY bisogna rinominare il campo da ordinare nello stesso modo in entrambe le query,
oppure si puu' sempre utilizzare il numero del campo della tabella risultato, in questo caso 3 che sta per l'area dei laghi o NULL.
NULLS LAST mette i null alla fine*/

/*Query 16 (facoltativa):
Elencare i paesi che non sono direttamente confinanti ma sono separati da un unico paese (il risultato deve
comprendere i nomi delle coppie di paesi).*/

/*versione1 simmetrica (es. Italy Germany; Germany Italy)*/

select c1.name as country1, c2.name as country2 -- selezione dei 2 paesi separati da un unico paese
from mondial.country c1 join mondial.borders b1 on c1.code = b1.country1 or c1.code = b1.country2 join
mondial.country c2 on c2.code <> b1.country1 and c2.code <> b1.country2
where b1.country1 in
    (select b2.country1
    from mondial.borders b2 
    where b2.country2 = c2.code
      union
    select b2.country2
    from mondial.borders b2 
    where b2.country1 = c2.code)
or b1.country2 in
    (select b2.country1
    from mondial.borders b2 
    where b2.country2 = c2.code
      union
    select b2.country2
    from mondial.borders b2 
    where b2.country1 = c2.code)
minus--si toglie i paesi confinanti
    (select c3.name, c4.name
    from mondial.country c3 join mondial.borders on c3.code = borders.country1 or c3.code = borders.country2 join
    mondial.country c4 on c4.code = borders.country1 or c4.code = borders.country2)
order by country1;

/*COMMENTO: Si esegue un join tra COUNTRY(ridenominato c1) e BORDERS(ridenominato b1) tramite gli attributi COUNTRY.Code e
BORDERS.Country1 oppure COUNTRY.Code e BORDERS.Country2. Dopodiche' si esegue un join con COUNTRY(ridenominato c2) tramite gli attributi
COUNTRY.Code diverso da BORDERS.Country1 e COUNTRY.Code diverso da BORDERES.Country2.
Successivamente si controlla che b1.Country1 oppure b2.Country2 stia in una delle 2 query:
1: Si prende la tabella BORDERS(ridenominata b2) e si seleziona b2.Country1 se: b2.Country2 = c2.code;
2: Si prende la tabella BORDERS(ridenominata b2) e si seleziona b2.Country2 se: b2.Country1 = c2.code.
Alla fine si tolgono i paesi confinanti.*/

/*versione 2 non simmetrica*/
select distinct country1.name as country1, country2.name as country2
from mondial.country country1 join mondial.country country2 on country1.code <> country2.code
      join mondial.borders borders1 on country1.code = borders1.country1 or country1.code = borders1.country2
      join mondial.borders borders2 on country2.code = borders2.country1 or country2.code = borders2.country2 
where country1.name > country2.name 
      and (borders1.country1 = borders2.country1 or borders1.country1 = borders2.country2 or borders1.country2 = borders2.country1 or
      borders1.country2 = borders2.country2)
minus--si toglie i paesi confinanti
    (select country1.name, country2.name
    from mondial.country country1 join mondial.borders borders on country1.code = borders.country1 or country1.code = borders.country2 join
    mondial.country country2 on country2.code = borders.country1 or country2.code = borders.country2);
    
/*Query 17 (facoltativa):
Elencare le coppie di fiumi che nascono in paesi confinanti e che sfociano in mari non adiacenti ma separati da un
unico mare (il risultato deve comprendere i nomi delle coppie di fiumi)*/

select distinct river1.name as fiume1, river2.name as fiume2
from mondial.river river1 join mondial.geo_source source1 on river1.name = source1.river
      join mondial.borders on source1.country = borders.country1 or source1.country = borders.country2
      join mondial.geo_source source2 on source2.country = borders.country1 or source2.country = borders.country2
      join mondial.river river2 on source2.river = river2.name
      join mondial.mergeswith merges1 on (river1.sea = merges1.sea1 and river2.sea <> merges1.sea2) or (river1.sea = merges1.sea2 and river2.sea <> merges1.sea1)
      join mondial.mergeswith merges2 on (river2.sea = merges2.sea1 and river1.sea <> merges2.sea2) or (river2.sea = merges2.sea2 and river1.sea <> merges2.sea1)
where river1.name > river2.name and source1.country<>source2.country and river1.sea <> river2.sea 
--Si mette river1.name > river2.name e non <> perche' cosi' non stampa coppie di fiummi uguali nell'ordine inverso (es Po, Enns; Enns,Po)
      and (merges1.sea1 = merges2.sea1 or merges1.sea1 = merges2.sea2 or merges1.sea2 = merges2.sea1 or merges1.sea2 = merges2.sea2)
minus--si toglie i fiumi che sfociano in mari adiacenti
    (select river1.name, river2.name
    from mondial.river river1 join mondial.mergeswith merges on river1.sea = merges.sea1 or river1.sea = merges.sea2 join
    mondial.river river2 on river2.sea = merges.sea1 or river2.sea = merges.sea2);

/*COMMENTO:
--1 prendo una coppia di fiumi
--2 per ogni fiume faccio il join con geo_source per trovare il paese in cui sorge
--3 faccio il join con borders per vedere se i due paesi sono confinanti
--4 per ogni fiume nella tabella river trovo il mare in cui sfocia
--5 per vedere che i fiumi non sfociano in mari adiacenti ma in mari separati da un unico mare, 
--  si fa come in query 16 usando la tabella MERGESWITH(non simmetrica come BORDERS)*/