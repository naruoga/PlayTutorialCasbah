package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.typesafe.config._

case class Task(id: String, label: String)

object Task {
  def all() : List[Task] = {
    println("appllication.conf/mongodb.server=" + server)
    println("appllication.conf/mongodb.port=" + port.toString)
    val connect = connectMongo()
    val col = connect("playTodo")("TODO")
    try {
      (for (x <- col.find()) yield {
	Task(x("_id").toString, x("label").toString)
      }).toList
    } finally {
      connect.close()
    }
  }

  def create(label: String) {
    val connect = connectMongo()
    val col = connect("playTodo")("TODO")
    try {
      val newObj = MongoDBObject("label" -> label)
      col += newObj
    } finally {
      connect.close()
    }
  }
	
    def delete(id: String) {
      val connect = connectMongo()
      val col = connect("playTodo")("TODO")
      try {
	val query = MongoDBObject("_id" -> new ObjectId(id))
	col.remove(query)
      } finally {
	connect.close()
      }
    }
	
    def connectMongo() : MongoClient = {
      val appConf = ConfigFactory.load()
      // TODO: make me simpler; just making
      // "aaa:47017,bbb, ccc" -> (("aaa", 47017), ("bbb", 27017), ("ccc", 27017))
      // 1) split each server by "," 2) remove space 3) create pair of server and port
      // (note: if port aren't specified, port should be 27017 as MongoDB normal port)
      // port number can be Int or String (if Int choosed, replSetSeeds process
      // should also changed).
      //
      val serverInfoStr = appConf.getString("mongodb.servers")
      val serverInfo = serverInfoStr.split(',') 
			  map (_.trim)
			  map ((z: String) => 
			         { if (z.contains(':')) z.split(':') 
				   else (z + ":27017").split(':') }
			      )
      try {
	val replSetSeeds = for (Array(svr, port) <- serverInfo) yield new ServerAddress(svr, port.toInt)
      } catch {
	ex: MongoException: throw new RuntimeExcepction("can't connect mongo server: " + serverInfoStr)
	ex: java.net.UnknownHostException: throw new RuntimeException("some server might not collect: " 
					      + serverInfoStr)
      }
      MongoClient(replSetSeeds)
}
