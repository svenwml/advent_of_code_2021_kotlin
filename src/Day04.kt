fun main() {

    fun build_bingo_number_sequence(input: List<String>) : List<Int>
    {
        // Check if the input data has a valid length.
        if(input.size < 1)
        {
            throw Exception("Input data is empty!")
        }

        // Read the bingo number sequence from the first input data line.
        return input[0].split(",").map{it.toInt()}
    }

    fun build_bingo_boards(input: List<String>) : MutableList<MutableList<MutableList<Int>>>
    {
        // Check if the input data has a valid length.
        if(input.size < 1)
        {
            throw Exception("Input data is empty!")
        }

        // Bingo boards start in the third row with one row spacing between each board.
        var input_row_mark = 2

        // Build internal bingo board representation.
        var bingo_boards : MutableList<MutableList<MutableList<Int>>> = ArrayList()

        while(input_row_mark < (input.size - 1))
        {
            var bingo_board: MutableList<MutableList<Int>> = ArrayList()

            while ((input[input_row_mark] != "") && (input_row_mark < input.size)) {
                // In order to be able to split based on space delimiters, we need to remove spaces in front of the first number.
                var bingo_board_row = input[input_row_mark]
                while (bingo_board_row[0] == ' ') {
                    bingo_board_row = bingo_board_row.drop(1)
                }

                bingo_board.add(bingo_board_row.split(regex = Regex("[ ]+")).map { it.toInt() }.toMutableList())
                input_row_mark++

                if(input_row_mark == input.size)
                {
                    break
                }
            }

            bingo_boards.add(bingo_board)
            input_row_mark++
        }

        return bingo_boards
    }

    // Replaces all occurrencies of the passed bingo_number in the passed input with -1.
    // Returns the updated list.
    fun update_bingo_boards(input: MutableList<MutableList<MutableList<Int>>>, bingo_number: Int) : MutableList<MutableList<MutableList<Int>>>
    {
        // Check if the input data has a valid length.
        if(input.size < 1)
        {
            throw Exception("Input data is empty!")
        }

        var updated_bingo_boards = input

        // Check each bingo board...
        for(bingo_board_index in 0 .. (updated_bingo_boards.size - 1))
        {
            // Each row...
            for(bingo_board_row_index in 0 .. (updated_bingo_boards[bingo_board_index].size - 1))
            {
                // Each number...
                for(bingo_board_number_index in 0 .. (updated_bingo_boards[bingo_board_index][bingo_board_row_index].size - 1))
                {
                    if(updated_bingo_boards[bingo_board_index][bingo_board_row_index][bingo_board_number_index] == bingo_number)
                    {
                        // If the current number of the current board row of the current board matches the passed bingo number,
                        // mark the number as "checked" by setting it to a defined value which is never used on the boards.
                        updated_bingo_boards[bingo_board_index][bingo_board_row_index][bingo_board_number_index] = -1
                    }
                }
            }
        }

        return updated_bingo_boards
    }

    // Checks the passed bingo lists for a "bingo" (completely marked row or column.
    // Returns a list of the boards having a "bingo".
    fun check_bingo(input: MutableList<MutableList<MutableList<Int>>>) : List<Int>
    {
        // Check if the input data has a valid length.
        if(input.size < 1)
        {
            throw Exception("Input data is empty!")
        }

        var bingo_boards_indexes = mutableListOf<Int>()

        // Check all rows.

        // Check each bingo board...
        for(bingo_board_index in 0 .. (input.size - 1))
        {
            // Each row...
            for(bingo_board_row_index in 0 .. (input[bingo_board_index].size - 1))
            {
                // Each number...
                var bingo = true
                for(bingo_board_number_index in 0 .. (input[bingo_board_index][bingo_board_row_index].size - 1))
                {
                    if(input[bingo_board_index][bingo_board_row_index][bingo_board_number_index] != -1)
                    {
                        bingo = false
                    }
                }

                if(bingo)
                {
                    if(false == bingo_boards_indexes.contains(bingo_board_index))
                    {
                        bingo_boards_indexes.add(bingo_board_index)
                    }
                }
            }
        }

        // Check all columns.

        // Check each bingo board...
        for(bingo_board_index in 0 .. (input.size - 1))
        {
            // Each column...
            for(bingo_board_column_index in 0 .. (input[bingo_board_index][0].size - 1))
            {
                // Each number...
                var bingo = true

                for(bingo_board_row_index in 0 .. (input[bingo_board_index].size - 1))
                {
                    if(input[bingo_board_index][bingo_board_row_index][bingo_board_column_index] != -1)
                    {
                        bingo = false
                    }
                }

                if(bingo)
                {
                    if(false == bingo_boards_indexes.contains(bingo_board_index))
                    {
                        bingo_boards_indexes.add(bingo_board_index)
                    }
                }
            }
        }

        return bingo_boards_indexes
    }

    fun calculate_board_score(input: MutableList<MutableList<Int>>, multiplicator: Int): Int
    {
        // Check if the input data has a valid length.
        if(input.size < 1)
        {
            throw Exception("Input data is empty!")
        }

        var bingo_board_score = 0

        // Each row...
        for(bingo_board_row in input)
        {
            // Each number...
            for(bingo_board_number in bingo_board_row)
            {
                if(bingo_board_number != -1)
                {
                    bingo_board_score += bingo_board_number
                }
            }
        }

        bingo_board_score *= multiplicator

        return bingo_board_score
    }

    fun part1(input: List<String>): Int {

        var bingo_number_sequence = build_bingo_number_sequence(input)
        var bingo_boards = build_bingo_boards(input)

        for(bingo_number in bingo_number_sequence)
        {
            bingo_boards = update_bingo_boards(bingo_boards, bingo_number)

            var winning_board_indexes = check_bingo(bingo_boards)

            if(winning_board_indexes.size > 0)
            {
                // We have a "bingo".
                return calculate_board_score(bingo_boards[winning_board_indexes[0]], bingo_number)
            }
        }

        throw Exception("No winning bingo board found!")
    }

    fun part2(input: List<String>): Int {

        var bingo_number_sequence = build_bingo_number_sequence(input)
        var bingo_boards = build_bingo_boards(input)

        var last_winning_board_score = -1
        var finished_board_indexes = mutableListOf<Int>()
        for(bingo_number in bingo_number_sequence)
        {
            bingo_boards = update_bingo_boards(bingo_boards, bingo_number)

            // Boards which have a "bingo" in this cycle.
            var bingo_board_indexes = check_bingo(bingo_boards)

            for(bingo_board_index in bingo_board_indexes)
            {
                if(finished_board_indexes.contains(bingo_board_index) == false)
                {
                    finished_board_indexes.add(bingo_board_index)
                }

                if(finished_board_indexes.size == bingo_boards.size)
                {
                    return calculate_board_score(bingo_boards[bingo_board_index], bingo_number)
                }
            }
        }

        if(last_winning_board_score != -1)
        {
            return last_winning_board_score
        }

        throw Exception("No winning bingo board found!")
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}