package com.framework.dao;

import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface SuperDao {
	public Gson moGson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
	JSONParser moJSONParser = new JSONParser();
}
