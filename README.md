# Steps to clone, build and run this project.

```bash
> git clone https://github.com/shravansabavat/calculator-assignment.git
> cd calculator-assignment
> mvn clean install
> java -jar target/calculator-jar-with-dependencies.jar "add(1,2)"
> java -jar target/calculator-jar-with-dependencies.jar "add(1,2)" "debug"
```

## Operators supported
```
add
sub
mult/multi
div
let operator for assigning values to variables
```


## Examples
```bash
shravankumarsabavat$ java -jar target/calculator-jar-with-dependencies.jar "add(1, mult(2, 3))"
2018-02-24 13:54:37 INFO  root:31 - ======= Evaluting the expression add(1, mult(2, 3)) =======
2018-02-24 13:54:37 INFO  root:32 - ======= Expression is evaluated to 7 ======= 
shravankumarsabavat$ java -jar target/calculator-jar-with-dependencies.jar "let(a, 5, let(b, mult(a, 10), add(b, a)))"
2018-02-24 13:54:53 INFO  root:31 - ======= Evaluting the expression let(a, 5, let(b, mult(a, 10), add(b, a))) =======
2018-02-24 13:54:53 INFO  root:32 - ======= Expression is evaluated to 55 ======= 
shravankumarsabavat$ java -jar target/calculator-jar-with-dependencies.jar "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))"
2018-02-24 13:55:06 INFO  root:31 - ======= Evaluting the expression let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)) =======
2018-02-24 13:55:06 INFO  root:32 - ======= Expression is evaluated to 40 ======= 
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
