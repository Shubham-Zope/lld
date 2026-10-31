# SOLID cheat sheet

| Principle | Question to ask of your design | Typical fix |
|---|---|---|
| Single Responsibility | Does this class change for more than one reason? | Split by reason to change (pricing vs allocation) |
| Open/Closed | Can I add the follow-up by adding a class? | Interface at the variation point + Strategy/Factory |
| Liskov Substitution | Does every subtype honour the parent's contract? | Don't throw `UnsupportedOperation` from subclasses; reshape hierarchy |
| Interface Segregation | Do implementers stub methods they don't need? | Split fat interfaces into role interfaces |
| Dependency Inversion | Does domain code `new` its infrastructure? | Inject interfaces (repository, clock, gateway) |
