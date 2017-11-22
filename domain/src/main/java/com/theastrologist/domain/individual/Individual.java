package com.theastrologist.domain.individual;

import com.theastrologist.domain.SkyPosition;
import com.theastrologist.domain.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Individuals")
public class Individual {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private User user;

	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "natal_theme_id")
	private SkyPosition natalTheme;

	public Individual() { }

	public Individual(String name) {
		this.name = name;
	}

	public Individual(String name, SkyPosition natalTheme) {
		this(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SkyPosition getNatalTheme() {
		return natalTheme;
	}

	public void setNatalTheme(SkyPosition natalTheme) {
		this.natalTheme = natalTheme;
	}
}
