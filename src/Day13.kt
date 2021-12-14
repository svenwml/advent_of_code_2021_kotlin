fun main() {

    fun do_folding(input: List<String>, return_number_of_dots_after_first_fold : Boolean, print_result: Boolean) : Int
    {
        // Get the dimensions of the required matrix first.

        data class coordinate(val x: Int, val y: Int)

        var coordinates = mutableListOf<coordinate>()
        var number_of_rows_total = 0
        var number_of_columns_total = 0

        data class command(val axis: Char, val position: Int)

        var commands = mutableListOf<command>()

        // Parser states:
        // 0 -> Parse Coordinates
        // 1 -> Parse Commands
        var parser_state = 0

        for(line in input)
        {
            // An empty line splits map coordinate list and commands.
            if(line == "")
            {
                parser_state = 1
                continue
            }

            if(parser_state == 0)
            {
                val coordinate_string = line.split(",".toRegex())

                coordinates.add(coordinate(coordinate_string[0].toInt(), coordinate_string[1].toInt()))

                number_of_columns_total = maxOf(number_of_columns_total, coordinate_string[0].toInt() + 1)
                number_of_rows_total = maxOf(number_of_rows_total, coordinate_string[1].toInt() + 1)
            }
            else if(parser_state == 1)
            {
                val command_parts = line.split(" ")[2].split("=")

                commands.add(command(command_parts[0][0], command_parts[1].toInt()))
            }
            else
            {
                throw Exception("Invalid parser state!")
            }

        }

        // Create map.
        val map = Array(number_of_rows_total) { BooleanArray(number_of_columns_total) }.toMutableList()

        // Fill the map with the initial values.
        for(coordinate in coordinates)
        {
            map[coordinate.y][coordinate.x] = true
        }

        var number_of_rows = number_of_rows_total
        var number_of_columns = number_of_columns_total

        // Helper functions for folding output.

        fun print_map()
        {
            for(row in 0 until number_of_rows)
            {
                for(column in 0 until number_of_columns)
                {
                    if(map[row][column] == true)
                    {
                        print("#")
                    }
                    else
                    {
                        print(" ")
                    }
                }

                print("\n")
            }
        }

        fun count_dots() : Int
        {
            var dot_count = 0

            for(row in 0 until number_of_rows)
            {
                for(column in 0 until number_of_columns)
                {
                    if(map[row][column] == true)
                    {
                        dot_count++
                    }
                }
            }

            return dot_count
        }

        for(command in commands)
        {
            if(command.axis == 'y')
            {
                // Horizontal split, fold up.
                val split_row_index = command.position

                for(row in split_row_index + 1 until number_of_rows)
                {
                    for(column in 0 until number_of_columns)
                    {
                        if(map[number_of_rows - row - 1][column] == true || map[row][column] == true)
                        {
                            map[number_of_rows - row - 1][column] = true
                        }
                    }
                }

                // Update the map size.
                number_of_rows = number_of_rows - split_row_index - 1
            }
            else if(command.axis == 'x')
            {
                // Vertical split, fold left.
                val split_column_index = command.position

                for(column in split_column_index + 1 until number_of_columns)
                {
                    for(row in 0 until number_of_rows)
                    {
                        if(map[row][number_of_columns - column - 1] == true || map[row][column] == true)
                        {
                            map[row][number_of_columns - column - 1] = true
                        }
                    }
                }

                // Update the map size.
                number_of_columns = number_of_columns - split_column_index - 1
            }
            else
            {
                throw Exception("Unknown axis in command!")
            }

            if(return_number_of_dots_after_first_fold)
            {
                return count_dots()
            }
        }

        if(print_result)
        {
            print_map()
        }

        return count_dots()
    }

    fun part1(input: List<String>) : Int {

        return do_folding(input, true, false)
    }

    fun part2(input: List<String>, print_result: Boolean) : Int {

        return do_folding(input,false, print_result)
    }

    val testInput = readInput("Day13_test")

    check(part1(testInput) == 17)
    check(part2(testInput, false) == 16)

    val input = readInput("Day13")

    println(part1(input))
    println(part2(input, true))
}