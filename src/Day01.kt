fun main() {

    fun calculate_number_of_increases_in_list(input: List<String>): Int {
        var previous_measurement = Int.MAX_VALUE
        var measurement_increase_counter = 0

        for(measurement in input)
        {
            if(measurement.toInt() > previous_measurement)
            {
                measurement_increase_counter++
            }

            previous_measurement = measurement.toInt()
        }

        return measurement_increase_counter
    }

    fun generate_measurement_window_sum_list(input: List<String>, window_size: Int): List<String> {
        val windows = input.windowed(window_size,1)

        val sums_list = arrayListOf<String>()
        for (window in windows.toList())
        {
            var sum = 0
            for(i in 0..window_size - 1) {
                sum += window[i].toInt();
            }

            sums_list.add(sum.toString())
        }

        return sums_list
    }

    fun part1(input: List<String>): Int {
        return calculate_number_of_increases_in_list(input)
    }

    fun part2(input: List<String>): Int {
        return calculate_number_of_increases_in_list(generate_measurement_window_sum_list(input, 3))
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}