package com.cloudcar.college.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ServerInitVerticle extends AbstractVerticle
{
	private final static Logger logger = LoggerFactory.getLogger(ServerInitVerticle.class);
	private String id;

	@Override
	public void start()
	{
		logger.info("Deploying ServerInitVerticle starts");
		logger.info("Deploy ServerInitVerticle running in: " + Thread.currentThread().getName());

		Vertx vertx = CloudCarClientUtil.getVertx();
		EventBus eventBus = vertx.eventBus();
		ClusterManager clusterManager = CloudCarClientUtil.getClusterManager();
		eventBus.consumer("about:" + clusterManager.getNodeID(),  message -> {
			message.reply(getAboutData());
		});

		CloudCarClientUtil.putDeployment("ServerInitVerticle:" + deploymentID(), Thread.currentThread().getName());
		logger.info("Deploying ServerInitVerticle ends");
	}

	private JsonObject getAboutData()
	{
		JsonObject result = new JsonObject();
		result.put("port", CloudCarClientUtil.getPort());
		result.put("nodeID", CloudCarClientUtil.getClusterManager().getNodeID());
		result.put("deploymentIDs", CloudCarClientUtil.getDeployment());

		return result;
	}

	public void setID(String id)
	{
		this.id = id;
	}

	public String getID()
	{
		return id;
	}
}
