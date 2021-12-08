import kotlin.math.abs
import kotlin.math.pow

fun main() {

    fun generate_split_sorted_input(input: List<String>): List<Int>
    {
        // We expect one line with all the horizontal values
        if(input.size != 1)
        {
            throw Exception("Expected input number of lines to be 1, but got " + input.size + " !")
        }

        return input[0].split(",") .map { it.toInt() }.sorted()
    }

    fun part1(input: List<String>): Int {

        val split_sorted_input = generate_split_sorted_input(input)
        val median = split_sorted_input[split_sorted_input.size / 2]

        // Calculate required fuel to travel from each element's horizontal position to the median.
        var required_fuel = 0
        for(horizontal_position in split_sorted_input)
        {
            required_fuel += abs(horizontal_position - median)
        }

        return required_fuel
    }

    fun part2(input: List<String>): Int {

        val split_sorted_input = generate_split_sorted_input(input)

        // Fuel consumption per submarine: fuel_consumption = 0.5 * x^2 + 0.5 x
        // where x is the horizontal distance.

        var fuel_consumption_min = Int.MAX_VALUE

        // Calculate the fuel consumption for each possible (horizontal) destination position and get the minimum...
        for(destination_position in split_sorted_input[0] .. split_sorted_input[split_sorted_input.size - 1])
        {
            var fuel_consumption_current_destination_position = 0

            for(initial_position in split_sorted_input)
            {
                val distance = abs(destination_position - initial_position)
                fuel_consumption_current_destination_position +=
                        (0.5 * distance.toDouble().pow(n=2) + 0.5 * distance.toDouble()).toInt()
            }

            if(fuel_consumption_current_destination_position < fuel_consumption_min)
            {
                fuel_consumption_min = fuel_consumption_current_destination_position
            }
        }

        return fuel_consumption_min
    }

    val testInput = readInput("Day07_test")

    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")

    println(part1(input))
    println(part2(input))
}