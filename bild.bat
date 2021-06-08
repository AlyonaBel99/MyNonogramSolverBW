rmdir /s /q build 2>nul
mkdir build
javac -encoding utf8 -sourcepath src -d build src/com/company/Main.java
java -classpath build com.company.Main