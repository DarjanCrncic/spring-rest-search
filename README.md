# spring-rest-search
Spring boot project that parses the search request parameter from a request and returns database results accordingly.

The search string has to be in specific format in order for the parser to be able to parse it. The format is "{entity field}\~{operation}\~{value}".
For example "name\~like\~John".
To see more examples, check the test directory and the tests.

Supported features are following:
 1. multiple different operations on different field types
    - LIKE, EQ, NOT_EQ, GT, LT, LTE, GTE, IN, NOT_IN
 2. grouping conditions with parentheses and logical operators (AND and OR)
    - "(name\~like\~John\~or\~lastName\~like\~Doe)~and~(name\~like\~Mia\~or\~lastName\~eq\~Chen)"
 3. using child and parent entities fields in the search
    - "parent.name\~like\~Philip"
