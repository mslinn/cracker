# Cracker &ndash; AWS Polly Demo 

Built with Scala 2.12 and Java 8+.

## To Run
You can run the program from SBT:

    sbt run 

You can also build a fat jar and run it with arbitrary message to voice:

    $ sbt assembly
    $ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar \
    "Learn Scala and Play at ScalaCourses.com!"

By default, the program creates a temporary MP3 file, plays it, then deletes it.
You can specify that the MP3 file should be kept by giving it a name with the `-o` option.

    $ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar \
    -o blah.mp3 \
    "Learn Scala and Play at ScalaCourses.com!"

You can also cause input to be read with the `-i` option; this causes any input provided on the command line to be ignored.

    $ java -jar target/scala-2.12/cracker-assembly-0.1.0.jar \
    -i input.txt \
    -o blah.mp3
