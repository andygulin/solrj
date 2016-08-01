package com.lucene.solrj;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.SolrResponseBase;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.util.NamedList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SolrJTest {
	private SolrClient client = null;

	private static final String SOLR_SERVER_URL = "http://192.168.137.130:8080/solr/collection1";

	@Before
	public void init() {
		client = new Builder(SOLR_SERVER_URL).allowCompression(true).withResponseParser(new BinaryResponseParser())
				.build();
	}

	@Test
	public void build() {
		final int BUILD_USER_COUNT = RandomUtils.nextInt(100, 10000);
		List<User> users = UserBuilder.getBuilderUsers(BUILD_USER_COUNT);
		UpdateResponse response;
		try {
			response = client.addBeans(users);
			print(response);
			response = client.commit();
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			try {
				response = client.rollback();
				print(response);
			} catch (SolrServerException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Test
	public void ping() throws SolrServerException, IOException {
		SolrPingResponse response = client.ping();
		print(response);
	}

	@Test
	public void deleteAll() {
		UpdateResponse response = null;
		try {
			response = client.deleteByQuery("*:*");
			print(response);
			response = client.commit();
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			try {
				response = client.rollback();
				print(response);
			} catch (SolrServerException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Test
	public void delete() {
		UpdateResponse response = null;
		try {
			response = client.deleteById("1");
			print(response);
			response = client.commit();
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			try {
				response = client.rollback();
				print(response);
			} catch (SolrServerException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Test
	public void queryAll() {
		SolrQuery query = new SolrQuery("*:*");
		try {
			QueryResponse response = client.query(query);
			List<User> users = response.getBeans(User.class);
			print(users);
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void queryOne() {
		SolrQuery query = new SolrQuery("id:1");
		try {
			QueryResponse response = client.query(query);
			List<User> users = response.getBeans(User.class);
			print(users);
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void queryByAge() {
		SolrQuery query = new SolrQuery("age:[10 TO 20]");
		setQuery(query);
		try {
			QueryResponse response = client.query(query);
			List<User> users = response.getBeans(User.class);
			print(users);
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void queryByName() {
		SolrQuery query = new SolrQuery("name:*bb*");
		setQuery(query);
		try {
			QueryResponse response = client.query(query);
			List<User> users = response.getBeans(User.class);
			print(users);
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void queryByNameAndAge() {
		SolrQuery query = new SolrQuery("name:*bb* AND age:[1 TO 10]");
		setQuery(query);
		try {
			QueryResponse response = client.query(query);
			List<User> users = response.getBeans(User.class);
			print(users);
			print(response);
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setQuery(SolrQuery query) {
		query.setSort("createAt", ORDER.desc);
		query.setStart(0);
		query.setRows(10);
	}

	private void print(List<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}

	private void print(SolrResponseBase response) {
		System.out.println("ElapsedTime : " + response.getElapsedTime());
		System.out.println("QTime : " + response.getQTime());
		System.out.println("RequestUrl : " + response.getRequestUrl());
		System.out.println("Status : " + response.getStatus());

		NamedList<Object> list = response.getResponse();
		Iterator<Entry<String, Object>> iter = list.iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			System.out.println("Response : " + entry.getKey() + " " + entry.getValue());
		}

		Iterator<?> headerIter = response.getResponseHeader().iterator();
		while (headerIter.hasNext()) {
			System.out.println("ResponseHeader : " + headerIter.next());
		}

		if (response instanceof QueryResponse) {
			QueryResponse queryResponse = (QueryResponse) response;
			System.out.println("Result Num Found: " + queryResponse.getResults().getNumFound());
		}
	}
}