package models

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient

case class Task(id: String, label: String)

object Task {
	def all() : List[Task] = {
			(for (x <- MongoClient()("test")("names").find()) yield {
				Task(x("_id").toString, x("name").toString)
			}).toList
	}
	def create(label: String) {
			val col = MongoClient()("test")("names")
			val newObj = MongoDBObject("name" -> label)
			col += newObj
			for (added <- col.findOne(newObj)) {
				
			}
	}
	
	def delete(id: String) {
			val col = MongoClient()("test")("names")
			val query = MongoDBObject("_id" -> new ObjectId(id))
			col.remove(query)
	  
	}
}