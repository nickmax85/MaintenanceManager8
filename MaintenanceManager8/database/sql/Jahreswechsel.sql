-- Aktuelle Stückzahlen der Anlagen und Stationen zu Jahresende zurücksetzen

set SQL_SAFE_UPDATES = 0;
set autocommit = 0;

start transaction;

-- damit der Wartungsfortschritt beim Zurücksetzen der aktuellen Stückzahlen bestehen bleibt
update anlage set lastWartungStueck=(aktuelleStueck-lastWartungStueck)*-1;
update station set station.lastWartungStueck=((select aktuelleStueck from anlage where anlage.id = station.anlageId)-lastWartungStueck)*-1;

update anlage set aktuelleStueck=0;

-- rollback
commit;

