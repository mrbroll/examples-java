#jnifun

sample code is from IBM's jni [tutorial](http://www.ibm.com/developerworks/java/tutorials/j-jni/j-jni.html).

###Sample1

Calling c++ from java

To Compile the java class:

    javac Sample1.java

To Generate the c header (already done)

    javah Sample1

To Compile and link the c++ library

OSX:

    clang++ -I/System/Library/Frameworks/JavaVM.Framework/Versions/Current/Headers/ -I/System/Library/Frameworks/JavaVM.Framework/Versions/A/Headers -o Sample1.o Sample1.cc
    clang++ -dynamiclib -o libSample1.jnilib Sample1.o

To run the java code:

    java Sample1

