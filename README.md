# Birthday Greetings Kata Scala edition

This is the Scala porting of a refactoring exercise by [Matteo Vaccari](http://matteo.vaccari.name/). The original idea behind it is to teach you about **dependency inversion principle** (DIP) and **dependency injection** (DI). For more info take a look at the [original repository](https://github.com/xpmatteo/birthday-greetings-kata).

However the fact that Scala is an _hybrid OOP/FP language_ opens the door to another interesting macro refactoring: **from bad imperative style to a pure FP style**. In order to reach that goal you have to:
- remove mutable variable
- split responsibility in functions and modules
- push the I/O at the boundary of the system
- handle failure/negative scenarious with effects
- handle side-effect
- put Monads everywhere! :trollface:

# How to use this Kata

There are two main ways to work:
1) Hard way: Refactor the code one tiny step at time until the code is clean and _pure_. In this case the `legacy-java-porting` is the pefect starting point. The project includes some tests useful to make sure you don't break the code while you refactor.
2) Simple way: Implements the logic from scratch. In this case the `master` branch offer a fresh empty SBT project.

In any case just clone the code and start hacking right away improving the design.

# License
This project is licensed under the terms of the MIT license.
