## OpenSrcSw

2022년 1학기 오픈소스SW입문과목

### 컴파일

```
javac -cp jars/jsoup-1.12.1.jar:jars/kkma-2.1.jar src/scripts/*.java -d bin -encoding UTF8
```

### 실행

```
java -cp ./jars/jsoup-1.12.1.jar:./jars/kkma-2.1.jar:bin scripts.kuir -c ./data

java -cp ./jars/jsoup-1.12.1.jar:./jars/kkma-2.1.jar:bin scripts.kuir -k ./collection.xml

java -cp ./jars/jsoup-1.12.1.jar:./jars/kkma-2.1.jar:bin scripts.kuir -i ./index.xml

java -cp ./jars/jsoup-1.12.1.jar:./jars/kkma-2.1.jar:bin scripts.kuir -s ./index.post -q "라면에는 면, 분말, 스프가 있다."
```