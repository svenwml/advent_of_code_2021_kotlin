fun main() {

    fun validate_and_count_number_of_digits(input: List<String>): Int {
        if(input.size == 0)
        {
            throw Exception("Input list size is 0!")
        }

        // Count the number of digits and make sure it is the same for each diagnostic report item.
        var number_of_digits = input[0].length
        for(diagnostic_report_item in input)
        {
            if(diagnostic_report_item.length != number_of_digits)
            {
                throw Exception("Number of digits must be the same for each diagnostic report item!")
            }
        }

        return number_of_digits
    }

    fun filter_list_for_criteria(input: List<String>, filter_for_most_common_values: Boolean) : String {

        var number_of_digits = validate_and_count_number_of_digits(input)

        var remaining_diagnostic_report_items_for_current_criteria = input
        var current_bit_position = 0
        var ones_in_current_bit_position = 0

        while((remaining_diagnostic_report_items_for_current_criteria.size > 1) &&
                (current_bit_position < number_of_digits))
        {
            // Count number of ones in the current bit position.
            for (diagnostic_report_item in remaining_diagnostic_report_items_for_current_criteria) {
                if (diagnostic_report_item[current_bit_position] == '1')
                {
                    ones_in_current_bit_position += 1
                }
            }

            if (filter_for_most_common_values && (ones_in_current_bit_position >= (remaining_diagnostic_report_items_for_current_criteria.size - ones_in_current_bit_position)) ||
                    !filter_for_most_common_values && (ones_in_current_bit_position < (remaining_diagnostic_report_items_for_current_criteria.size - ones_in_current_bit_position)))
            {
                // Keep all report items with a '1' in the current bit position.
                var remaining_diagnostic_report_items_for_current_criteria_next = mutableListOf<String>()

                for (remaining_diagnostic_report_item in remaining_diagnostic_report_items_for_current_criteria)
                {
                    if (remaining_diagnostic_report_item[current_bit_position] == '1')
                    {
                        remaining_diagnostic_report_items_for_current_criteria_next.add(remaining_diagnostic_report_item)
                    }
                }

                remaining_diagnostic_report_items_for_current_criteria = remaining_diagnostic_report_items_for_current_criteria_next
            }
            else
            {
                // Keep all report items with a '0' in the current bit position.
                var remaining_diagnostic_report_items_for_current_criteria_next = mutableListOf<String>()

                for (remaining_diagnostic_report_item in remaining_diagnostic_report_items_for_current_criteria) {
                    if (remaining_diagnostic_report_item[current_bit_position] == '0') {
                        remaining_diagnostic_report_items_for_current_criteria_next.add(remaining_diagnostic_report_item)
                    }
                }

                remaining_diagnostic_report_items_for_current_criteria = remaining_diagnostic_report_items_for_current_criteria_next
            }

            current_bit_position += 1
            ones_in_current_bit_position = 0
        }

        // At this point, we expect only one string to be left.
        if(remaining_diagnostic_report_items_for_current_criteria.size > 1)
        {
            throw Exception("More than one diagnostic report item remaining after filtering!")
        }

        return remaining_diagnostic_report_items_for_current_criteria[0]
    }

    fun part1(input: List<String>): Int {

        var number_of_digits = validate_and_count_number_of_digits(input)

        // Count the number of ones for each digit column.
        var  digit_one_count_array = IntArray(number_of_digits)
        for(diagnostic_report_item in input)
        {
            for(column in 0..(number_of_digits - 1))
            {
                if(diagnostic_report_item[column] == '1')
                {
                    digit_one_count_array[column] += 1
                }
            }
        }

        // Compose the gamma rate by using the most common / least common value of each column.
        var gamma_rate = ""
        var epsilon_rate = ""

        for(column in 0 .. (number_of_digits - 1))
        {
            // If there are more ones than zeros in the column, the respective gamma rate digit is '1', else '0'.
            if(digit_one_count_array[column] > (input.size - digit_one_count_array[column]))
            {
                gamma_rate += '1'
                epsilon_rate += '0'
            }
            else
            {
                gamma_rate += '0'
                epsilon_rate += '1'
            }
        }

        // Calculate the power by multiplying gamma rate and epsilon rate.
        var power = Integer.parseInt(gamma_rate,2 ) * Integer.parseInt(epsilon_rate, 2)

        return power
    }

    fun part2(input: List<String>): Int {

        var oxygen_generator_rating = filter_list_for_criteria(input, filter_for_most_common_values = true)
        var co2_filter_rating = filter_list_for_criteria(input, filter_for_most_common_values = false)

        return Integer.parseInt(oxygen_generator_rating,2 ) * Integer.parseInt(co2_filter_rating,2 )
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}