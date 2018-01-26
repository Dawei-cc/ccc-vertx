package com.cloudcar.college.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher
{
	private final static Logger logger = LoggerFactory.getLogger(Launcher.class);

	public static void main(String[] args)
	{
		logger.info("Launcher starts");

		//4 CloudCarClientUtil.setPort(); //4

		//1 HazelcastClusterManager manager = new HazelcastClusterManager(); //1
		//1 VertxOptions options = new VertxOptions().setClusterManager(manager); //1
		//1 CloudCarClientUtil.setClusterManager(manager); //1
		//2 logger.info("Main Thread: " + Thread.currentThread().getName()); //2

		//6 logger.info("sequence: 1"); //6
		//1 Vertx.clusteredVertx(options, h -> { //1
		//6 	logger.info("sequence: 2"); //6
		//2 	if (h.failed()) //2
		//2 	{ //2
		//2 		logger.info("Failed to create clustered Vertx"); //2
		//2 		System.exit(1); //2
		//2 	} //2
		//2 	logger.info("Vertx Handler Thread: " + Thread.currentThread().getName()); //2
		//2 	logger.info("Cluster Vertx Node ID: " + manager.getNodeID()); //2

		//3	Vertx vertx = h.result(); //3
		//3	CloudCarClientUtil.setVertx(vertx); //3

		//6	logger.info("sequence: 3"); //6
		//3 	vertx.deployVerticle(new ServerInitVerticle(), res -> { //3
		//6 		logger.info("sequence: 4"); //6
		//3		logger.info("Deploy ServerInitVerticle Handler Thread: " + Thread.currentThread().getName()); //3
		//3		logger.info("ServerInitVerticle Deployment ID: " + res.result()); //3

		//4		vertx.deployVerticle(MainVerticle.class, new DeploymentOptions().setInstances(2), hh -> { //4
		//6			logger.info("sequence: 5"); //6
		//4			logger.info("Deploy MainVerticle Handler Thread: " + Thread.currentThread().getName()); //4
		//4			logger.info("MainVerticle Deployment ID: " + hh.result()); //4
		//4		}); //4
		//6		logger.info("sequence: 6"); //6
		//5		vertx.deployVerticle(MainVerticle.class, new DeploymentOptions().setInstances(2), hh -> { //5
		//6			logger.info("sequence: 7"); //6
		//5			logger.info("2nd Deploy MainVerticle Handler Thread: " + Thread.currentThread().getName()); //5
		//5			logger.info("2nd MainVerticle Deployment ID: " + hh.result()); //5
		//5		});  //5
		//6		logger.info("sequence: 8"); //6
		//3	}); //3
		//6	logger.info("sequence: 9"); //6
		//1 }); //1
		//6 logger.info("sequence: 10"); //6

		logger.info("Launcher ends");
	}
}
