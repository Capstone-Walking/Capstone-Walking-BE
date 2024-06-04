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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"nt\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"et\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"st\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"wt\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"ne\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"se\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"sw\", \"trafficId\": ",@itstId), " }"),
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
        detail =concat( concat("{ \"apiSource\" :\"seoul\", \"direction\" : \"nw\", \"trafficId\": ",@itstId), " }"),
        point_value = ST_SRID(POINT(@lng, @lat), 4326),
        created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00'),
        updated_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');


CREATE SPATIAL INDEX idx_coordinates ON traffic (point_value);