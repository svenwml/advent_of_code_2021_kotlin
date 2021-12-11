import java.lang.Exception

fun main() {

    fun get_octopus_map_padded(input: List<String>) : List<List<Int>>
    {
        if(input.size < 1)
        {
            throw Exception("Empty input data!")
        }

        var number_of_columns = input[0].length

        // We will add one line on top and bottom, left and right consisting out of 0s to avoid the need
        // of special index handling at the borders.
        var octopus_map_left_and_right_padded = input.map{ listOf(0) + it.toCharArray().map { it.digitToInt() } + listOf(0)}.toMutableList()
        var octopus_map_padded = listOf(List(number_of_columns + 2){0}).toMutableList() +
                                 octopus_map_left_and_right_padded +
                                 listOf(List(number_of_columns + 2){0}).toMutableList()

        return octopus_map_padded
    }

    data class cycle_return(val octopus_map_padded: List<List<Int>>, val number_of_flashes_occurred : Int)

    fun run_cycle(octopus_map_padded: List<List<Int>>) : cycle_return
    {
        val number_of_rows = octopus_map_padded.size
        val number_of_columns = octopus_map_padded[0].size

        var number_of_flashes_occurred_in_current_cycle = 0

        // Increase the energy level of each octopus by 1.
        var octopus_map_padded = octopus_map_padded.map { it.map { it+1 }.toMutableList() }.toMutableList()

        var has_flashed = Array(number_of_rows) { BooleanArray(number_of_columns) {false} }

        do {
            var flash_occurred_in_current_subcycle = false

            for(row_index in 1 until number_of_rows - 1)
            {
                for(column_index in 1 until number_of_columns - 1)
                {
                    // Check for each octopus if it flashed (energy level > 9).
                    if(octopus_map_padded[row_index][column_index] > 9)
                    {
                        // This octopus flashed, reset energy level to 0 and increase energy level of all adjacent
                        // octopuses by 1.

                        octopus_map_padded[row_index][column_index] = 0
                        has_flashed[row_index][column_index] = true
                        flash_occurred_in_current_subcycle = true
                        number_of_flashes_occurred_in_current_cycle++

                        // Top left.
                        if(has_flashed[row_index - 1][column_index - 1] == false)
                        {
                            octopus_map_padded[row_index - 1][column_index - 1] += 1
                        }
                        // Top.
                        if(has_flashed[row_index - 1][column_index] == false)
                        {
                            octopus_map_padded[row_index - 1][column_index] += 1
                        }
                        // Top right.
                        if(has_flashed[row_index - 1][column_index + 1] == false)
                        {
                            octopus_map_padded[row_index - 1][column_index + 1] += 1
                        }
                        // Left.
                        if(has_flashed[row_index][column_index - 1] == false)
                        {
                            octopus_map_padded[row_index][column_index - 1] += 1
                        }
                        // Right.
                        if(has_flashed[row_index][column_index + 1] == false)
                        {
                            octopus_map_padded[row_index][column_index + 1] += 1
                        }
                        // Bottom left.
                        if(has_flashed[row_index + 1][column_index - 1] == false)
                        {
                            octopus_map_padded[row_index + 1][column_index - 1] += 1
                        }
                        // Bottom.
                        if(has_flashed[row_index + 1][column_index] == false)
                        {
                            octopus_map_padded[row_index + 1][column_index] += 1
                        }
                        // Bottom right.
                        if(has_flashed[row_index + 1][column_index + 1] == false)
                        {
                            octopus_map_padded[row_index + 1][column_index + 1] += 1
                        }
                    }
                }
            }
        }while(flash_occurred_in_current_subcycle == true)

        return cycle_return(octopus_map_padded, number_of_flashes_occurred_in_current_cycle)
    }

    fun part1(input: List<String>): Int {

        var octopus_map_padded = get_octopus_map_padded(input)
        var number_of_flashes = 0

        for(cycle_number in 0 until 100)
        {
            val cycle_return = run_cycle(octopus_map_padded)

            number_of_flashes += cycle_return.number_of_flashes_occurred
            octopus_map_padded = cycle_return.octopus_map_padded
        }

        return number_of_flashes
    }

    fun part2(input: List<String>): Int {

        if(input.size < 1)
        {
            throw Exception("Empty input data!")
        }

        val number_of_octopuses = input.size * input[0].length
        var octopus_map_padded = get_octopus_map_padded(input)
        var cycle_count = 0

        while(true)
        {
            val cycle_return = run_cycle(octopus_map_padded)
            octopus_map_padded = cycle_return.octopus_map_padded

            if(cycle_return.number_of_flashes_occurred == number_of_octopuses)
            {
                return cycle_count + 1
            }

            cycle_count ++
        }
    }

    val testInput = readInput("Day11_test")

    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")

    println(part1(input))
    println(part2(input))
}