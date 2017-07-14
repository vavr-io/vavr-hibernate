Hibernate module for Vavr
-

This is currently a total WIP. 
 Initial goals
 
 1. Get a `List` wiring in via a UserType
 2. Get an `Option` wired in via a UserType
 3. Auto-support `List` through registration.
 4. Auto-support `Option` through registration.
 
 
 Notes: 
 -
 `PersistentList` must be one of type vavr `List` and be of type `PersistentCollection`
 to be supported by hibernate. Currently implementation is super verbose because it must delegate 
 so many methods. Lombok `@Delegate` doesn't really help because of it's limitations.
 Lombok `@Delegate` only delegates methods for 1 level of hierarchy and `List` has at least 4
 and implements multiple interfaces.
 
 `TypeDescriptor` is the mechanism by which hibernate auto-registers it's 
 type marshalling scheme.
 
 Current test in place fails as planned. Get that to work and we are have something.
 
