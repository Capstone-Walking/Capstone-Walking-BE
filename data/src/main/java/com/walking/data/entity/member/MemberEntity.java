package com.walking.data.entity.member;

import com.walking.data.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "member")
@SQLDelete(sql = "UPDATE member SET deleted=true where id=?")
public class MemberEntity extends BaseEntity {

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String password;




}
