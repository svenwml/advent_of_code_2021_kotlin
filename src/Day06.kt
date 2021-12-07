import java.util.*

fun main() {

    fun get_fish_count_after_n_days(input: List<String>, days: Int) : Long
    {
        // We expect one line with all the initial fishes.
        if(input.size != 1)
        {
            throw Exception("Expected input number of lines to be 1, but got " + input.size + " !")
        }

        val split_input = input[0].split(",")

        // Build initial list of fishes (each element representing the respective fish's current timer).

        var fishes = mutableListOf<Byte>()

        for(fish in split_input)
        {
            if(fish.toInt() < 0 || fish.toInt() > 8)
            {
                throw Exception("Initial fish age must be between 0 and 8, but got: " + fish.toInt())
            }

            fishes.add(fish.toByte())
        }

        // We hold all the spawns of the future days in a list which we can shift one element to the left on each day.
        var spawns_on_future_days = MutableList<Long>(10){0}

        // Fill the spawn list with values for the initial fishes.
        for(fish_index in 0 until fishes.size)
        {
            spawns_on_future_days[fishes[fish_index].toInt()]++
        }

        var number_of_fishes = split_input.size.toLong()

        for(day in 0 until days)
        {
            number_of_fishes += spawns_on_future_days[0]

            // Each fish that initiated a spawn today will spawn another fish in 7 days.
            spawns_on_future_days[7] += spawns_on_future_days[0]
            // Each fish spawned today will spawn another fish in 9 days.
            spawns_on_future_days[9] += spawns_on_future_days[0]

            // Shift the spawn list one element to the left to move on to the next day.
            Collections.rotate(spawns_on_future_days, -1)
            spawns_on_future_days[9] = 0
        }

        return number_of_fishes
    }

    fun part1(input: List<String>): Long {

        return get_fish_count_after_n_days(input, 80)
    }

    fun part2(input: List<String>): Long {

        return get_fish_count_after_n_days(input, 256)
    }

    val testInput = readInput("Day06_test")

    check(part1(testInput) == 5934.toLong())
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")

    println(part1(input))
    println(part2(input))
}