update anlage set jahresStueck = null, lastWartungStueck = null, aktuelleStueck = null
WHERE lastWartungId is null;

update station set lastWartungStueck = null
WHERE lastWartungId is null;


