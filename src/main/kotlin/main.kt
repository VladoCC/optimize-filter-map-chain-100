fun main(args: Array<String>) {
    var map = "element"
    var filter = ""
    readLine()?.split("%>%")?.forEach {
        if (it.endsWith("}")) {
            if (it.startsWith("map{")) {
                val last = map
                map = it.substring(4, it.length - 1).replace("element", last)
                parseIntExpression(map)
            } else if (it.startsWith("filter{")) {
                if (filter.isNotEmpty()) filter += '&'
                filter += it.substring(7, it.length - 1).replace("element", map)
                parseBooleanExpression(filter)
            } else throw SyntaxError("")
        } else throw SyntaxError("")
    }
    if (filter.isEmpty()) filter = "1=1"
    println("filter{$filter}%>%map{$map}")
}

private fun findComplex(exp: String): Int {
    var counter = 0
    exp.forEachIndexed { i, it ->
        if (it == '(') counter++ else if (it == ')') counter--
        if (counter == 0) return i + 1
    }
    return -1
}

private fun findNum(exp: String): Int{
    exp.forEachIndexed { i, it ->
        if (!it.isDigit()) return i
    }
    return  -1
}

private data class BinaryOperation(val operator: Char, val arg1: String, val arg2: String)

private fun parseBinaryOperation(exp: String): BinaryOperation {
    val innerExp = exp.substring(1, exp.length - 1)
    val operatorPos: Int
    if (innerExp.startsWith("element")) {
        operatorPos = 7
    } else if (innerExp[0].isDigit()) {
        operatorPos = findNum(innerExp)
    } else if (innerExp[0] == '-') {
        operatorPos = findNum(innerExp.substring(1)) + 1
    } else if (innerExp[0] == '(') {
        operatorPos = findComplex(innerExp)
    } else {
        operatorPos = -1
    }
    if (operatorPos > -1) {
        return BinaryOperation(innerExp[operatorPos],
            innerExp.substring(0, operatorPos),
            innerExp.substring(operatorPos + 1, innerExp.length))
    } else {
        throw SyntaxError("Invalid syntax for binary expression in the string: $exp")
    }
}

private fun parseComplexIntExpression(exp: String) {
    val operation = parseBinaryOperation(exp)
    val operators = arrayOf('+', '-', '*')
    if (operators.contains(operation.operator)) {
        parseIntExpression(operation.arg1)
        parseIntExpression(operation.arg2)
    } else throw TypeError("Arithmetical expression not found in the string: $exp")
}

fun parseIntExpression(exp: String) {
    if (exp.startsWith("(")) {
        parseComplexIntExpression(exp)
    } else if (exp != "element" && !exp.matches(Regex("-?\\d+"))) {
        throw SyntaxError("Invalid syntax for int expression in the string: $exp")
    }
}

fun parseBooleanExpression(exp: String) {
    val operation = parseBinaryOperation(exp)

    return when (operation.operator) {
        '&', '|' -> {
            parseBooleanExpression(operation.arg1)
            parseBooleanExpression(operation.arg2)
        }
        '<', '>', '='  -> {
            parseIntExpression(operation.arg1)
            parseIntExpression(operation.arg2)
        }
        else -> throw TypeError("Boolean expression not found in the string: $exp")
    }
}

class SyntaxError(text: String): Error(text)
class TypeError(text: String): Error(text)