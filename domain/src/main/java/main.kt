import java.math.BigDecimal

fun main() {
    val decimal = BigDecimal.valueOf(12.12)
    val precision = decimal.precision()
    precision
}