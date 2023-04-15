# spring-rest-search
Spring boot project that parses the search request parameter from a request and returns database results accordingly.

The search string has to be in specific format in order for the parser to be able to parse it. 

The parser supports the following:
 1. multiple different operations on different field types
 2. grouping conditions with parentheses
 3. using child and parent entities fields in the search
