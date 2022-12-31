package ru.edu.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Warrior implements Serializable {

	@JsonAlias("name")
	private String name;

	@JsonAlias("title")
	private String title;

	@JsonAlias("number")
	private Integer hp;

	private Boolean isAlive;

	private static final long serialVersionID = 1L;

	public Warrior() {
	}

	public Warrior(String name, String title, Integer hp, Boolean isAlive) {
		this.name = name;
		this.title = title;
		this.hp = hp;
		this.isAlive = isAlive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Boolean getAlive() {
		return isAlive;
	}

	public void setAlive(Boolean alive) {
		isAlive = alive;
	}

}
