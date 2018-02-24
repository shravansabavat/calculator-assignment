# Steps to clone, build and run this project.

```bash
> git clone https://github.com/shravansabavat/calculator-assignment.git
> cd calculator-assignment
> mvn clean install
> java -jar target/calculator-jar-with-dependencies.jar "add(1,2)"
> java -jar target/calculator-jar-with-dependencies.jar "add(1,2)" "debug"
```

## Sample output
```bash
> java -jar target/calculator-jar-with-dependencies.jar "add(1,2)"
2018-02-24 00:19:04 INFO  root:32 - ======= Evaluting the expression add(1,2) =======
2018-02-24 00:19:04 INFO  root:33 - ======= Expression is evaluated to 3 ======= 
```
```bash
> java -jar target/calculator-jar-with-dependencies.jar "add(1,2)" "debug"
2018-02-24 00:21:21 DEBUG root:24 - Log level is now set to: debug
2018-02-24 00:21:21 INFO  root:32 - ======= Evaluting the expression add(1,2) =======
2018-02-24 00:21:21 DEBUG Calculator:32 - Pushing + into stack
2018-02-24 00:21:21 DEBUG Calculator:60 - Pushing 1 into stack
2018-02-24 00:21:21 DEBUG Calculator:39 - Popped 1 from stack
2018-02-24 00:21:21 DEBUG Calculator:43 - Performing operation + on values 1,2from stack
2018-02-24 00:21:21 DEBUG Calculator:177 - Performing operation + on values 1,2
2018-02-24 00:21:21 DEBUG Calculator:194 - Result of operation + on values 1,2 is 3
2018-02-24 00:21:21 DEBUG Calculator:46 - Pushing result from operation3 into stack
```

### Here is the travis build for this project

https://travis-ci.org/shravansabavat/calculator-assignment
