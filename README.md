# Optimize filter map chain (100 lines challenge)
Task from Jetbrain Internship 2021.

### Input:  
You get a string containing chains of calls to `map` and `filter` functions.   
Example:   
```map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}```   
It performs described operations on a list of ints.

### Output:
You are expected to turn this chain into exactly 2 calls. First to `filter` and then second to `map`. Your chain must produce result equal to the result of original chain on any list of ints.

### Solutions
I created two solutions to this problem.   
One in this repository was created as an additional challenge to fit the whole solution into 100 lines of code, including all the required data sanitization. Because of that this solution does only the most basic operations on expressions (mostly replasing one string with another).
You can find my second solution [here](https://github.com/VladoCC/optimize-filter-map-chain).
