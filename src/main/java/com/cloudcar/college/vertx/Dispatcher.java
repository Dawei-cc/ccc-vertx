package com.cloudcar.college.vertx;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Dispatcher
{
	private final static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	private MainVerticle verticle;

	public Dispatcher(MainVerticle mainVerticle)
	{
		this.verticle = mainVerticle;
	}

	public void about(RoutingContext routingContext)
	{
		JsonObject result = new JsonObject();
		List<String> nodes = CloudCarClientUtil.getClusterManager().getNodes();
		EventBus eventBus = CloudCarClientUtil.getVertx().eventBus();
		List<Future> futures = new ArrayList<>();
		for (String node : nodes)
		{
			Future<JsonObject> future = Future.future();
			futures.add(future);
			eventBus.send("about:"+node, "", h -> {
				JsonObject message = (JsonObject)h.result().body();
				future.complete(message);
			});
		}
		CompositeFuture.join(futures).setHandler(h -> {
			if (h.succeeded())
			{
				JsonArray cluster = new JsonArray();
				for (int i=0;i<h.result().size();i++)
				{
					JsonObject r = h.result().resultAt(i);
					cluster.add(r);
				}
				result.put("cluster", cluster);
				routingContext
						.response()
						.putHeader("content-type", "application/json; charset=utf-8")
						.end(result.encodePrettily());
			}
		});
	}

	public void test1(RoutingContext routingContext)
	{
		JsonObject result = new JsonObject();
		result.put("port", CloudCarClientUtil.getPort());
		result.put("nodeID", CloudCarClientUtil.getClusterManager().getNodeID());
		result.put("deploymentID", verticle.deploymentID());
		result.put("thread", Thread.currentThread().getName());
		routingContext
				.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(result.encodePrettily());
	}

	public void test2(RoutingContext routingContext)
	{
		EventBus eventBus = CloudCarClientUtil.getVertx().eventBus();
		eventBus.send("testing:send", "", h -> {
			JsonObject ret = new JsonObject();
			JsonObject runningOn = new JsonObject();
			runningOn.put("port", CloudCarClientUtil.getPort());
			runningOn.put("nodeID", CloudCarClientUtil.getClusterManager().getNodeID());
			runningOn.put("deploymentID", verticle.deploymentID());
			runningOn.put("thread", Thread.currentThread().getName());
			ret.put("runningOn", runningOn);
			JsonObject result = (JsonObject)h.result().body();
			ret.put("consumerRunningOn", result);
			routingContext
					.response()
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(ret.encodePrettily());
		});
	}

	public void test3(RoutingContext routingContext)
	{
		EventBus eventBus = CloudCarClientUtil.getVertx().eventBus();
		eventBus.publish("testing:publish", "");
		JsonObject runningOn = new JsonObject();
		runningOn.put("port", CloudCarClientUtil.getPort());
		runningOn.put("nodeID", CloudCarClientUtil.getClusterManager().getNodeID());
		runningOn.put("deploymentID", verticle.deploymentID());
		runningOn.put("thread", Thread.currentThread().getName());
		routingContext
				.response()
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(runningOn.encodePrettily());
	}
}
