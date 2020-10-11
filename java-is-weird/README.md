# Examples of weird Java stuff :)

* [testBigDecimalCompare](src/test/java/itx/examples/javaisweird/tests/WeirdTesting.java) -
  BigDecimal.equals(?) compares content of objects, however BigDecimal.compare(?) deliberately considers values like '1' and '1.0' as same value.
* [testIntegerCompare](src/test/java/itx/examples/javaisweird/tests/WeirdTesting.java) -
  The Integer type keeps a cache of all objects with a value in the range of -128 to 127 for performance reasons. So when you declare new variables in that range, you’re actually referring to the same object. 
* [testCharArithmetic](src/test/java/itx/examples/javaisweird/tests/WeirdTesting.java) -
  ```
  char ch = '0'; // ASCII for ‘0’ is 48
  ch *= 1.1; // 48 x 1.1 is 52.8 which turns to 52 when cast to char
  assertEquals('4', ch); // 52 represents ‘4’ in ASCII
  ```