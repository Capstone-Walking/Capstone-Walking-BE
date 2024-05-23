LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"nt\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');


LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"et\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');

LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"st\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');

LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"wt\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');

LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"ne\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');

LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"se\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');

LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"sw\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');

LOAD DATA INFILE '/var/lib/mysql-files/base-traffic-info.csv'
    IGNORE
    INTO TABLE api.traffic
    FIELDS
    TERMINATED BY ','
    LINES
    TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@itstId, @itstNm, @lat, @lng, @regDt)
    SET
        name = @itstNm,
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"nw\", \"id\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');