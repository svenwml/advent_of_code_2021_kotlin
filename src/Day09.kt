fun main() {

    fun get_heightmap_padded(input: List<String>) : List<List<Int>>
    {
        if(input.size < 1)
        {
            throw Exception("Empty input data!")
        }

        var number_of_rows = input.size
        var number_of_columns = input[0].length

        // We will add one line on top and bottom, left and right consisting out of 9s to avoid the need
        // of special index handling at the borders.
        var heightmap_left_and_right_padded = input.map{ listOf(9) + it.toCharArray().map { it.digitToInt() } + listOf(9)}.toMutableList()
        var heightmap_padded = listOf(List(number_of_columns + 2){9}).toMutableList() +
                heightmap_left_and_right_padded +
                listOf(List(number_of_columns + 2){9}).toMutableList()

        return heightmap_padded
    }

    data class point(val row_index: Int, val column_index: Int)
    data class minimum(val coordinates : point, val value: Int)

    // Input is a padded height map.
    fun get_minimums(heightmap_padded: List<List<Int>>): List<minimum>
    {
        val number_of_rows = heightmap_padded.size
        val number_of_columns = heightmap_padded[0].size

        // Now check for each element (leaving out the borders): Is each element around smaller?

        var minimums = mutableListOf<minimum>()

        for(row_index in 1 until number_of_rows - 1)
        {
            for(column_index in 1 until number_of_columns - 1)
            {
                val sub_list_rows = heightmap_padded.subList(row_index - 1, row_index + 2)

                var sub_list = sub_list_rows[0].subList(column_index - 1, column_index + 2) +
                        sub_list_rows[1].subList(column_index - 1, column_index + 2) +
                        sub_list_rows[2].subList(column_index - 1, column_index + 2)

                if(heightmap_padded[row_index][column_index] == sub_list.sorted().first())
                {
                    minimums.add(minimum(point(row_index, column_index), heightmap_padded[row_index][column_index]))
                }
            }
        }

        return minimums
    }

    fun part1(input: List<String>): Int {

        var risk_level_sum = 0

        val minimums = get_minimums(get_heightmap_padded(input))

        for(minimum in minimums)
        {
            risk_level_sum += minimum.value + 1
        }

        return risk_level_sum
    }

    fun part2(input: List<String>): Int {

        val height_map_padded = get_heightmap_padded(input)
        val minimums = get_minimums(height_map_padded)

        var basin_point_counts = mutableListOf<Int>()

        for(minimum in minimums)
        {
            data class basin_point(val coordinates: point, var is_checked: Boolean)

            var basin_points = mutableListOf<basin_point>()
            basin_points.add(basin_point(minimum.coordinates, false))

            var unchecked_basin_points_found = true

            while(unchecked_basin_points_found)
            {
                unchecked_basin_points_found = false

                for(basin_point in basin_points)
                {
                    if(!basin_point.is_checked)
                    {
                        unchecked_basin_points_found = true
                        break
                    }
                }

                // Criteria for a basin point: value != 9

                // Get next unchecked basin point.
                var unchecked_basin_point_index = 0
                for(basin_point_index in 0 until basin_points.size)
                {
                    if (!basin_points[basin_point_index].is_checked)
                    {
                        unchecked_basin_point_index = basin_point_index
                        break
                    }
                }

                // Note: For each of the following checks, make sure that an already found point is not added to the list again.

                // Check point above.

                if (height_map_padded[basin_points[unchecked_basin_point_index].coordinates.row_index - 1][basin_points[unchecked_basin_point_index].coordinates.column_index] != 9 &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index - 1, basin_points[unchecked_basin_point_index].coordinates.column_index), true)) &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index - 1, basin_points[unchecked_basin_point_index].coordinates.column_index), false)))
                {
                    basin_points.add(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index - 1, basin_points[unchecked_basin_point_index].coordinates.column_index), false))
                }

                // Check point right.

                if (height_map_padded[basin_points[unchecked_basin_point_index].coordinates.row_index][basin_points[unchecked_basin_point_index].coordinates.column_index + 1] != 9 &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index, basin_points[unchecked_basin_point_index].coordinates.column_index + 1), true)) &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index, basin_points[unchecked_basin_point_index].coordinates.column_index + 1), false)))
                {
                    basin_points.add(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index, basin_points[unchecked_basin_point_index].coordinates.column_index + 1), false))
                }

                // Check point below.

                if (height_map_padded[basin_points[unchecked_basin_point_index].coordinates.row_index + 1][basin_points[unchecked_basin_point_index].coordinates.column_index] != 9 &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index + 1, basin_points[unchecked_basin_point_index].coordinates.column_index), true)) &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index + 1, basin_points[unchecked_basin_point_index].coordinates.column_index), false)))
                {
                    basin_points.add(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index + 1, basin_points[unchecked_basin_point_index].coordinates.column_index), false))
                }

                // Check point left.

                if (height_map_padded[basin_points[unchecked_basin_point_index].coordinates.row_index][basin_points[unchecked_basin_point_index].coordinates.column_index - 1] != 9 &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index, basin_points[unchecked_basin_point_index].coordinates.column_index - 1), true)) &&
                        !basin_points.contains(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index, basin_points[unchecked_basin_point_index].coordinates.column_index - 1), false)))
                {
                    basin_points.add(basin_point(point(basin_points[unchecked_basin_point_index].coordinates.row_index, basin_points[unchecked_basin_point_index].coordinates.column_index - 1), false))
                }

                basin_points[unchecked_basin_point_index].is_checked = true;
            }

            basin_point_counts.add(basin_points.size)

        }

        // Get three largest basin point counts and multiply them.

        val sorted_basin_counts = basin_point_counts.sortedDescending()

        return sorted_basin_counts[0] * sorted_basin_counts[1] * sorted_basin_counts[2]
    }

    val testInput = readInput("Day09_test")

    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}