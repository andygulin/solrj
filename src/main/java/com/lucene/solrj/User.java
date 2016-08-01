package com.lucene.solrj;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

public class User implements Serializable {

	private static final long serialVersionUID = 7874060364002373219L;

	@Field("id")
	private String id;
	@Field("name")
	private String name;
	@Field("age")
	private int age;
	@Field("address")
	private String address;
	@Field("createdAt")
	private Date createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
