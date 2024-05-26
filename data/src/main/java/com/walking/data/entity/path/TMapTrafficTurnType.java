package com.walking.data.entity.path;

public enum TMapTrafficTurnType {

//n e w s

    NorthOrSouth(211), // 직진
    East(212), // 동
    West(213), // 서
    ne(214), // 북동  2시방향
    nw(215), // 북서 10시방향
    se(216), // 남동 4시방향
    sw(217); // 남서 8시방향


    private final Integer num;

    TMapTrafficTurnType(Integer num) {
        this.num = num;
    }

    static public TMapTrafficTurnType findByNumber(Integer num){
        for (TMapTrafficTurnType turnType : TMapTrafficTurnType.values()){
            if(num == turnType.num){
                return turnType;
            }
        }
        throw new IllegalArgumentException("올바른 방향이 없습니다.");
    }



}
