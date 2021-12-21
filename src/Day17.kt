fun main() {

    data class probe(var x_position: Int, var y_position: Int, var x_velocity: Int, var y_velocity: Int)
    data class target_area(var x_min: Int, var x_max: Int, var y_min: Int, var y_max: Int)
    data class simulation_output(val maximum_y_position: Int, val steps_to_reach_target_area: Int)

    val simulation_start_velocity_min = -5000
    val simulation_start_velocity_max = 5000

    fun simulate_probe(input: List<String>): simulation_output
    {
        // Parse target area.
        val split_input = input[0].split(" ")
        //  Drop "x=".                                     Drop ',' delimiter. Drop '.' delimiters.
        val x_range = split_input[2].substring(2).dropLast(1).split(".")
        //  Drop "y=".                                     Drop '.' delimiters.
        val y_range = split_input[3].substring(2).split(".")

        var target_area = target_area(x_range[0].toInt(), x_range[2].toInt(), y_range[0].toInt(), y_range[2].toInt())

        var maximum_y_position = 0

        var steps_to_reach_target_area = 0

        for(initial_x_velocity in simulation_start_velocity_min .. simulation_start_velocity_max)
        {
            for(initial_y_velocity in simulation_start_velocity_min .. simulation_start_velocity_max)
            {
                var probe = probe(0, 0, initial_x_velocity, initial_y_velocity)

                var target_area_reached = false
                var step_counter = 0
                var maximum_y_position_tmp = 0

                while(true)
                {
                    probe.x_position += probe.x_velocity
                    probe.y_position += probe.y_velocity

                    if(probe.x_position in target_area.x_min .. target_area.x_max &&
                            probe.y_position in target_area.y_min .. target_area.y_max)
                    {
                        target_area_reached = true
                        break
                    }

                    if(probe.x_velocity > 0)
                    {
                        probe.x_velocity--
                    }
                    else if(probe.x_velocity < 0)
                    {
                        probe.x_velocity++
                    }

                    probe.y_velocity--

                    if(probe.x_position > target_area.x_max && probe.x_velocity >= 0 ||
                            probe.x_position < target_area.x_min && probe.x_velocity <= 0 ||
                            probe.y_position < target_area.y_min && probe.y_velocity < 0)
                    {
                        break
                    }

                    if(probe.y_position > maximum_y_position_tmp)
                    {
                        maximum_y_position_tmp = probe.y_position
                    }

                    step_counter++
                }

                if(target_area_reached)
                {
                    if(maximum_y_position_tmp > maximum_y_position)
                    {
                        maximum_y_position = maximum_y_position_tmp
                    }

                    steps_to_reach_target_area++
                }
            }
        }

        return simulation_output(maximum_y_position, steps_to_reach_target_area)
    }

    fun part1(input: List<String>): Int {

        return simulate_probe(input).maximum_y_position
    }

    fun part2(input: List<String>): Int {

        return simulate_probe(input).steps_to_reach_target_area
    }

    val testInput = readInput("Day17_test")

    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")

    println(part1(input))
    println(part2(input))
}