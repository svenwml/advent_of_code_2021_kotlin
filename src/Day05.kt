fun main() {

    data class Point(val x: Int, val y: Int)
    data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    fun create_line_map(line: Line, x_max: Int, y_max: Int) : MutableList<MutableList<Int>>
    {
        // Generate points to mark.

        var points_to_mark = mutableListOf<Point>()

        if(line.y1 == line.y2)
        {
            // We have a horizontal line.
            for(x in minOf(line.x1, line.x2) .. maxOf(line.x1, line.x2))
            {
                points_to_mark.add(Point(x, line.y1))
            }
        }
        else if(line.x1 == line.x2)
        {
            // We have a vertical line.
            for(y in minOf(line.y1, line.y2) .. maxOf(line.y1, line.y2))
            {
                points_to_mark.add(Point(line.x1, y))
            }
        }
        else
        {
            // We have a diagonal line.
            var current_x = line.x1
            var current_y = line.y1

            while(current_x != line.x2 && current_y != line.y2)
            {
                points_to_mark.add(Point(current_x, current_y))

                if(current_x < line.x2)
                {
                    current_x++
                }
                else
                {
                    current_x--
                }

                if(current_y < line.y2)
                {
                    current_y++
                }
                else
                {
                    current_y--
                }
            }

            points_to_mark.add(Point(current_x, current_y))

            // Validate that all points are marked.
            if(points_to_mark[0].x != line.x1 ||
                    points_to_mark[0].y != line.y1 ||
                    points_to_mark[points_to_mark.size - 1].x != line.x2 ||
                    points_to_mark[points_to_mark.size - 1].y != line.y2)
            {
                throw Exception("Detected an error while calculating diagonal points (missing start point or end point)!")
            }
        }

        var line_map : MutableList<MutableList<Int>> = ArrayList()

        for(row in 0 .. y_max)
        {
            var row_content = mutableListOf<Int>()

            for(column in 0 .. x_max)
            {
                if(points_to_mark.contains(Point(column, row)))
                {
                    row_content.add(1)
                }
                else
                {
                    row_content.add(0)
                }
            }

            line_map.add(row_content)
        }

        return line_map
    }

    fun sum_up_line_maps(line_maps: MutableList<MutableList<MutableList<Int>>>) : MutableList<MutableList<Int>>
    {
        if(line_maps.size < 1)
        {
            throw Exception("Input data is empty!")
        }

        var summed_up_line_maps = line_maps[0]

        for(line_map_index in 1 until line_maps.size)
        {
            print("Sum up line map " + line_map_index + "/" + line_maps.size + "\n")

            // Each row...
            for(row_index in 0 until line_maps[0].size)
            {
                // Each column...
                for(column_index in 0 until line_maps[line_map_index].size)
                {
                    summed_up_line_maps[row_index][column_index] += line_maps[line_map_index][row_index][column_index]
                }
            }
        }

        print("Line maps summed up.\n")

        return summed_up_line_maps
    }

    fun count_points_where_lines_overlap(summed_up_line_map: MutableList<MutableList<Int>>, overlapping_line_count_min: Int) : Int
    {
        var overlap_points = 0

        for(row in summed_up_line_map)
        {
            for(overlap_count in row)
            {
                if(overlap_count >= overlapping_line_count_min)
                {
                    overlap_points++
                }
            }
        }

        return overlap_points
    }

    fun generate_lines(input: List<String>, ignore_diagonal_lines: Boolean) : MutableList<Line>
    {
        var lines = mutableListOf<Line>()

        // Parse all line coordinates from input.
        for(vent_line in input)
        {
            val points_coordinates = "[0-9]+".toRegex().findAll(vent_line)

            if(points_coordinates.count() != 4)
            {
                throw Exception("Expected four coordinates (x1,y1,x2,y2) for a line, but got " + points_coordinates.count() + " !")
            }

            val line = Line(x1 = points_coordinates.elementAt(0).value.toInt(),
                    y1 = points_coordinates.elementAt(1).value.toInt(),
                    x2 = points_coordinates.elementAt(2).value.toInt(),
                    y2 = points_coordinates.elementAt(3).value.toInt())

            if(!ignore_diagonal_lines || line.x1 == line.x2 || line.y1 == line.y2)
            {
                lines.add(line)
            }
        }

        return lines
    }

    fun generate_line_maps(input: List<String>, ignore_diagonal_lines: Boolean) : MutableList<MutableList<MutableList<Int>>>
    {
        var lines = generate_lines(input, ignore_diagonal_lines)

        print("Lines generated.\n")

        // Get the dimensions of the required map.
        var x_max = 0
        var y_max = 0

        for(line in lines)
        {
            x_max = maxOf(line.x1, line.x2, x_max)
            y_max = maxOf(line.x1, line.y2, y_max)
        }

        // For each line, create a map on which the line is marked.

        var line_maps : MutableList<MutableList<MutableList<Int>>> = ArrayList()

        var line_map_counter = 0
        for(line in lines)
        {
            print("Create line map " + line_map_counter + "/" + lines.size + "\n")
            line_map_counter++

            val line_map = create_line_map(line, x_max, y_max)

            line_maps.add(line_map)
        }

        print("Line maps generated.\n")

        return line_maps
    }

    fun part1(input: List<String>): Int {

        val line_maps = generate_line_maps(input, ignore_diagonal_lines = true)

        val summed_up_line_map = sum_up_line_maps(line_maps)

        // Search for the points where at least two lines overlap.
        val overlapping_points = count_points_where_lines_overlap(summed_up_line_map, 2)

        return overlapping_points
    }

    fun part2(input: List<String>): Int {

        val line_maps = generate_line_maps(input, ignore_diagonal_lines = false)

        val summed_up_line_map = sum_up_line_maps(line_maps)

        // Search for the points where at least two lines overlap.
        val overlapping_points = count_points_where_lines_overlap(summed_up_line_map, 2)

        return overlapping_points
    }

    print("Running...\n")

    val testInput = readInput("Day05_test")

    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))

    print("Finished.")
}