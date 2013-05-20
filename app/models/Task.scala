package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.typesafe.config._

case class Task(id: String, label: String)

object Task {
  private val appConf = ConfigFactory.load()
  private val server = appConf.getString("mongodb.server")
  private val port   = appConf.getInt("mongodb.port")
  
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
	
    def connectMongo() : MongoClient = MongoClient(server, port)
}
