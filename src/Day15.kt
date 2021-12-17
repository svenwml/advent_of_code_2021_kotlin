fun main() {

    fun get_shortest_path_length(input: List<String>) : Int
    {
        val input_row_count = input.size
        val input_column_count = input[0].length
        val input_matrix = Array(input_row_count, { IntArray(input_column_count) })

        for(row_index in 0 until input_row_count)
        {
            for(column_index in 0 until input_column_count)
            {
                input_matrix[row_index][column_index] = input[row_index][column_index].digitToInt()
            }
        }

        // Use Dijkstra algorithm.

        data class point(val row : Int, val column: Int)

        var distance_matrix = Array(input_row_count, { IntArray(input_column_count) })
        var visit_mark_matrix = Array(input_row_count, { BooleanArray(input_column_count) })

        // Set initial values for each node.
        for(row_index in 0 until input_row_count)
        {
            for(column_index in 0 until input_column_count)
            {
                distance_matrix[row_index][column_index] = Int.MAX_VALUE
                visit_mark_matrix[row_index][column_index] = false
            }
        }

        // Start node has distance 0.
        distance_matrix[0][0] = 0

        for(row_index in 0 until input_row_count)
        {
            print("Progress: " + row_index + "/" + input_row_count + "\n")

            for(column_index in 0 until input_column_count)
            {

                // Get the point with the minimum distance.

                var minimum_distance = Int.MAX_VALUE
                var minimum_point = point(0,0)
                for(row_index in 0 until input_row_count)
                {
                    for(column_index in 0 until input_column_count)
                    {
                        if (visit_mark_matrix[row_index][column_index] == false &&
                                distance_matrix[row_index][column_index] <= minimum_distance)
                        {
                            minimum_distance = distance_matrix[row_index][column_index]
                            minimum_point = point(row_index, column_index)
                        }
                    }
                }

                visit_mark_matrix[minimum_point.row][minimum_point.column] = true

                // Get all the points adjacent to the minimum point...
                var adjacent_points = mutableListOf<point>()

                // ... node above.
                if(minimum_point.row > 0)
                {
                    adjacent_points.add(point(minimum_point.row - 1, minimum_point.column))
                }

                // ... node below.
                if(minimum_point.row < input_row_count - 1)
                {
                    adjacent_points.add(point(minimum_point.row + 1, minimum_point.column))
                }

                // ... node left.
                if(minimum_point.column > 0)
                {
                    adjacent_points.add(point(minimum_point.row, minimum_point.column - 1))
                }

                // ... node right.
                if(minimum_point.column < input_column_count - 1)
                {
                    adjacent_points.add(point(minimum_point.row, minimum_point.column + 1))
                }

                for(adjacent_point in adjacent_points)
                {
                    var cost = input_matrix[adjacent_point.row][adjacent_point.column]

                    if(!visit_mark_matrix[adjacent_point.row][adjacent_point.column] &&
                        distance_matrix[minimum_point.row][minimum_point.column] + cost < distance_matrix[adjacent_point.row][adjacent_point.column])
                    {
                        distance_matrix[adjacent_point.row][adjacent_point.column] = distance_matrix[minimum_point.row][minimum_point.column] + cost
                    }
                }
            }
        }

        // Return the minimum distance from top left to bottom right.
        return distance_matrix[input_row_count - 1][input_column_count - 1]
    }

    fun part1(input: List<String>) : Int {

        return get_shortest_path_length(input)
    }

    fun part2(input: List<String>) : Int {

        // Enlarge four times in row and column direction.
        val scheme_repeat_count = 5
        // Each element value increases by one on each enlarge time.

        var input_padded = mutableListOf<String>()

        for(line in input)
        {
            var line_padded = ""

            for(iteration in 0 until scheme_repeat_count)
            {
                var line_padded_iteration = ""

                for(character in line)
                {
                     var character_increased = ' '

                     if(character.digitToInt() + iteration > 9)
                     {
                         character_increased = (((character.digitToInt() + iteration) % 10) + 1).digitToChar()
                     }
                     else
                     {
                         character_increased = (character.digitToInt() + iteration).digitToChar()
                     }

                    line_padded_iteration += character_increased
                }

                line_padded += line_padded_iteration
            }

            input_padded.add(line_padded)
        }

        for(block_iteration in 0 until scheme_repeat_count - 1)
        {
            for(line_iteration in 0 until input.size)
            {
                var line_padded_increased = ""

                for(character in input_padded[line_iteration])
                {
                    var character_increased = ' '

                    if(character.digitToInt() + block_iteration + 1 > 9)
                    {
                        character_increased = (((character.digitToInt() + block_iteration + 1) % 10) + 1).digitToChar()
                    }
                    else
                    {
                        character_increased = (character.digitToInt() + block_iteration + 1).digitToChar()
                    }

                    line_padded_increased += character_increased
                }

                input_padded.add(line_padded_increased)
            }
        }

        return get_shortest_path_length(input_padded)
    }

    val testInput = readInput("Day15_test")

    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")

    println(part1(input))
    println(part2(input))
}