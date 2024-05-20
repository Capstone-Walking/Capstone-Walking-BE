#신호등 기본 데이터 생성
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
detail = @itstId,
point_value = ST_SRID(POINT(@lng, @lat), 4326),
created_at = STR_TO_DATE(@regDt, '%Y-%m-%dT%H:%i:%s.000+00:00');
