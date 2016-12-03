# Cracker &ndash; AWS Polly Demo 

Built with Scala 2.12 and Java 8+.

## To Run
You can run the program from SBT:

    sbt run 

You can also build a fat jar and run it with arbitrary message to voice:

   $ sbt assembly
   $ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar "Learn Scala and Play at ScalaCourses.com!"
