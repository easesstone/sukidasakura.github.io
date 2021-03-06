import org.apache.spark.{SparkConf, SparkContext}

object ReduceByKeyDemo {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf().setMaster("local[2]").setAppName("ReduceByKeyDemo")
		val sc = new SparkContext(conf)
		// def parallelize[T](seq: Seq[T], numSlices: Int = defaultParallelism)(implicit arg0: ClassTag[T]): RDD[T]
		// - 第一个参数一是一个 Seq集合
		// - 第二个参数是分区数
		// - 返回的是RDD[T]
		val x = sc.parallelize(Array(("a", 1), ("b", 1), ("a", 1),
			("a", 1), ("b", 1), ("b", 1),
			("b", 1), ("b", 1)), 3) //此处表示分区数为3
		// 把相同key 的数值放在一起，然后进行操作。
		// (pre,after) -> (pre+after) 是一个抽象，逻辑上pre与after分别代表同一个key的两个不同values
		val y = x.reduceByKey((pre, after) => pre + after)
		y.collect().foreach{println(_)}

//		val words = "Mr.Li is my husband".split(" ")
//		// 接下来，我们想要计算这些单词.
//		val pairs = words.map(word => (word, 1))
//		sc.parallelize(pairs).collect().foreach(println(_))

	}
}
