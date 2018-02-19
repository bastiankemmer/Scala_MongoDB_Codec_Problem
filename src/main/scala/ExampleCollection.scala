import org.mongodb.scala.bson.ObjectId

object ExampleCollection {
  def apply(test: Map[String, Any]): ExampleCollection = new ExampleCollection(new ObjectId(), test)
}

case class ExampleCollection(_id: ObjectId, test: Map[String, Any])
