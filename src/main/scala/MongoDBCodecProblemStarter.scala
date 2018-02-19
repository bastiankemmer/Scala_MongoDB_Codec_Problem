import org.mongodb.scala.{MongoClient, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import scala.concurrent.ExecutionContext.Implicits.global
import org.mongodb.scala.model.Filters._

import scala.concurrent.duration._
import scala.concurrent.Await

object MongoDBCodecProblemStarter extends App {
  val codecRegistry = fromRegistries(fromProviders(classOf[ExampleCollection]), DEFAULT_CODEC_REGISTRY)

  val mongoClient: MongoClient = MongoClient()

  val testDatabase: MongoDatabase = mongoClient.getDatabase("scalaMongoProblem").withCodecRegistry(codecRegistry)

  val testMap: Map[String, Any] = Map[String, Any]("foo" -> Map[String, Any]("bar" -> "baz"))

  val mongoDBCollection = testDatabase.getCollection[ExampleCollection]("exampleCollection")
  val exampleCollection = ExampleCollection(testMap)

  val result = Await.result(for {
    _ <- mongoDBCollection.insertOne(exampleCollection).toFuture()
    result <- mongoDBCollection.find(equal("_id", exampleCollection._id)).head()
  } yield result, 30.second)
}
