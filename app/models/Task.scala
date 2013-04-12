package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient

case class Task(id: String, label: String)

object Task {
	def all() : List[Task] = {
			(for (x <- connectMongo("playTodo", "TODO").find()) yield {
				Task(x("_id").toString, x("label").toString)
			}).toList
	}
	def create(label: String) {
			val col = connectMongo("playTodo", "TODO")
			val newObj = MongoDBObject("label" -> label)
			col += newObj
			for (added <- col.findOne(newObj)) {
				
			}
	}
	
	def delete(id: String) {
			val col = connectMongo("playTodo", "TODO")
			val query = MongoDBObject("_id" -> new ObjectId(id))
			col.remove(query)
	  
	}
	
	def connectMongo(db: String, collection: String) : MongoCollection = {
			MongoClient()(db)(collection)
	}
}