fun main() {

    data class parsed_input(var signal_patterns : MutableList<List<String>>, var four_digit_output_values : MutableList<List<String>>)

    // For each displayed digit of the 7 segment display, a specific number of segments are active.
    // Displayed 0 -> 6 active segments, Displayed 1 -> 2 active segments, ...
    val number_of_active_segments_for_digit = arrayOf(6,2,5,5,4,5,6,3,7,6)

    fun parse_input(input: List<String>) : parsed_input
    {
        var signal_patterns = mutableListOf<List<String>>()
        var four_digit_output_values = mutableListOf<List<String>>()

        for(line in input)
        {
            val split_line = line.split(" | ")

            var signal_pattern = split_line[0].split(" ")

            for(i in 0 until signal_pattern.size)
            {
                signal_pattern[i].replace("\\s".toRegex(), "")
            }

            signal_patterns.add(signal_pattern)

            var four_digit_output_value = split_line[1].removePrefix(" ").split(" ")

            for(i in 0 until four_digit_output_value.size)
            {
                four_digit_output_value[i].replace("\\s".toRegex(), "")
            }

            four_digit_output_values.add(four_digit_output_value)
        }

        return parsed_input(signal_patterns, four_digit_output_values)
    }

    fun part1(input: List<String>): Int {

        val parsed_input = parse_input(input)

        var digit_counts = Array<Int>(10){0}

        // We have four digits per output value.

        // For each output value...
        for(four_digit_output_value in parsed_input.four_digit_output_values)
        {
            // For each of the four digits...
            for(four_digit_output_value_digit in four_digit_output_value)
            {
                when (four_digit_output_value_digit.length) {
                    number_of_active_segments_for_digit[1] ->
                    {
                        digit_counts[1]++
                    }
                    number_of_active_segments_for_digit[4] ->
                    {
                        digit_counts[4]++
                    }
                    number_of_active_segments_for_digit[7] ->
                    {
                        digit_counts[7]++
                    }
                    number_of_active_segments_for_digit[8] ->
                    {
                        digit_counts[8]++
                    }
                }
            }
        }

        val result = digit_counts[1] + digit_counts[4] + digit_counts[7] + digit_counts[8]

        return result
    }

    fun part2(input: List<String>): Int {

        val parsed_input = parse_input(input)

        var output_value_sum = 0

        // For each signal pattern...
        for(line_count in 0 until parsed_input.signal_patterns.size)
        {
            val signal_pattern = parsed_input.signal_patterns[line_count]

            data class seven_segment_assignments(var top: String = "",
                                                 var top_left: String = "",
                                                 var top_right: String = "",
                                                 var center: String = "",
                                                 var bottom_left: String = "",
                                                 var bottom_right: String = "",
                                                 var bottom: String = "")

            var seven_segment_assignment = seven_segment_assignments()

            // Search for the 1: Get the possible assignments for top right and bottom right.
            for(signal in signal_pattern)
            {
                if(signal.length == number_of_active_segments_for_digit[1])
                {
                    seven_segment_assignment.top_right += signal
                    seven_segment_assignment.bottom_right += signal
                }
            }

            // Search for the 7: Get the possible assignments for top (top right and bottom right are already handled
            // by the 1).
            for(signal in signal_pattern)
            {
                if(signal.length == number_of_active_segments_for_digit[7])
                {
                    seven_segment_assignment.top += signal

                    // Filter out assignments that may already be used for top right and bottom right by the 1.

                    for(character in seven_segment_assignment.top_right)
                    {
                        seven_segment_assignment.top = seven_segment_assignment.top.replace(character.toString(), "")
                    }

                    for(character in seven_segment_assignment.bottom_right)
                    {
                        seven_segment_assignment.top = seven_segment_assignment.top.replace(character.toString(), "")
                    }
                }
            }

            // Search for the 4: Get the possible assignments for top left and center (top right and bottom right are already handled
            // by 7 and 1).
            for(signal in signal_pattern)
            {
                if(signal.length == number_of_active_segments_for_digit[4])
                {
                    seven_segment_assignment.top_left += signal
                    seven_segment_assignment.center += signal

                    // Filter out assignments that may already be used for top right and bottom right by the 1 and the 7.

                    for(character in seven_segment_assignment.top_right)
                    {
                        seven_segment_assignment.top_left = seven_segment_assignment.top_left.replace(character.toString(), "")
                        seven_segment_assignment.center = seven_segment_assignment.center.replace(character.toString(), "")
                    }

                    for(character in seven_segment_assignment.bottom_right)
                    {
                        seven_segment_assignment.top_left = seven_segment_assignment.top_left.replace(character.toString(), "")
                        seven_segment_assignment.center = seven_segment_assignment.center.replace(character.toString(), "")
                    }
                }
            }

            // Search for the 8: Get the possible assignments for bottom left and bottom (top, top left, top right, center and bottom right are already handled
            // by 4, 7 and 1).
            for(signal in signal_pattern)
            {
                if(signal.length == number_of_active_segments_for_digit[8])
                {
                    seven_segment_assignment.bottom_left += signal
                    seven_segment_assignment.bottom += signal

                    // Filter out assignments that may already be used for top, top left, top right, center and bottom right by the 1, the 7 and the 4.

                    for(character in seven_segment_assignment.top)
                    {
                        seven_segment_assignment.bottom_left = seven_segment_assignment.bottom_left.replace(character.toString(), "")
                        seven_segment_assignment.bottom = seven_segment_assignment.bottom.replace(character.toString(), "")
                    }

                    for(character in seven_segment_assignment.top_left)
                    {
                        seven_segment_assignment.bottom_left = seven_segment_assignment.bottom_left.replace(character.toString(), "")
                        seven_segment_assignment.bottom = seven_segment_assignment.bottom.replace(character.toString(), "")
                    }

                    for(character in seven_segment_assignment.top_right)
                    {
                        seven_segment_assignment.bottom_left = seven_segment_assignment.bottom_left.replace(character.toString(), "")
                        seven_segment_assignment.bottom = seven_segment_assignment.bottom.replace(character.toString(), "")
                    }

                    for(character in seven_segment_assignment.center)
                    {
                        seven_segment_assignment.bottom_left = seven_segment_assignment.bottom_left.replace(character.toString(), "")
                        seven_segment_assignment.bottom = seven_segment_assignment.bottom.replace(character.toString(), "")
                    }

                    for(character in seven_segment_assignment.bottom_right)
                    {
                        seven_segment_assignment.bottom_left = seven_segment_assignment.bottom_left.replace(character.toString(), "")
                        seven_segment_assignment.bottom = seven_segment_assignment.bottom.replace(character.toString(), "")
                    }

                }
            }

            // Now we have (multiple) possible assignments for each segment.

            var assignment_candidates : ArrayList<MutableList<String>> = ArrayList()

            // Generate possible assignments for the 0.

            var possible_assignments_0 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_left +
                    seven_segment_assignment.top_right +
                    seven_segment_assignment.bottom_left +
                    seven_segment_assignment.bottom_right +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(i in 0 until 10)
            {
                assignment_candidates.add(mutableListOf())
            }

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[0])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_0.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                for(character in seven_segment_assignment.top)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                // Both segments of 1 must be used.

                for(character in seven_segment_assignment.top_right)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    for (character in seven_segment_assignment.bottom_right)
                    {
                        if (!signal.contains(character))
                        {
                            assignment_found = false
                            break
                        }
                    }
                }

                if(assignment_found)
                {
                    // Not both possible segments of the 4's "L" must be used.

                    var occurencies_found = 0
                    for(character in signal)
                    {
                        if(seven_segment_assignment.top_left.contains(character))
                        {
                            occurencies_found++
                        }
                    }
                    if(occurencies_found > 1)
                    {
                        assignment_found = false
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[0].add(signal)
                }
            }

            // Generate possible assignments for the 1.

            var possible_assignments_1 = (seven_segment_assignment.top_right +
                    seven_segment_assignment.bottom_right).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[1])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_1.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[1].add(signal)
                }
            }

            // Generate possible assignments for the 2.

            var possible_assignments_2 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_right +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_left +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[2])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_2.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                // Not both possible segments of 1 must be used.

                var occurencies_found = 0
                for(character in signal)
                {
                    if(seven_segment_assignment.top_right.contains(character))
                    {
                        occurencies_found++
                    }
                }
                if(occurencies_found > 1)
                {
                    assignment_found = false
                }

                if(assignment_found)
                {
                    // Not both possible segments of the 4's "L" must be used.

                    var occurencies_found = 0
                    for(character in signal)
                    {
                        if(seven_segment_assignment.top_left.contains(character))
                        {
                            occurencies_found++
                        }
                    }
                    if(occurencies_found > 1)
                    {
                        assignment_found = false
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[2].add(signal)
                }
            }

            // Generate possible assignments for the 3.

            var possible_assignments_3 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_right +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_right +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[3])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_3.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                // Both segments of 1 must be used.

                for(character in seven_segment_assignment.top_right)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                for(character in seven_segment_assignment.bottom_right)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[3].add(signal)
                }
            }

            // Generate possible assignments for the 4.

            var possible_assignments_4 = (seven_segment_assignment.top_left +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_right).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[4])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_4.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[4].add(signal)
                }
            }

            // Generate possible assignments for the 5.

            var possible_assignments_5 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_left +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_right +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[5])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_5.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                // Not both possible segments of 1 must be used.

                var occurencies_found = 0
                for(character in signal)
                {
                    if(seven_segment_assignment.top_right.contains(character))
                    {
                        occurencies_found++
                    }
                }
                if(occurencies_found > 1)
                {
                    assignment_found = false
                }

                // Both segments of the 4's "L" must be used.
                if(assignment_found) {
                    for (character in seven_segment_assignment.top_left) {
                        if (!signal.contains(character)) {
                            assignment_found = false
                            break
                        }
                    }
                }

                if(assignment_found)
                {
                    for (character in seven_segment_assignment.center)
                    {
                        if (!signal.contains(character))
                        {
                            assignment_found = false
                            break
                        }
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[5].add(signal)
                }
            }

            // Generate possible assignments for the 6.

            var possible_assignments_6 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_left +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_left +
                    seven_segment_assignment.bottom_right +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[6])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_6.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                // Not both possible segments of 1 must be used.

                var occurencies_found = 0
                for(character in signal)
                {
                    if(seven_segment_assignment.top_right.contains(character))
                    {
                        occurencies_found++
                    }
                }
                if(occurencies_found > 1)
                {
                    assignment_found = false
                }

                if(assignment_found)
                {
                    assignment_candidates[6].add(signal)
                }
            }

            // Generate possible assignments for the 7.

            var possible_assignments_7 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_right +
                    seven_segment_assignment.bottom_right).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[7])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_7.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[7].add(signal)
                }
            }

            // Generate possible assignments for the 8.

            var possible_assignments_8 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_left +
                    seven_segment_assignment.top_right +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_left +
                    seven_segment_assignment.bottom_right +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[8])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_8.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[8].add(signal)
                }
            }

            // Generate possible assignments for the 9.

            var possible_assignments_9 = (seven_segment_assignment.top +
                    seven_segment_assignment.top_left +
                    seven_segment_assignment.top_right +
                    seven_segment_assignment.center +
                    seven_segment_assignment.bottom_right +
                    seven_segment_assignment.bottom).toCharArray().map{it.toString()}.toTypedArray().distinct()

            for(signal in signal_pattern)
            {
                if(signal.length != number_of_active_segments_for_digit[9])
                {
                    continue
                }

                var assignment_found = true

                for(character in signal)
                {
                    if (!possible_assignments_9.contains(character.toString()))
                    {
                        assignment_found = false
                        break
                    }
                }

                // All segments of 4 must be used.

                for(character in seven_segment_assignment.top_left)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                for(character in seven_segment_assignment.center)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                for(character in seven_segment_assignment.top_right)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                for(character in seven_segment_assignment.bottom_right)
                {
                    if(!signal.contains(character))
                    {
                        assignment_found = false
                        break
                    }
                }

                if(assignment_found)
                {
                    assignment_candidates[9].add(signal)
                }
            }

            // Check if only one candidate per number is left.

            for(i in 0 until assignment_candidates.size)
            {
                if(assignment_candidates[i].size != 1)
                {
                    throw Exception("Invalid number of assignment candidates after filtering, expected 1, got: " + assignment_candidates[i].size + "\n")
                }
            }

            // Match output values to candidates (all permutations possible).

            var output_value = ""

            for(output_value_index in 0 until parsed_input.four_digit_output_values[line_count].size)
            {
                for(number in 0 until assignment_candidates.size)
                {
                    if(assignment_candidates[number][0].length == parsed_input.four_digit_output_values[line_count][output_value_index].length)
                    {
                        var match = true
                        for(character in assignment_candidates[number][0])
                        {
                            if(!parsed_input.four_digit_output_values[line_count][output_value_index].contains(character))
                            {
                                match = false
                            }
                        }

                        if(match)
                        {
                            output_value += number.toString()
                        }
                    }
                }
            }

            // Sum up output values.
            output_value_sum += output_value.toInt()
        }

        return output_value_sum
    }

    val testInput = readInput("Day08_test")

    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}