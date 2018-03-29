# Solution description

This refactored solution follow a principle design that I like call `plain functionl programming`. The main idea is to model your application mostly with functions and function combinators. During this process try to stay away from introducing one hundred monads for every little aspects just because it's possible to do. Just to be clear, with this design:

- the use one/two monads is :+1:
- the use of IO[Either[List[A], Option[List[Try[B]]]]] is :-1:

# Peculiar properties (aka buzzword)
- Model the non determinism of `loadEmployee` with the `List Monad`.
- Use mutable local collection in `loadEmployee` so the function is still pure but gain performace.
- Push the I/O at the boundary of the `BirthdayService`.
- Split into separated modules domain and infrastructure.
- Decouple domain from infrastructure in the `BirthdayService` with dependency inversion principle (DIP).
- Use high-order functions to build functions with injected configuration values (DI).
- Don't handle failure scenarios (malformed file, smtp server down, etc), let the application crash.
- `AcceptanceTest` suite works in memory thaks to stubbed infrastructure functions.