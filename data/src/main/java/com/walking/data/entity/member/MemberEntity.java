package com.walking.data.entity.member;

import com.walking.data.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "member")
@SQLDelete(sql = "UPDATE member SET deleted=true where id=?")
public class MemberEntity extends BaseEntity {

	/* 소셜 로그인을 통해 가입한 회원의 닉네임 */
	@Column(nullable = false, unique = true, length = 50)
	private String nickName;

	/* 소셜 로그인을 통해 가입한 회원의 프로필 이미지 URL */
	@Column(nullable = false)
	private String profile;

	/* 소셜 로그인을 통해 가입한 회원의 식별자 */
	@Column(nullable = false, unique = true)
	private String certificationId;

	/* 소셜 로그인을 통해 가입한 회원의 인증 주체 */
	@SuppressWarnings("FieldMayBeFinal")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(nullable = false)
	private CertificationSubject certificationSubject = CertificationSubject.KAKAO;

	@SuppressWarnings("FieldMayBeFinal")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(nullable = false)
	private MemberStatus status = MemberStatus.REGULAR;

	@Builder.Default
	@Column(nullable = false, columnDefinition = "json")
	private String resource = "{}";

	public MemberEntity(String nickName, String profile, String certificationId) {
		this.nickName = nickName;
		this.profile = profile;
		this.certificationId = certificationId;
	}

	public MemberEntity withDrawn() {
		this.status = MemberStatus.WITHDRAWN;
		return this;
	}

	public MemberEntity updateProfile(String profile) {
		this.profile = profile;
		return this;
	}
}
