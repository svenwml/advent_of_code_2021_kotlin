fun main() {

    fun calculate_position(input: List<String>, use_aim: Boolean): Int {
        var depth = 0
        var horizontal_position = 0
        var aim = 0

        val direction_regex = Regex("[a-z]+")
        val distance_regex = Regex("[0-9]+")

        for(command in input)
        {
            var direction = direction_regex.find(command)
            var distance = distance_regex.find(command)

            if(direction == null || distance == null)
            {
                throw Exception("Command " + command + " does not match expected pattern!")
            }

            when(direction.value)
            {
                "forward" ->
                {
                    horizontal_position += distance.value.toInt()

                    if(use_aim)
                    {
                        depth += (aim * distance.value.toInt())
                    }
                }
                "backward" -> horizontal_position -= distance.value.toInt()
                "up" ->
                {
                    if(!use_aim)
                    {
                        depth -= distance.value.toInt()
                    }

                    aim -= distance.value.toInt()
                }
                "down" ->
                {
                    if(!use_aim)
                    {
                        depth += distance.value.toInt()
                    }

                    aim += distance.value.toInt()
                }
                else -> {
                    throw Exception("Direction " + direction.value + " unknown!")
                }
            }
        }

        return horizontal_position * depth
    }

    fun part1(input: List<String>): Int {
        return calculate_position(input = input, use_aim = false)
    }

    fun part2(input: List<String>): Int {
        return calculate_position(input = input, use_aim = true)
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}