# Cracker &ndash; AWS Polly Demo

[![Build Status](https://travis-ci.org/mslinn/cracker.svg?branch=master)](https://travis-ci.org/mslinn/cracker)

Voices a string or text file using AWS Polly. A maximum of 3000 characters is allowed in the message to voice.

Built with Scala 2.12 and Java 8+.

## To Run
You can run the program from SBT:

    sbt run

You can also build a fat jar and run it with arbitrary message to voice:

    $ sbt assembly
    $ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar \
    "Learn Scala and Play at ScalaCourses.com!"

By default, the program creates a temporary MP3 file, plays it, then deletes it.
You can specify that the MP3 file should be kept and not played by giving it a name with the `-o` option.

    $ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar \
    -o output.mp3 \
    "Learn Scala and Play at ScalaCourses.com!"

You can also cause input to be read with the `-i` option; this causes any input provided on the command line to be ignored.

    $ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar \
    -i input.txt \
    -o output.mp3

The default voice to use is Joanna. You can specify another voice with the `-w` option. The only other English voice is Joey.
Available voices are: Amy, Astrid, Brian, Carla, Carmen, Celine, Chantal, Conchita, Cristiano, Dora, Emma, Enrique, Ewa, Filiz, Geraint, Giorgio, Gwyneth, Hans, Ines, Ivy,
 Jacek, Jan, Joanna, Joey, Justin, Karl, Kendra, Kimberly, Liv, Lotte, Mads, Maja, Marlene, Mathieu, Maxim, Miguel, Mizuki, Naja, Nicole, Penelope, Raveena, Ricard
o, Ruben, Russell, Salli, Tatyana, and Vitoria

    $ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar \
    -i input.txt \
    -o output.mp3 \
    -w Joey

The following help message is produced in response to the `-h` option:

    $ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar -h
    You can run this program from SBT:
    
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
    
    The voice used defaults to Joanna, but you can override this with the -w option.
    VoiceIds are: Amy, Astrid, Brian, Carla, Carmen, Celine, Chantal, Conchita, Cristiano, Dora, Emma, Enrique, Ewa, Filiz, Geraint, Giorgio, Gwyneth, Hans, Ines, Ivy, Jacek, Jan, Joanna, Joey, Justin, Karl, Kendra, Kimberly, Liv, Lotte, Mads, Maja, Marlene, Mathieu, Maxim, Miguel, Mizuki, Naja, Nicole, Penelope, Raveena, Ricardo, Ruben, Russell, Salli, Tatyana, Vitoria
    
    This help message is produced in response to the `-h` option:
    
        $ java -jar target/scala-2.12/cracker-assembly-0.1.1.jar -h
