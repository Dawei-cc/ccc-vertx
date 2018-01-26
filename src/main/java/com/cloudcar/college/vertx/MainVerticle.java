package com.cloudcar.college.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle
{
	private final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public void start()
	{
		logger.info("Deploying MainVerticle starts in: " + Thread.currentThread().getName());

		HttpServer server = vertx.createHttpServer();

		Router router = Router.router(vertx);
		setupRouter(router);

		server.requestHandler(router::accept).listen(CloudCarClientUtil.getPort());

		EventBus eventBus = vertx.eventBus();
		eventBus.consumer("testing:send", h -> {
			JsonObject result = new JsonObject();
			result.put("port", CloudCarClientUtil.getPort());
			result.put("nodeID", CloudCarClientUtil.getClusterManager().getNodeID());
			result.put("deploymentID", deploymentID());
			result.put("thread", Thread.currentThread().getName());
			h.reply(result);
		});

		eventBus.consumer("testing:publish", h -> {
			logger.info("[testing:publish] nodeID: {}, deploymentID: {}, thread: {}", CloudCarClientUtil.getClusterManager().getNodeID(), deploymentID(), Thread.currentThread().getName());
		});

		CloudCarClientUtil.putDeployment("MainVerticle:" + deploymentID(), Thread.currentThread().getName());
		logger.info("Deploying MainVerticle ends in: " + Thread.currentThread().getName());
	}

	private void setupRouter(Router router)
	{
		Dispatcher dispatcher = new Dispatcher(this);
		router.route(HttpMethod.GET, "/about").handler(dispatcher::about);
		router.route(HttpMethod.GET, "/test1").handler(dispatcher::test1);
		router.route(HttpMethod.GET, "/test2").handler(dispatcher::test2);
		router.route(HttpMethod.GET, "/test3").handler(dispatcher::test3);
	}
}
